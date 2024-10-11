<%@ page import="org.example.demo.model.Article" %>
<%@ page import="java.util.List" %><%-- 
  Created by IntelliJ IDEA. 
  User: yassi 
  Date: 04/10/2024 
  Time: 10:12 
  To change this template use File | Settings | File Templates.
--%>

<%
    List<Article> articles = (List<Article>) request.getAttribute("articles");
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List article</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
        }
        h1 {
            text-align: center;
            color: #2c3e50;
        }
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 12px;
            text-align: center;
        }
        th {
            background-color: #2980b9;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #ddd;
        }
        .btn {
            padding: 5px 10px;
            color: white;
            background-color: #27ae60;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            margin-right: 5px;
            cursor: pointer;
        }
        .btn-delete {
            background-color: #e74c3c;
        }
        .btn:hover {
            opacity: 0.8;
        }
        .btn-delete:hover {
            background-color: #c0392b;
        }
        .btn-add {
            padding: 10px 20px;
            color: white;
            background-color: #10b9;
            border: none;
            border-radius: 4px;
            text-decoration: none;
            margin-right: 5px;
            cursor: pointer;
        }
        .btn-add:hover {
            opacity: 0.8;
        }
        .container{
            display: flex;
            justify-content: flex-end;
            padding: 20px;
        }
    </style>
</head>
<body>
<h1>Liste des articles</h1>
<div class="container">
    <a href="addArticle" class="btn-add">Ajouter Article</a>
</div>
<table>
    <tr>
        <th>Id</th>
        <th>Titre</th>
        <th>Contenu</th>
        <th>Actions</th>
    </tr>
    <% for (Article article : articles) { %>
    <tr>
        <td><%= article.getCode() %></td>
        <td><%= article.getDesignation() %></td>
        <td><%= article.getPrix() %></td>
        <td>
            <a href="updateArticle?code=<%= article.getCode() %>" class="btn">Modifier</a>
            <a href="deleteArticle?code=<%= article.getCode() %>" class="btn btn-delete">Supprimer</a>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>
