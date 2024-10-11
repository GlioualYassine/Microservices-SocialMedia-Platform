package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.DaoArticle;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "listServlet", value = "/list-servlet")
public class ListServlet extends HttpServlet {

   // DaoArticle daoArticle = new DaoArticle();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        req.setAttribute("articles", DaoArticle.getArticles());
        //PrintWriter out=response.getWriter();
        req.getRequestDispatcher("/WEB-INF/listeArticles.jsp").forward(req,response);
    }

    @Override
    public void init() throws ServletException {
        DaoArticle.init();

    }
}
