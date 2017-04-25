import java.util.*;
import java.sql.*;
import java.io.*;


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
        java.util.Date now = new java.util.Date();
        return now.getTime() > this.expires;
    }

    public long setExpires(){
        java.util.Date now = new java.util.Date();
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


    public String tryLogin(String login_id, String login_pass) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " SELECT loginpass FROM jcs436login WHERE loginid='" + login_id + ";";
            ResultSet rs = stmt.executeQuery(this_query);
            if (rs.next()) {
                if (rs.getString("loginpass").equals(login_pass)) {
                    this.userAuthenticated = true;
                    return rs.getString("loginpass");
                }
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            return "sql error" + e.getMessage();
        }catch (ClassNotFoundException e) {
            return "class not found" + e.getMessage();
        }

        return "false";
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
