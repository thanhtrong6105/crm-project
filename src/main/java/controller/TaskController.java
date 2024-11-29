package controller;

import entities.JobEntity;
import entities.TaskEntity;
import entities.UsersEntity;
import services.JobService;
import services.TaskService;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name="TaskController", urlPatterns = {"/task", "/task-add"})
public class TaskController extends HttpServlet {
    private TaskService service = new TaskService();
    private JobService jobService = new JobService();
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/task")) {
            loadTask(req, resp);
        }else if (path.equals("/task-add")) {
            addTaskGet(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/task-add")) {
            addTaskPost(req, resp);
        }
    }

    private void loadTask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("id");
        if(id != null) {
            service.deleteTaskById(Integer.parseInt(id));
        }
        List<TaskEntity> listTask = service.getAllTask();
        if(listTask != null) {
            req.setAttribute("listTask", listTask);
        }
        req.getRequestDispatcher("task.jsp").forward(req, resp);
    }

    private void addTaskGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        List<JobEntity> listJob = jobService.getAllJob();
        if(listJob != null) {
            req.setAttribute("listJob", listJob);
        }
        List<UsersEntity> listUser = userService.getAllUser();
        if(listUser != null) {
            req.setAttribute("listUser", listUser);
        }
        req.getRequestDispatcher("task-add.jsp").forward(req, resp);
    }

    private void addTaskPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        int jobId = 0;
        String jobParam = req.getParameter("job");
        if (jobParam != null && jobParam.matches("\\d+")) {
            jobId = Integer.parseInt(jobParam);
        }
        String taskName = req.getParameter("task");
        int userId = Integer.parseInt(req.getParameter("user") != null ? req.getParameter("user") : "0");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");

        String dateError = service.checkDateFormat(startDateStr, endDateStr);
        if (dateError != null) {
            req.setAttribute("error", dateError);
            req.getRequestDispatcher("task-add.jsp").forward(req, resp);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
            Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
            String errorMessage = service.addTask(jobId, taskName, userId, startDate, endDate);
            if (errorMessage == null) {
                resp.sendRedirect(req.getContextPath() + "/task");
            } else {
                req.setAttribute("errorMessage", errorMessage);
                req.getRequestDispatcher("task-add.jsp").forward(req, resp);
            }
        } catch (ParseException e) {
            req.setAttribute("error", "Lỗi không xác định trong quá trình xử lý ngày tháng.");
            req.getRequestDispatcher("task-add.jsp").forward(req, resp);
        }
    }
}
