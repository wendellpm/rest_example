The application uses a MySQL database, so a MySQL server must be running on the user's computer in order to use the application with database persistence. This, unfortunately, is a separate step that a user has to complete, rather than a step to be completed automatically through the directives in the pom.xml file.

Using Maven:

mvn run:tomcat

will start the Tomcat web server. 

To add data to the database, there are two client user interfaces, within the com/wpmassociates/comcast/client package, one from the command line, one a graphical client.  These can be run from the command line in a separate instance of the console. In other words a second console in addition to the console from the console used above to run the tomcat server. Either of the follow commands may be used depending on whether the command line interface or the graphical interface is used:

mvn exec:java -Dexec.mainClass=com.wpmassociates.comcast.client.HttpClientRequestCommandLine 

or

Dexec.mainClass=com.wpmassociates.comcast.client.HttpClientRequestGraphicalInterface

The command line interface will prompt for these three values:
the user id
duration (in days)
text

The graphical client generates a window where there are input boxes each of the same three values: 
partner id 
duration
text
plus a send button to send the data to the server.

To check for a given partner id a browser may be opened, then the URL and the partner id are entered into the address box. Assuming the Tomcat server is running on localhost: http://localhost:8080/comcast/<partnerId>, e.g. ../10. That will return either the JSON code for that partner id or some other message, including any error, as plain text in the browser window.

There is a default.properties file within the src/main/resources directory that contains key/values to determine whether the application should use a SQL database (in this case MySQL) or use an in-memory, map-based database. 

The property for this has the key useMap which is set by default to the value no meaning that the MySQL database is used, rather than the in-memory data store.

If the value for the useMap key is set to yes, then the in-memory database is used. The other key/values, as indicated below, must be changed to use with the local database. Specifically a username and a password must be provided to access the MySQL database (json) and relevant database table (also named json).

The database itself needs to be created first from the MySQL prompt as follows:

mysql>create database json;

The enclosed json.sql file creates the one table, also named json, in the data base json.

$ mysql -u <username> -p <password> json < json.sql

MySQL may require the <password> value to be set separately, thus:

$ mysql -u <username> -p json < json.sql <press enter key>
enter password:<password> <press enter key>

Then set the key/values in the default.properties file, as follows, before running the application server:

databaseName=json		leave as is
useMap=no				leave as is
dbUsername=<username>		add username for local server
dbPassword=<password>		add password for local server
mysqlUrl=jdbc:mysql://localhost:3306	leave as is, default port for mysql
driverName=com.mysql.jdbc.Driver		leave as is