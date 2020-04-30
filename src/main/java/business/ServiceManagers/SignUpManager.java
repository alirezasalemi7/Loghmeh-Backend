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
        }catch (UserDoesNotExistException e){}
        UserDAO newUser = new UserDAO();
        newUser.setName(firstName);
        newUser.setFamily(lastName);
        newUser.setCredit(0.0);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setEmail(email);
        newUser.setLocation(new Location(0, 0));
        newUser.setId(RandomStringUtils.randomAlphanumeric(50));
        newUser.setPassword(password.hashCode());
        UserRepository.getInstance().AddUser(newUser);
        SignUpDTO signUpDTO = new SignUpDTO("successful signUp.",1);
        String jwt = AuthenticationManager.getInstance().authenticateUser(newUser.getEmail(), password).getJwt();
        signUpDTO.setJwt(jwt);
        return signUpDTO;
    }

}
