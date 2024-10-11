<%@ page import="org.example.demo.model.Article" %><%--
  Created by IntelliJ IDEA.
  User: yassi
  Date: 04/10/2024
  Time: 10:34
  To change this template use File | Settings | File Templates.
--%>

<%
    Article articleToUpdate = (Article) request.getAttribute("articleforupdate");
    System.out.println("Article: " + articleToUpdate);
    Integer code = articleToUpdate != null ? articleToUpdate.getCode() : null;
    String designation = articleToUpdate != null ? articleToUpdate.getDesignation() : null;
    double prix = articleToUpdate != null ? articleToUpdate.getPrix() : 0.0;
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gestion d'article</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            color: #333;
            padding: 20px;
        }
        h1 {
            text-align: center;
            color: #2c3e50;
        }
        form {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
            padding: 20px;
            width: 300px;
            margin: 20px auto;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #34495e;
        }
        input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            background-color: #27ae60;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #2ecc71;
        }
        .btn-delete {
            background-color: #e74c3c;
        }
    </style>
</head>
<body>

<h1><%= articleToUpdate != null ? "Modifier l'article" : "Ajouter un nouvel article" %></h1>

<% if (articleToUpdate != null) { %>
<form method="post" action="/demo_war_exploded/updateArticle">
    <label for="code">Code</label>
    <input type="text" id="code" name="code" value="<%= code %>" readonly>
    <label for="designation">Designation</label>
    <input type="text" id="designation" name="designation" value="<%= designation %>">
    <label for="prix">Prix</label>
    <input type="text" id="prix" name="prix" value="<%= prix %>">
    <input type="submit" value="Enregistrer">
</form>
<% } else { %>
<form method="post" action="/demo_war_exploded/addArticle">
    <label for="code">Code</label>
    <input type="text" id="code" name="code">
    <label for="designation">Designation</label>
    <input type="text" id="designation" name="designation">
    <label for="prix">Prix</label>
    <input type="text" id="prix" name="prix">
    <input type="submit" value="Enregistrer">
</form>
<% } %>

<%@ include file="listeArticles.jsp" %> <!-- Inclusion de la liste des articles -->

</body>
</html>
