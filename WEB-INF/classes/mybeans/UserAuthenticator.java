package mybeans;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.sql.*; 

public class UserAuthenticator {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs4010";
    static final String USER = "cs4010";
    static final String PASS = "cs4010";

    public UserAuthenticator() {
    }

    public static boolean tryLogin(String login_id, String login_pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " SELECT loginpass FROM jcs436login WHERE loginid='" + login_id + ";";
            ResultSet rs = stmt.executeQuery(this_query);
            if (rs.next()) {
                return rs.getString("loginpass").equals(login_pass);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            log("SQL error in user authenticator");
            return false;
        }

        return false;
    }

    public void log(String s){

        try {
            File outFile = new File("~/tomcat/my_log");
            outFile.createNewFile();
            PrintWriter fileWriter = new PrintWriter(new FileOutputStream(outFile,true));
            fileWriter.println(s+" at: " + new Date(System.currentTimeMillis()).toString());
            fileWriter.close();
        } catch (IOException ex) {

        }
    }

}

/*        MariaDB [cs4010]> describe jcs436login
        -> ;
        +-----------+-------------+------+-----+---------+----------------+
        | Field     | Type        | Null | Key | Default | Extra          |
        +-----------+-------------+------+-----+---------+----------------+
        | id        | int(11)     | NO   | PRI | NULL    | auto_increment |
        | loginid   | varchar(25) | NO   |     | NULL    |                |
        | loginpass | varchar(25) | NO   |     | NULL    |                |
        +-----------+-------------+------+-----+---------+----------------+
        3 rows in set (0.00 sec)*/




