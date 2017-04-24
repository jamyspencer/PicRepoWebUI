package mybeans;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.sql.*;

public class picList {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs4010";
    static final String USER = "cs4010";
    static final String PASS = "cs4010";

    private ArrayList<Pic> pics;

    public picList() {
    }

    public boolean tryGetList(String search) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " SELECT * FROM jcs436pics WHERE tag='" + search.trim() + ";";
            ResultSet rs = stmt.executeQuery(this_query);
            while (rs.next()) {
                Pic temp = new Pic();
                rs.getString("filename").equals(temp.setFileName);
                rs.getString("tag").equals(temp.setTag);
                rs.getString("id").equals(temp.setSqlID);
                pics.add(temp);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean tryAddPic(String fileName, String tag){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " INSERT INTO jcs436pics(filename, tag) VALUES('" + fileName "' '" + tag + "');";
            ResultSet rs = stmt.executeQuery(this_query);

        }catch(exception e){
            return false;
        }
        return true;
    }
}