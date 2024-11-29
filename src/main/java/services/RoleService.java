package services;

import entities.RolesEntity;
import repository.RoleRepository;

import java.util.List;

public class RoleService {
    private RoleRepository roleRepository = new RoleRepository();

    public List<RolesEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    public boolean deleteUserById(int id) {
        int count = roleRepository.deleteById(id);

        return count > 0;
    }

    public boolean insertRole(String roleName, String description) {
        return roleRepository.insert(roleName, description) > 0;
    }
}
