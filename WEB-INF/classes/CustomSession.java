import java.util.*

public class CustomSession {
    private    String ip;
    private    long expires;
    private    String name;
    private    String password;
    private    String ID;

    public CustomSession(String ip, String name, String pw){
        this.ip = ip;
        this.name = name;
        this.password = pw;
        this.expires = setExpires();
        this.ID = getRandomString();
    }

    public boolean isExpired(){
        Date now = new Date();
        return now.getTime() > this.expires;
    }

    public boolean isAuthentic(String name, String pw){
        if (name.equals(this.name) && pw.equals(this.password)){return true;}
        return false;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
