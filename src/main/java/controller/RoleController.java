package controller;

import entities.RolesEntity;
import services.RoleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="RoleController", urlPatterns = {"/role", "/role-add"})
public class RoleController extends HttpServlet {
    private RoleService service = new RoleService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/role")) {
            loadRole(req, resp);
        }else if(path.equals("/role-add")) {
            addRole(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/role-add")) {
            addRolePost(req, resp);
        }
    }

    private void loadRole(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String id = req.getParameter("id");
        if(id != null) {
            service.deleteUserById(Integer.parseInt(id));
        }

        List<RolesEntity> listRole = new ArrayList<RolesEntity>();
        listRole = service.getAllRoles();
        req.setAttribute("listRole", listRole);
        req.getRequestDispatcher("role-table.jsp").forward(req, resp);
    }

    private void addRole(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<RolesEntity> listRole = new ArrayList<RolesEntity>();
        listRole = service.getAllRoles();
        req.setAttribute("listRole", listRole);
        req.getRequestDispatcher("role-add.jsp").forward(req, resp);
    }

    private void addRolePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String roleName = req.getParameter("name");
        String description = req.getParameter("description");
        if(roleName != null && description != null && !roleName.equals("") && !description.equals("")) {
            service.insertRole(roleName, description);
            resp.sendRedirect(req.getContextPath() + "/role");
        }else {
            req.getRequestDispatcher("role-add.jsp").forward(req, resp);
        }
    }
}
