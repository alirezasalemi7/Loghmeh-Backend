package business.ServiceManagers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class GoogleVerifier {
    public static JsonNode verify(String token){
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet("https://oauth2.googleapis.com/tokeninfo?id_token="+token);
        try {
            StringBuilder jsonBody = new StringBuilder();
            HttpResponse response = client.execute(getRequest);
            if(response.getStatusLine().getStatusCode() != 200){
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = reader.readLine()) != null){
                jsonBody.append(line);
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(jsonBody.toString());
                boolean verified = root.get("email_verified").asBoolean();
                if(verified==false){
                    return null;
                }
                long exp = root.get("exp").asLong();
                if(exp*1000 < (new Date()).getTime()){
                    System.err.println(exp);
                    System.err.println((new Date()).getTime());
                    return null;
                }
                return root;
            }
            catch (IOException e){
                return null;
            }
        }
        catch (ClientProtocolException e){
            return null;
        }
        catch (IOException e){
            return null;
        }
    }
}
