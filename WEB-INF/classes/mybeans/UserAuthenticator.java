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

    public UserAuthenticator(){}

    public static boolean tryLogin(String login_id, String login_pass){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String this_query=" SELECT loginpass FROM jcs436login WHERE loginid='" + login_id + ";";
            ResultSet rs = stmt.executeQuery(this_query);
            if (rs.next()) {
                return rs.getString("loginpass").equals(login_pass);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {return false;}

        return false;
    }
/*
    public String getAll(){
        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
            String this_query=" SELECT * from java_store WHERE  file_name='"+file_name+"' AND version_id="+version_id+";"; 
            ResultSet rs = stmt.executeQuery(this_query);
            while (rs.next()) {
                java_store_id=rs.getInt("java_store_id");  
                file_name=rs.getString("file_name");  
                version_id=rs.getInt("version_id");    
                save_time = rs.getString("save_time") ;     
                this_version =Bytes_Hex.HexString2String(rs.getString("this_version"));   
                notes=Bytes_Hex.HexString2String(rs.getString("notes"));           
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {return "not found";

        }
        return "";
    }
    public void setNotes(String n,String f,int v){
        notes=n;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement stmt = conn.createStatement();
         //     System.out.println("here: "+Bytes_Hex.String2HexString(n));
            String this_query="UPDATE java_store SET notes='"+Bytes_Hex.String2HexString(n)+"' WHERE  file_name='"+f+"' AND version_id="+v+";";  
       //     System.out.println(this_query);
            stmt.executeUpdate(this_query);
            stmt.close();
            conn.close();
        } catch (Exception e) {

        }
        return ;
    }*/
}
/*


*/



