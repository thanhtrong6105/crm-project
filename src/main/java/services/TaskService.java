package services;

import entities.TaskEntity;
import repository.TaskRepository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();

    public List<TaskEntity> getAllTask() {
        return taskRepository.findAll();
    }

    public boolean deleteTaskById(int id) {
        return taskRepository.deleteById(id) > 0;
    }

    public String addTask(int jobId, String taskName, int userId, Date startDate, Date endDate) {
        String errorMessage = checkFormat(taskName, startDate, endDate);
        if (errorMessage == null) {
            taskRepository.insert(jobId, taskName, userId, startDate, endDate);
            return null;
        }
        return errorMessage;
    }

    public String checkDateFormat(String startDateStr, String endDateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        try {
            java.util.Date startDateUtil = dateFormat.parse(startDateStr);
            java.util.Date endDateUtil = dateFormat.parse(endDateStr);

            Date startDate = new Date(startDateUtil.getTime());
            Date endDate = new Date(endDateUtil.getTime());

            if (startDate.after(endDate)) {
                return "Ngày bắt đầu phải trước ngày kết thúc.";
            }
        } catch (ParseException e) {
            return "Ngày không đúng định dạng yyyy-MM-dd.";
        }
        return null;
    }

    private String checkFormat(String taskName, Date startDate, Date endDate) {
        if (taskName == null || taskName.trim().isEmpty()) {
            return "Tên công việc không được để trống.";
        }
        if (startDate.after(endDate)) {
            return "Ngày bắt đầu phải trước ngày kết thúc.";
        }
        return null;
    }

    public List<TaskEntity> getTaskByUserIdAndStatusName(int userId, String statusName) {
        return taskRepository.findByUserIdAndStatusName(userId, statusName);
    }
}
