import java.util.*;

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

}
