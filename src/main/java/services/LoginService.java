package services;

import entities.UsersEntity;
import repository.UsersRepository;
import utils.MD5;

public class LoginService {
    private UsersRepository usersRepository;

    public UsersEntity login(String email, String password, String remember) {
        usersRepository = new UsersRepository();
        String paswordEnCode = MD5.getMd5(password);
        UsersEntity user = usersRepository.findByEmailAndPassword(email, paswordEnCode);
        if(user != null && checkLogin(email, paswordEnCode, user)) {
            return user;
        }
        return null;

    }

    private boolean checkLogin(String email, String password, UsersEntity user) {
        return user != null && user.getEmail().equals(email) && user.getPassword().equals(password);
    }
}
