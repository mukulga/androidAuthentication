package mukul.authenticationsample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    Button submitBtn;
    String userId;
    String pwd;
    LoginActivity thisClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisClass = this;
        setContentView(R.layout.activity_login);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUid = findViewById(R.id.editUid);
                EditText editPwd = findViewById(R.id.editPwd);
                userId = editUid.getText().toString();
                pwd = editPwd.getText().toString();
                new DBConnectionTask().execute();
            }
        });
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
                rs = stmt.executeQuery("SELECT * FROM public.\"Users\" where \"userId\" = '" + userId + "' and \"password\" = '" + pwd + "'");
                if (rs.next()) {
                    count = 1;
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
                    AuthenticationHelper.showDialog("Sign in", "Sign in successful", "OK", thisClass);
                } else {
                    AuthenticationHelper.showDialog("Sign in", "User id or password is wrong", "OK", thisClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
