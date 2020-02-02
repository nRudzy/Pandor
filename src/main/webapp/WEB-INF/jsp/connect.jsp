<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/head.jsp" %>
    <title>Connexion | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>

<form class="form-signin text-center" method="post" action="<%= request.getContextPath()%>/Connect">
    <h1 class="h3 font-weight-normal">
        Connectez vous
    </h1>
    <%@ include file="includes/errorHandling.jsp" %>
    <label for="inputPseudo" class="sr-only">Pseudo</label>
    <input type="text" id="inputPseudo" class="form-control my-4" placeholder="Pseudo"
           required="" autofocus="" name="pseudo" minlength="4" maxlength="64">
    <label for="inputPassword" class="sr-only">Mot de passe</label>
    <input type="password" id="inputPassword" class="form-control mb-4" placeholder="Mot de passe"
           required="" name="password" minlength="8" maxlength="64">
    <button class="btn btn-lg btn-primary btn-block" type="submit">
        Se connecter
    </button>
</form>
<form class="form-signin" method="get" action="<%= request.getContextPath()%>/Inscription">
    <button class="btn btn-lg btn-warning btn-block" type="submit">
        Créer un compte
    </button>
</form>

<%@ include file="includes/tail.jsp" %>
</body>
</html>