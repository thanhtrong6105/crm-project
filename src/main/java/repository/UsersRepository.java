package repository;

import config.MysqlConfig;
import dto.UserTaskStatusDTO;
import entities.UsersEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    public UsersEntity findByEmailAndPassword(String email, String password) {
        String query = "SELECT * FROM users u WHERE u.email = ? AND u.password = ?";
        try(Connection connection = MysqlConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                UsersEntity user = new UsersEntity();
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                return user;

            }
        }catch (Exception e) {
            System.out.println("Query Error From Users : " + e.getMessage());
        }
        return null;
    }

    public List<UsersEntity> findAll() {
        List<UsersEntity> listUser = new ArrayList<>();
        String query = "SELECT u.id, u.fullname, u.email, r.name FROM users u JOIN roles r ON u.role_id = r.id";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                UsersEntity user = new UsersEntity();
                user.setEmail(result.getString("email"));
                user.setRoleName(result.getString("r.name"));
                user.setFullname(result.getString("fullname"));
                user.setId(result.getInt("id"));
                listUser.add(user);
            }
        } catch (Exception e) {
            System.out.println("findAll : " + e.getMessage());
        }
        return listUser;
    }

    public int deleteById(int id) {
        int rowDeleted = 0;
        String query = "DELETE FROM users u WHERE u.id = ?";
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            rowDeleted = statement.executeUpdate();



        } catch (Exception e) {
            System.out.println("deleteById : " + e.getMessage());
        }
        return rowDeleted;
    }

    public int insert(String email, String password, String fullname, int roleId) {
        String query = "INSERT INTO users(email,password,fullname,role_id)VALUES(?,?,?,?)";
        int rowInsert = 0;
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullname);
            statement.setInt(4, roleId);

            rowInsert = statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("insert : " + e.getMessage());
        }
        return rowInsert;
    }

    public UsersEntity findById(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = MysqlConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                UsersEntity user = new UsersEntity();
                user.setFullname(result.getString("fullname"));
                user.setEmail(result.getString("email"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("findById : " + e.getMessage());
        }
        return null;
    }

    public UserTaskStatusDTO findUserTaskCompletionPercentageByUserId(int userId) {
        String query = "SELECT " +
                "ROUND((COUNT(CASE WHEN t.status_id = 1 THEN 1 END) * 100.0) / COUNT(t.id), 2) AS ChuaThucHien, " +
                "ROUND((COUNT(CASE WHEN t.status_id = 2 THEN 1 END) * 100.0) / COUNT(t.id), 2) AS DangThucHien, " +
                "ROUND((COUNT(CASE WHEN t.status_id = 3 THEN 1 END) * 100.0) / COUNT(t.id), 2) AS HoanThanh " +
                "FROM crm_app.users u " +
                "LEFT JOIN crm_app.tasks t ON u.id = t.user_id " +
                "WHERE u.id = ? " +
                "GROUP BY u.id, u.fullname, u.email;";
        try(Connection connection = MysqlConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
            if(result.next()) {
                UserTaskStatusDTO userTaskStatusDTO = new UserTaskStatusDTO();
                userTaskStatusDTO.setNotStartedPercentage(result.getDouble("ChuaThucHien"));
                userTaskStatusDTO.setInProgressPercentage(result.getDouble("DangThucHien"));
                userTaskStatusDTO.setCompletedPercentage(result.getDouble("HoanThanh"));
                return userTaskStatusDTO;
            }
        }catch (Exception e) {
            System.out.println("findUserTaskCompletionPercentageByUserId : " + e.getMessage());
        }
        return null;
    }
}
