<%@page import="ucbl.ptm.projet.modele.Rubrique" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="pandor" scope="application" type="ucbl.ptm.projet.metier.Pandor"/>

<!DOCTYPE html>
<html lang="fr">
<head>
    <%@ include file="includes/head.jsp" %>

    <title>Création de Topic | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>

<div class="px-3 py-3 pt-md-5 pd-md-4 mx-auto text-center">
    <h1>Posez une question</h1>
</div>

<div class="container">
    <%@ include file="includes/errorHandling.jsp" %>
    <form class="form-group text-center" method="post" action="CreateTopic">
        <div class="row form-inline justify-content-left">
            <select class=form-control id="selectRubrique" name="selectRubrique">
                <% for (Rubrique rubrique : pandor.getAllRubriques()) { %>
                <% if (request.getParameter("rubrique") != null && request.getParameter("rubrique").equals(rubrique.getNom())) { %>
                <option value="<%=rubrique.getNom()%>" selected><%=rubrique.getNom()%>
                </option>
                <% } else { %>
                <option value="<%=rubrique.getNom()%>"><%=rubrique.getNom()%>
                </option>
                <% } %>
                <% } %>
            </select>
            <label class="sr-only" for="selectRubrique">SelectRubrique</label>
        </div>
        <div class="row">
            <label for="inputTitle" class="sr-only">Titre</label>
            <% if (request.getParameter("titre") != null) { %>
            <input class="form-control" type="text" id="inputTitle" placeholder="Titre de votre question"
                   required="" name="inputTitle" maxlength="256" value="<%= request.getParameter("titre")%>">
            <% } else { %>
            <input class="form-control" type="text" id="inputTitle" placeholder="Titre de votre question"
                   required="" name="inputTitle" maxlength="256">
            <% } %>
        </div>
        <div class="row form-group">
            <label for="inputDescription" class="sr-only">Description</label>
            <textarea class="form-control" rows="7" id="inputDescription" placeholder="Détaillez votre question ici"
                      required="" name="inputDescription"></textarea>
        </div>
        <div class="row">
            <label for="inputTags" class="sr-only">Tags</label>
            <% if (request.getParameter("tags") != null) { %>
            <input class="form-control" type="text" id="inputTags"
                   placeholder="Listes des tags en rapport avec votre question (séparés par une virgule)"
                   required="" name="inputTags" maxlength="256" value="<%= request.getParameter("tags")%>">
            <% } else { %>
            <input class="form-control" type="text" id="inputTags"
                   placeholder="Listes des tags en rapport avec votre question (séparés par une virgule)"
                   required="" name="inputTags" maxlength="256">
            <% } %>
        </div>
        <div class="checkbox">
            <label>
                <input type="checkbox" name="anonPost" value="anonyme"> Poster en anonyme
            </label>
        </div>
        <div class="row form-signin">
            <button class="btn btn-lg btn-primary btn-block" type="submit">Poster la question</button>
        </div>
    </form>
</div>

<%@ include file="includes/tail.jsp" %>
</body>
</html>
