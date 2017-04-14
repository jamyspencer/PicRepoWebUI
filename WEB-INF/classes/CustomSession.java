import java.util.*

public class CustomSession {
    private    String ip;
    private    long expires;
    private    String name;
    private    String password;
    private    String securityString;

    public CustomSession(int begin){
        task=begin;
    }
    public CustomSession(String ip, long expires, String name, String pw, String the_ss){
        this.ip = ip;
        this.expires = expires;
        this.name = name;
        this.password = pw;
        this.securityString = the_ss;
    }
    boolean isExpired(long now){
        return now > this.expires;
    }

    boolean isAuthentic(String name, String pw){
        if (name.equals(this.name) && pw.equals(this.password)){return true;}
        return false;
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

    public String getSecurityString() {
        return securityString;
    }

    public void setSecurityString(String securityString) {
        this.securityString = securityString;
    }

}
