package mybeans;

import java.io.Serializable;

public class Pic implements Serializable{

    private String fileName;
    private String tag;
    private int sqlID;

    public Pic(){
        this.fileName = "";
        this.tag = "";
        this.sqlID = -1;
    }

    public String getTag(){ return tag; }

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
