package mybeans;

import

public class Pic {

    private String fileName;
    private int sqlID;

    public String getTag() implements java.io.Serializable{
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private String tag;

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
