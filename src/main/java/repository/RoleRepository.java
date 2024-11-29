package repository;

import config.MysqlConfig;
import entities.RolesEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {
    public List<RolesEntity> findAll(){
        List<RolesEntity> listRole = new ArrayList<RolesEntity>();
        String query = "SELECT * FROM roles";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                RolesEntity role = new RolesEntity();
                role.setId(result.getInt("id"));
                role.setName(result.getString("name"));
                role.setDescription(result.getString("description"));

                listRole.add(role);
            }
        } catch (Exception e) {
            System.out.println("findAll: " + e.getMessage());
        }
        return listRole;
    }

    public int deleteById(int id) {
        int rowDeleted = 0;
        String query = "DELETE FROM roles r WHERE r.id = ?";
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            rowDeleted = statement.executeUpdate();


        }catch (Exception e) {
            System.out.println("deleteById : " + e.getMessage());
        }
        return rowDeleted;
    }

    public int insert(String roleName, String description) {
        int rowInserted = 0;
        String query = "INSERT INTO roles(name,description)VALUES(?,?)";
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, roleName);
            statement.setString(2, description);

            rowInserted = statement.executeUpdate();


        }catch (Exception e) {
            System.out.println("insert : " + e.getMessage());
        }
        return rowInserted;
    }
}
