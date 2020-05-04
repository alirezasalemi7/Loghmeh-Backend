package business.ServiceManagers;

import business.exceptions.ServerInternalException;
import business.exceptions.UserDoesNotExistException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import dataAccess.DAO.UserDAO;
import dataAccess.Repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import services.DTO.Login.LoginDTO;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AuthenticationManager {

    private static AuthenticationManager instance;
    private ArrayList<String> excludedPath = new ArrayList<>();
    private final String AUTH_TOKEN = "Authorization";
    private final String AUTH_TOKEN_PREFIX = "Bearer ";
    private final String SECRET_KEY = "loghme";
    private final ArrayList<String> clientIds = new ArrayList<>();
    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

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
        clientIds.add("953204279771-ghbm16m0rob8432b7r9q33ikfrjgvtn0.apps.googleusercontent.com");
    }

    public static AuthenticationManager getInstance(){
        if(instance==null){
            instance = new AuthenticationManager();
        }
        return instance;
    }

    private String makeJwt(UserDAO user){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,1);
        String jwt = Jwts.builder()
                .setExpiration(calendar.getTime())
                .setIssuedAt(new Date())
                .setIssuer("loghme/login")
                .claim("userId", user.getId())
                .signWith(key)
                .compact();
        return jwt;
    }

    LoginDTO successFulLogin(UserDAO user){
        String jwt = makeJwt(user);
        LoginDTO dto = new LoginDTO();
        dto.setStatus(1);
        dto.setDescription("successful login.");
        dto.setJwt(jwt);
        return dto;
    }

    private LoginDTO invalidPasswordOrUsername(){
        LoginDTO dto = new LoginDTO();
        dto.setStatus(2);
        dto.setDescription("invalid username or password");
        return dto;
    }

    private LoginDTO invalidGoogleAuth(){
        LoginDTO dto = new LoginDTO();
        dto.setStatus(3);
        dto.setDescription("invalid token");
        return dto;
    }


    public LoginDTO authenticateUser(String email,String password) throws ServerInternalException{
        try {
            UserDAO user = UserRepository.getInstance().getUserByEmail(email);
            return user.getPassword()==password.hashCode() ? successFulLogin(user) : invalidPasswordOrUsername();
        }
        catch (UserDoesNotExistException e){
            return invalidPasswordOrUsername();
        }
    }

    public LoginDTO googleAuthenticationVerifier(String token) throws ServerInternalException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory()).setAudience(clientIds).build();
        try {
            GoogleIdToken googleIdToken = verifier.verify(token);
            if(googleIdToken!=null){
                try {
                    UserDAO user = UserRepository.getInstance().getUserByEmail(googleIdToken.getPayload().getEmail());
                    return successFulLogin(user);
                }
                catch (UserDoesNotExistException e){
                    GoogleIdToken.Payload payload = googleIdToken.getPayload();
                    LoginDTO dto = new LoginDTO();
                    dto.setStatus(3);
                    dto.setDescription("should make account.");
                    dto.setEmail(payload.getEmail());
                    dto.setFamily((String) payload.get("family_name"));
                    dto.setName((String) payload.get("name"));
                    return dto;
                }
            }
            else{
                return invalidGoogleAuth();
            }
        }
        catch (GeneralSecurityException | IOException e){
            return invalidGoogleAuth();
        }
    }
}
