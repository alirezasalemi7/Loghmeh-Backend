package business.ServiceManagers;

import business.Domain.Location;
import business.exceptions.ServerInternalException;
import business.exceptions.UserAlreadyExistException;
import business.exceptions.UserDoesNotExistException;
import dataAccess.DAO.UserDAO;
import dataAccess.Repositories.UserRepository;
import org.apache.commons.lang.RandomStringUtils;
import services.DTO.Signup.SignUpDTO;

public class SignUpManager {

    private static SignUpManager instance;

    public static SignUpManager getInstance(){
        if(instance==null){
            instance = new SignUpManager();
        }
        return instance;
    }

    private SignUpManager(){}

    public SignUpDTO signUpNewUser(String firstName, String lastName, String email, String password, String phoneNumber) throws ServerInternalException {
        try {
            UserDAO user = UserRepository.getInstance().getUserByEmail(email);
            SignUpDTO signUpDTO = new SignUpDTO("user already exists.",1);
            return signUpDTO;
        } catch (UserDoesNotExistException e) {
            UserRepository.getInstance().AddUser(new UserDAO(firstName, lastName, phoneNumber, email, 0.0, RandomStringUtils.randomAlphanumeric(50), new Location(0, 0), password.hashCode()));
            SignUpDTO signUpDTO = new SignUpDTO("successful signUp.", 1);
            String jwt = AuthenticationManager.getInstance().authenticateUser(email, password).getJwt();
            signUpDTO.setJwt(jwt);
            return signUpDTO;
        }
    }

}
