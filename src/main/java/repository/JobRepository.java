package repository;

import config.MysqlConfig;
import entities.JobEntity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JobRepository {
    public List<JobEntity> findAll(){
        List<JobEntity> listJob = new ArrayList<JobEntity>();
        String query = "SELECT * FROM jobs";
        Connection connection = MysqlConfig.getConnection();
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                JobEntity job = new JobEntity();
                job.setId(result.getInt("id"));
                job.setName(result.getString("name"));
                job.setStart_date(result.getDate("start_date"));
                job.setEnd_date(result.getDate("end_date"));
                listJob.add(job);
            }

        }catch (Exception e) {
            System.out.println("findAll: " + e.getMessage());
        }
        return listJob;
    }

    public int deleteById(int id) {
        int rowDeleted = 0;
        String query = "DELETE FROM jobs j WHERE j.id = ?";
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

    public int insert(String jobName, Date startDate, Date endDate) {
        int rowInserted = 0;
        String query = "INSERT INTO jobs(name,start_date,end_date)VALUES(?,?,?)";
        Connection connection = MysqlConfig.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, jobName);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);

            rowInserted = statement.executeUpdate();

        }catch (Exception e) {
            System.out.println("insert : " + e.getMessage());
        }
        return rowInserted;
    }
}
