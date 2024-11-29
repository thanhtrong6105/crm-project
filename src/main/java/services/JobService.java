package services;

import entities.JobEntity;
import repository.JobRepository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class JobService {
    private JobRepository jobRepository = new JobRepository();

    public List<JobEntity> getAllJob(){
        return jobRepository.findAll();
    }

    public boolean deleteJobById(int id) {
        return jobRepository.deleteById(id) > 0;
    }

    public String insert(String roleName, Date startDate, Date endDate) {
        String error = checkFormat(roleName, startDate, endDate);
        if (error == null) {
            jobRepository.insert(roleName, startDate, endDate);
            return null;
        }
        return error;
    }

    private String checkFormat(String roleName, Date startDate, Date endDate) {
        if (roleName == null || roleName.trim().isEmpty()) {
            return "Tên vai trò không được để trống.";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        String start = dateFormat.format(startDate);
        String end = dateFormat.format(endDate);

        try {
            dateFormat.parse(start);
            dateFormat.parse(end);
        } catch (ParseException e) {
            return "Ngày không đúng định dạng yyyy-MM-dd.";
        }

        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc.";
        }

        return null;
    }
}
