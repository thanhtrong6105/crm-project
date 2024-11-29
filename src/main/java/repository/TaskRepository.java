package repository;

import config.MysqlConfig;
import entities.TaskEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TaskRepository {
    private UsersRepository usersRepository = new UsersRepository();
    public List<TaskEntity> findAll() {
        List<TaskEntity> tasks = new ArrayList<>();
        String query = "SELECT t.id, t.name AS task_name , j.name AS job_name, u.fullname as user_name, t.start_date, t.end_date, s.name as status "
                + "FROM users u "
                + "JOIN tasks t ON u.id = t.user_id "
                + "JOIN jobs j ON j.id = t.job_id "
                + "JOIN status s ON s.id = t.status_id";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                TaskEntity task = new TaskEntity();
                task.setId(result.getInt("t.id"));
                task.setTask_name(result.getString("task_name"));
                task.setJob_name(result.getString("job_name"));
                task.setUser_name(result.getString("user_name"));
                task.setStart_date(result.getDate("t.start_date"));
                task.setEnd_date(result.getDate("t.end_date"));
                task.setStatus_name(result.getString("status"));
                tasks.add(task);
            }

        } catch (Exception e) {
            System.out.println("findAll: " + e.getMessage());
        }

        return tasks;
    }


    public int deleteById(int id) {
        int rowDeleted = 0;
        String query = "DELETE FROM tasks t WHERE t.id = ?";
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

    public int insert(int jobId, String taskName, int userId, Date startDate, Date endDate) {
        int rowInserted = 0;
        int statusId = new Random().nextInt(1) + 3;

        // Kiểm tra sự tồn tại của userId trong bảng users
        if (usersRepository.findById(userId) == null) {
            System.out.println("User with ID " + userId + " does not exist.");
            return rowInserted;
        }

        String query = "INSERT INTO tasks(name, start_date, end_date, user_id, job_id, status_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, taskName);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            statement.setInt(4, userId);
            statement.setInt(5, jobId);
            statement.setInt(6, statusId);

            rowInserted = statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("insert : " + e.getMessage());
        }
        return rowInserted;
    }

    public List<TaskEntity> findByUserIdAndStatusName(int userId, String statusName) {
        List<TaskEntity> tasks = new ArrayList<>();
        String query = "SELECT t.name AS task_name, t.start_date, t.end_date "
                + "FROM users u "
                + "JOIN tasks t ON u.id = t.user_id "
                + "JOIN status s ON s.id = t.status_id "
                + "WHERE u.id = ? AND s.name = ?";
        try (Connection connection = MysqlConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, statusName);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                TaskEntity task = new TaskEntity();
                task.setTask_name(result.getString("task_name"));
                task.setStart_date(result.getDate("start_date"));
                task.setEnd_date(result.getDate("end_date"));
                tasks.add(task);
            }
        } catch (Exception e) {
            System.out.println("findByUserIdAndStatusName : " + e.getMessage());
        }
        return tasks;
    }

}
