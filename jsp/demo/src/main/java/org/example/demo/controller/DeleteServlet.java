package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.DaoArticle;

import java.io.IOException;

@WebServlet(name = "DeleteServlet", value = "/deleteArticle")
public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("code"));
        System.out.println("code = " + code);
        DaoArticle.deleteArticle(code);
        resp.sendRedirect(req.getContextPath() + "/list-servlet");
    }
}
