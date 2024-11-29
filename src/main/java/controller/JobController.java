package controller;

import entities.JobEntity;
import services.JobService;

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

@WebServlet(name="JobController", urlPatterns = {"/jobs", "/job-add", "/job-details"})
public class JobController extends HttpServlet {
    private JobService service = new JobService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/jobs")) {
            loadJobs(req, resp);
        }else if(path.equals("/job-add")) {
            addJobGet(req, resp);
        }else if(path.equals("/job-details")) {
            loadJobDetail(req, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/job-add")) {
            addJobPost(req, resp);
        }
    }

    private void loadJobs(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String id = req.getParameter("id");
        if(id != null) {
            service.deleteJobById(Integer.parseInt(id));
        }
        List<JobEntity> listJob = service.getAllJob();
        if(listJob != null) {
            req.setAttribute("listJob", listJob);
        }
        req.getRequestDispatcher("groupwork.jsp").forward(req, resp);
    }

    private void addJobPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String jobName = req.getParameter("jobName");
        String startDateStr = req.getParameter("startDate");
        String endDateStr = req.getParameter("endDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            Date startDate = new Date(dateFormat.parse(startDateStr).getTime());
            Date endDate = new Date(dateFormat.parse(endDateStr).getTime());
            String error = service.insert(jobName, startDate, endDate);
            if (error == null) {
                resp.sendRedirect(req.getContextPath() + "/jobs");
            } else {
                req.setAttribute("error", error);
                req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
            }
        } catch (ParseException e) {
            req.setAttribute("error", "Ngày không đúng định dạng yyyy-MM-dd. Vui lòng nhập lại.");
            req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
        }
    }

    private void addJobGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
    }

    private void loadJobDetail(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        req.getRequestDispatcher("groupwork-details.jsp").forward(req, resp);
    }
}
