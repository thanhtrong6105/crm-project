package services;

import dto.UserTaskStatusDTO;
import entities.RolesEntity;
import entities.UsersEntity;
import repository.RoleRepository;
import repository.UsersRepository;
import utils.MD5;

import java.util.List;

public class UserService {
    private UsersRepository usersRepository = new UsersRepository();
    private RoleRepository repository = new RoleRepository();
    public List<UsersEntity> getAllUser(){
        List<UsersEntity> list = usersRepository.findAll();
        return list;
    }


    public boolean deleteUserById(int id) {
        int count = usersRepository.deleteById(id);

        return count > 0;
    }

    public List<RolesEntity> getAllRole(){
        return repository.findAll();
    }

    public boolean insertUser(String email, String password, String fullname, int roleId) {
        String passwordEncode = MD5.getMd5(password);
        return usersRepository.insert(email, passwordEncode, fullname, roleId) > 0;
    }

    public UsersEntity getUserById(int id) {
        return usersRepository.findById(id) != null ? usersRepository.findById(id) : null;
    }

    public UserTaskStatusDTO getUserTaskCompletionPercentageByUserId(int id) {
        return usersRepository.findUserTaskCompletionPercentageByUserId(id);
    }
}
