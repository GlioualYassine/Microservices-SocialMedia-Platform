package org.example.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.Article;
import org.example.demo.model.DaoArticle;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UpdateServlet", value = "/updateArticle")
public class UpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("code"));
        Article article = DaoArticle.getArticles().stream().filter(a -> a.getCode() == code).findFirst().get();
        List<Article> articles = DaoArticle.getArticles() ;
        System.out.println(article);
        req.setAttribute("articleforupdate", article);
        req.setAttribute("articles", articles);
        System.out.println("test8");
        req.getRequestDispatcher("/WEB-INF/Article.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("code"));
        String designation = req.getParameter("designation");
        double prix = Double.parseDouble(req.getParameter("prix"));

        DaoArticle.updateArticle(Article.builder().code(code).designation(designation).prix(prix).build());
        System.out.println("inside do post update");
        resp.sendRedirect(req.getContextPath() + "/list-servlet");
    }
}
