package mybeans;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;
import static java.util.Arrays.asList;
import java.sql.*;


public class PicList implements Serializable{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/cs4010";
    static final String USER = "cs4010";
    static final String PASS = "cs4010";

    private ArrayList<Pic> pics = new ArrayList<>();

    public PicList() {
    }

    public String sendPics(){
        String html = "";
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                html = html +
                        "<div class='col-sm-6, col-md-4'> " +
                            "<div class='thumbnail'>" +
                                "<img class='img-responsive, img-rounded' src='pics/" + pics.get(i).getFileName() +"'>" +
                            "</div>" +
                        "</div>";
            }
        }
        return html;
    }

    public String tryGetList(String search) {
        Pic temp;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " SELECT * FROM jcs436Pics WHERE tag='" + search.trim() + "';";
            ResultSet rs = stmt.executeQuery(this_query);
            while (rs.next()) {
                temp = new Pic();
                temp.setFileName(rs.getString("filename"));
                temp.setTag(rs.getString("tag"));
                temp.setSqlID(Integer.parseInt(rs.getString("id")));
                this.pics.add(temp);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            return e.getMessage();
        }catch (ClassNotFoundException e) {
            return e.getMessage();
        }
        return "sql success";
    }

    public boolean tryAddPic(String fileName, String tag){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String this_query = " INSERT INTO jcs436Pics(filename, tag) VALUES('" + fileName + "' '" + tag + "');";
            ResultSet rs = stmt.executeQuery(this_query);

        }catch(Exception e){
            return false;
        }
        return true;
    }
    public int getPicQuantity(){
        if (pics == null){ return 0; }
        else {return pics.size(); }
    }
}
