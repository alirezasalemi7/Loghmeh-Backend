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
    ) {
        JsonNode emailJson = payload.get("email");
        JsonNode passwordJson = payload.get("password");
        JsonNode phoneJson = payload.get("phone");
        JsonNode nameJson = payload.get("name");
        JsonNode familyJson = payload.get("family");
        if (emailJson == null || phoneJson == null || passwordJson == null || nameJson == null || familyJson == null)
            return new ResponseEntity<>(new ErrorDTO("bad request",4001), HttpStatus.BAD_REQUEST);
        try {
            SignUpDTO result = SignUpManager.getInstance().signUpNewUser(nameJson.asText(), familyJson.asText(), emailJson.asText(), passwordJson.asText(), phoneJson.asText());
            if (result.getStatus() == 0)
                return new ResponseEntity<>(new ErrorDTO("user already signed up",4002), HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("server error",500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
