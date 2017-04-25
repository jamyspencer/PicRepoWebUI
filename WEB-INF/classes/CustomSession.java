import java.util.*;
import java.sql.*;


public class CustomSession {
    private    String ip;
    private    long expires;
    private    boolean userAuthenticated;
    private    String ID;

    public CustomSession(){
        this.expires = setExpires();
        this.ID = getRandomString();
    }

    public CustomSession(String ip){
        this.ip = ip;
        this.expires = setExpires();
        this.ID = getRandomString();
    }

    public boolean isExpired(){
        Date now = new Date();
        return now.getTime() > this.expires;
    }

    public long setExpires(){
        Date now = new Date();
        return now.getTime() + 900000;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isUserAuthenticated() {
        return userAuthenticated;
    }

    public void setUserAuthenticated(boolean userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String getRandomString(){
        byte[] randbyte=new byte[32];
        Random rand  = new Random(System.currentTimeMillis());
        for (int idx = 0; idx < 32; ++idx) {
            int randomInt = rand.nextInt(26); //0<=randomInt<26
            //System.out.println(randomInt);
            randbyte[idx]=(byte)(randomInt+65);
        }

        try {
            String rs=new String(randbyte, "UTF-8");
            //System.out.println(rs);
            return rs;
        } catch (Exception e) {
            //System.out.println("bad string");
            return "bad";
        }
    }


    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs4010";
    static final String USER = "cs4010";
    static final String PASS = "cs4010";


    public boolean tryLogin(String login_id, String login_pass) {
        log("running authenticator");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " SELECT loginpass FROM jcs436login WHERE loginid='" + login_id + ";";
            ResultSet rs = stmt.executeQuery(this_query);
            if (rs.next()) {
                log("the password is: " + rs.getString("loginpass"));
                if (rs.getString("loginpass").equals(login_pass)) {
                    this.userAuthenticated = true;
                    return true;
                }
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
            File outFile = new File("~/tomcat/sql_log");
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
