<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <%@ include file="includes/head.jsp" %>

    <title>Création d'une rubrique | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>

<div class="px-3 py-3 pt-md-5 pd-md-4 mx-auto text-center">
    <h1>Création d'une rubrique</h1>
</div>
<div class="container">
    <form class="form-group text-center" method="post" action="<%= request.getContextPath()%>/CreateRubrique">
        <div class="row">
            <label for="inputTitre" class="sr-only">Titre</label>
            <input class="form-control" type="text" id="inputTitre" placeholder="Titre de la rubrique"
                   required="" name="inputTitre" maxlength="256">
        </div>
        <div class="row form-group">
            <label for="inputPresentation" class="sr-only">Description</label>
            <textarea class="form-control" rows="7" id="inputPresentation" placeholder="Présentation de la rubrique"
                      required="" name="inputPresentation"></textarea>
        </div>
        <div class="row form-signin">
            <button class="btn btn-lg btn-primary btn-block" type="submit">Créer la rubrique</button>
        </div>
    </form>
</div>

<%@ include file="includes/tail.jsp" %>
</body>
</html>
