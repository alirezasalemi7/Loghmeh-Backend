package services.Controllers;

import business.ServiceManagers.AuthenticationManager;
import business.exceptions.ServerInternalException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import services.DTO.Error.ErrorDTO;

@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<Object> loginWithUserPass(
            @RequestBody(required = true) JsonNode payload
    ) {
        JsonNode email = payload.get("email");
        JsonNode password = payload.get("password");
        if (email == null || password == null)
            return new ResponseEntity<>(new ErrorDTO("username or password is not sent", 400), HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(AuthenticationManager.getInstance().authenticateUser(email.asText(), password.asText()), HttpStatus.OK);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("an internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/login/google", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<Object> loginWithGoogle(
            @RequestBody(required = true) JsonNode payload
    ) {
        JsonNode token = payload.get("token");
        if (token == null)
            return new ResponseEntity<>(new ErrorDTO("token is not sent", 400), HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(AuthenticationManager.getInstance().googleAuthenticationVerifier(token.asText()), HttpStatus.OK);
        } catch (ServerInternalException e) {
            return new ResponseEntity<>(new ErrorDTO("an internal server error occurred", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
