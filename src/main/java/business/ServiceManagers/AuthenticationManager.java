package business.ServiceManagers;

import java.util.ArrayList;

public class AuthenticationManager {

    private static AuthenticationManager instance;
    private ArrayList<String> excludedPath = new ArrayList<>();
    private final String AUTH_TOKEN = "Authorization";
    private final String AUTH_TOKEN_PREFIX = "Bearer ";
    private final String SECRET_KEY = "loghme";

    public ArrayList<String> getExcludedPath() {
        return excludedPath;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public String getAUTH_TOKEN_PREFIX() {
        return AUTH_TOKEN_PREFIX;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }

    private AuthenticationManager(){
        this.excludedPath.add("/login");
        this.excludedPath.add("/signup");
    }

    public static AuthenticationManager getInstance(){
        if(instance==null){
            instance = new AuthenticationManager();
        }
        return instance;
    }

    public String authenticateUser(String email,String password){
        return null;
    }
}
