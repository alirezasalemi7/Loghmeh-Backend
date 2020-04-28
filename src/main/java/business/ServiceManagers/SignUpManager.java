package business.ServiceManagers;

import business.Domain.Location;
import business.exceptions.ServerInternalException;
import business.exceptions.UserAlreadyExistException;
import business.exceptions.UserDoesNotExistException;
import dataAccess.DAO.UserDAO;
import dataAccess.Repositories.UserRepository;
import org.apache.commons.lang.RandomStringUtils;

public class SignUpManager {

    private static SignUpManager instance;

    public static SignUpManager getInstance(){
        if(instance==null){
            instance = new SignUpManager();
        }
        return instance;
    }

    private SignUpManager(){}

    public void signUpNewUser(String firstName,String lastName,String email,String password,String phoneNumber) throws ServerInternalException,UserAlreadyExistException {
        try {
            UserDAO user = UserRepository.getInstance().getUserByEmail(email);
            throw new UserAlreadyExistException();
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
    }

}
