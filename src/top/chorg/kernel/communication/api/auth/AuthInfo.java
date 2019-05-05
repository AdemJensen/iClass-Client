package top.chorg.kernel.communication.api.auth;

public class AuthInfo {
    public String method;      // "Normal" or "Token"
    public String username;
    public String password;
    public String token;       // Only avail if method = "Token"

    public AuthInfo(String username, String password, boolean registerMode) {
        this.method = "Register";
        this.username = username;
        this.password = password;
    }

    public AuthInfo(String username, String password) {
        this.method = "Normal";
        this.username = username;
        this.password = password;
    }

    public AuthInfo(String token) {
        this.method = "Token";
        this.token = token;
    }
}
