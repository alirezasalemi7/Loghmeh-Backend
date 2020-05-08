package business.ServiceManagers;

import business.exceptions.ServerInternalException;
import business.exceptions.UserDoesNotExistException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Clock;
import dataAccess.DAO.UserDAO;
import dataAccess.Repositories.UserRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import services.DTO.Login.LoginDTO;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

public class AuthenticationManager {

    private static AuthenticationManager instance;
    private ArrayList<String> excludedPath = new ArrayList<>();
    private final String AUTH_TOKEN = "Authorization";
    private final String AUTH_TOKEN_PREFIX = "Bearer ";
    private final String SECRET_KEY = "loghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghmeloghme";
    private final ArrayList<String> clientIds = new ArrayList<>();
    private SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public SecretKey getKey() {
        return key;
    }

    public ArrayList<String> getExcludedPath() {
        return excludedPath;
    }

    public String getAUTH_TOKEN() {
        return AUTH_TOKEN;
    }

    public String getAUTH_TOKEN_PREFIX() {
        return AUTH_TOKEN_PREFIX;
    }

    private AuthenticationManager(){
        this.excludedPath.add("/login");
        this.excludedPath.add("/signup");
        this.excludedPath.add("/login/google");
        clientIds.add("C08yGjgMlWx7l99fOAkM7eQE");
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
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("userId", user.getId())
                .signWith(key, SignatureAlgorithm.HS256)
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
        JsonNode result = GoogleVerifier.verify(token);
        if(result!=null){
            try {
                UserDAO user = UserRepository.getInstance().getUserByEmail(result.get("email").asText());
                return successFulLogin(user);
            }
            catch (UserDoesNotExistException e) {
                LoginDTO dto = new LoginDTO();
                dto.setStatus(4);
                dto.setDescription("should make account.");
                dto.setEmail(result.get("email").asText());
                dto.setFamily(result.get("family_name").asText());
                dto.setName(result.get("given_name").asText());
                return dto;
            }
        }
        else{
            return invalidGoogleAuth();
        }
    }
}
