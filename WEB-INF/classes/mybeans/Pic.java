package mybeans;


public class Pic implements java.io.Serializable{

    private String fileName;
    private String tag;
    private int sqlID;

    public String getTag(){
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSqlID() {
        return sqlID;
    }

    public void setSqlID(int sqlID) {
        this.sqlID = sqlID;
    }
}
