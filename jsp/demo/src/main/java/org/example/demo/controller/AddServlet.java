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
import java.util.concurrent.atomic.AtomicBoolean;

@WebServlet(name = "AddServlet", value = "/addArticle")
public class AddServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Article> articles = DaoArticle.getArticles() ;
        req.setAttribute("articles", articles);
        req.getRequestDispatcher("/WEB-INF/Article.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");

        int code = Integer.parseInt(req.getParameter("code"));
        String designation = req.getParameter("designation");
        double prix = Double.parseDouble(req.getParameter("prix"));
        boolean ok = true;
        Article article = Article.builder().code(code).designation(designation).prix(prix).build();
        DaoArticle.getArticles().forEach(article1 -> {
            if(article1.equals(article)) {
                try {
                    resp.sendRedirect(req.getContextPath() + "/list-servlet");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        DaoArticle.addArticle(article);

        resp.sendRedirect(req.getContextPath() + "/list-servlet");

    }
}
