# androidAuthentication
This is a sample Android app, providing following two features:  
a) Registration form, that can accept user details and save it in a back-end database.  
b) Login form, that accepts user-id and password and do authentication against the back-end database.

The back-end data of this app resides on AWS RDS PostgreSQL service. A JDBC driver residing in the app, makes the app directly connect to the remote DB as per the user's actions. One of the design alternative could be the app, to connect to the database via something like a REST API, which hasn't been done in this app.
