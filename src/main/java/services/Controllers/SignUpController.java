package services.Controllers;

import business.ServiceManagers.SignUpManager;
import business.exceptions.ServerInternalException;
import business.exceptions.UserAlreadyExistException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import services.DTO.Error.ErrorDTO;
import services.DTO.Signup.SignUpDTO;

@RestController
public class SignUpController {


    @RequestMapping(value = "/signup",method = RequestMethod.POST,produces = "application/json")
    ResponseEntity<Object> signUp(
            @RequestBody(required = true) JsonNode payload
    ){
        JsonNode emailJson = payload.get("email");
        JsonNode passwordJson = payload.get("password");
        JsonNode phoneJson = payload.get("phone");
        JsonNode nameJson = payload.get("name");
        JsonNode familyJson = payload.get("family");
        if(emailJson==null || phoneJson==null || passwordJson==null || nameJson==null || familyJson==null){
            return new ResponseEntity<>(new ErrorDTO("bad request",400), HttpStatus.BAD_REQUEST);
        }
        String email = emailJson.asText();
        String password = passwordJson.asText();
        String phone = phoneJson.asText();
        String name = nameJson.asText();
        String family = familyJson.asText();
        try {
            SignUpManager.getInstance().signUpNewUser(name, family, email, password, phone);
            return new ResponseEntity<>(new SignUpDTO("successful signUp.",200),HttpStatus.OK);
        }
        catch (ServerInternalException e){
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (UserAlreadyExistException e){
            return new ResponseEntity<>(new ErrorDTO("user already exist.",4001), HttpStatus.BAD_REQUEST);
        }
    }

}
