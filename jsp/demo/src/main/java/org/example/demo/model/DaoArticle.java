package org.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class DaoArticle {

    static List<Article> articles = new ArrayList<>();

    public static void init(){
        articles.add(Article.builder().code(1).designation("Article 1").prix(10).build());
        articles.add(Article.builder().code(2).designation("Article 2").prix(20).build());
        articles.add(Article.builder().code(3).designation("Article 3").prix(30).build());
    }

    public static List<Article> getArticles(){
        return articles;
    }

    public static void addArticle(Article article){
        articles.add(article);
    }

    public static void deleteArticle(int code){
        articles.removeIf(article -> article.getCode() == code);
    }

    public static void updateArticle(Article article){
        for (Article a : articles){
            if (a.getCode() == article.getCode()){
                a.setDesignation(article.getDesignation());
                a.setPrix(article.getPrix());
            }
        }
    }





}
