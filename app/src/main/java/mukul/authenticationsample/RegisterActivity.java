package mukul.authenticationsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterActivity extends AppCompatActivity {

    Button submitBtn;
    String userId;
    String pwd;
    String rePwd;
    String fName;
    String lName;
    String email;
    RegisterActivity thisClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        thisClass = this;
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUid = findViewById(R.id.editUid);
                EditText editPwd = findViewById(R.id.editPwd);
                EditText editRePwd = findViewById(R.id.editRePwd);
                EditText editFname = findViewById(R.id.editFname);
                EditText editLname = findViewById(R.id.editLname);
                EditText editEmail = findViewById(R.id.editEmail);
                userId = editUid.getText().toString();
                pwd = editPwd.getText().toString();
                rePwd = editRePwd.getText().toString();
                fName = editFname.getText().toString();
                lName = editLname.getText().toString();
                email = editEmail.getText().toString();
                // do front-end validations
                boolean isAnyError = false;
                StringBuffer errMsg = new StringBuffer();
                if (userId.length() < 3 || userId.length() > 15) {
                    isAnyError = true;
                    errMsg.append("User id must be of length between 3 and 15.\n");
                }

                if (pwd.equals(rePwd)) {
                    if (pwd.length() < 5 || pwd.length() > 12) {
                        isAnyError = true;
                        errMsg.append("Password must be of length between 5 and 12.\n");
                    }
                }
                else {
                    isAnyError = true;
                    errMsg.append("The two password fields, do not contain the same values.\n");
                }

                if (fName.length() < 1 || fName.length() > 20) {
                    isAnyError = true;
                    errMsg.append("First name must be of length between 1 and 20.\n");
                }

                if (lName.length() < 1 || lName.length() > 20) {
                    isAnyError = true;
                    errMsg.append("Last name must be of length between 1 and 20.\n");
                }

                if (!isValidEmail(email)) {
                    isAnyError = true;
                    errMsg.append("Email address is not valid.\n");
                }

                if (isAnyError) {
                    AuthenticationHelper.showDialog("Registration form error", errMsg.toString(), "OK", thisClass);
                }
                else {
                    new DBConnectionTask().execute();
                }
            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private class DBConnectionTask extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            Connection conn = null;
            ResultSet rs = null;
            Statement stmt = null;
            Integer count = 0;
            String jdbcUrl = "jdbc:postgresql://mobiledev1.c2avwmhnlshz.ap-south-1.rds.amazonaws.com:5432/mobileAppDb";
            String username = "user1";
            String password = "abc#123Aws";
            try {
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(jdbcUrl, username, password);
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM public.\"Users\" where \"userId\" = '" + userId + "'");
                if (rs.next()) {
                    count = -1;
                } else {
                    count = stmt.executeUpdate("INSERT INTO public.\"Users\" (\"userId\", \"password\", \"fName\", \"lName\", \"email\") values ('" + userId + "', '" + pwd + "', '" + fName + "', '" + lName + "', '" + email + "')");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return count;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            try {
                if (result.equals(Integer.valueOf(1))) {
                    AuthenticationHelper.showDialog("Registration", "Record added successfully", "OK", thisClass);
                } else if (result.equals(Integer.valueOf(-1))) {
                    AuthenticationHelper.showDialog("Registration", "User already exists", "OK", thisClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
