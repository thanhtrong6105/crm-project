package controller;

import entities.UsersEntity;
import services.LoginService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    private LoginService loginService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = "";
        String password = "";

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie item : cookies) {
                if ("email".equals(item.getName())) {
                    email = item.getValue();
                }
                if ("password".equals(item.getName())) {
                    password = item.getValue();
                }
            }
        }

        req.setAttribute("email", email);
        req.setAttribute("password", password);
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loginService = new LoginService();
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        UsersEntity user = loginService.login(email, password, remember);
        if (user != null) {
            System.out.println("YES");
            Cookie ckUser = new Cookie("user", email);
            ckUser.setMaxAge(60*60*24);
            resp.addCookie(ckUser);
            if (remember != null) {
                Cookie ckEmail = new Cookie("email", email);
                Cookie ckPassword = new Cookie("password", password);
                ckEmail.setMaxAge(60*60*24);
                ckPassword.setMaxAge(60*60*24);
                resp.addCookie(ckEmail);
                resp.addCookie(ckPassword);
            }
            resp.sendRedirect(req.getContextPath() + "/home");
        } else {
            System.out.println("NO");
            req.setAttribute("errorMessage", "INVALID EMAIL OR PASSWORD !");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
