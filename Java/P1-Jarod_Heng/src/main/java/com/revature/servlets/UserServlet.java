package com.revature.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getHeader("username");
        String password = req.getHeader("password");
        try {
            User user = AuthService.Login(username,password);
            resp.getWriter().print(user.toString());
            resp.setStatus(200);
        } catch (RuntimeException e) {
            resp.setStatus(401);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // register
        String username = req.getHeader("username");
        String password, firstname, lastname, email, phone;

        password = req.getHeader("password");
        firstname = req.getHeader("firstname");
        lastname = req.getHeader("lastname");
        email = req.getHeader("email");
        phone = req.getHeader("phone");

        User userToRegister = new User(0, username, password, Role.EMPLOYEE, firstname, lastname, email, phone);

        try {
            User newUser = AuthService.Register(userToRegister);
            resp.setStatus(201);
            resp.getWriter().print(newUser);
        } catch (RuntimeException e) {
            e.printStackTrace();
            resp.setStatus(401);
            throw new ServletException(e.getMessage());
        }


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setStatus(200);
    }
}
