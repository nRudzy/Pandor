<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/head.jsp" %>

    <title>Créer un compte | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>

<%@ include file="includes/subscribeErgonomics1.jsp" %>

<form class="form-signin text-center" method="post" action="<%= request.getContextPath()%>/Inscription">
    <h1 class="h3 my-3 font-weight-normal">Inscrivez vous</h1>
    <%@ include file="includes/errorHandling.jsp" %>

    <label for="inputNom" class="sr-only">Nom</label>
    <input type="text" id="inputNom" class="form-control mb-4" placeholder="Nom" required="" autofocus="" maxlength="64"
           name="nom" value=<%=enteredNom%>>

    <label for="inputPrenom" class="sr-only">Prénom</label>
    <input type="text" id="inputPrenom" class="form-control mb-4" placeholder="Prénom" required="" autofocus=""
           maxlength="64" name="prenom" value=<%=enteredPrenom%>>

    <label for="inputNumEtu" class="sr-only">Numéro étudiant</label>
    <input type="text" id="inputNumEtu" class="form-control mb-4" placeholder="Numéro étudiant" required="" autofocus=""
           maxlength="8" minlength="8" name="numEtu" value=<%=enteredNumEtu%>>

    <label for="inputEmail" class="sr-only">Email</label>
    <input type="email" id="inputEmail" class="form-control mb-4" placeholder="Email" required="" autofocus=""
           name="email" value=<%=enteredEmail%>>

    <label for="inputPseudo" class="sr-only">Pseudo</label>
    <input type="text" id="inputPseudo" class="form-control mb-4" placeholder="Pseudo" required="" autofocus=""
           name="pseudo" minlength="4" maxlength="64" value=<%=enteredPseudo%>>

    <label for="inputPassword" class="sr-only">Mot de passe</label>
    <input type="password" id="inputPassword" class="form-control mb-4" placeholder="Mot de passe" required=""
           name="password" minlength="8" maxlength="64">

    <label for="reTypePassword" class="sr-only">Confirmer mot de passe</label>
    <input type="password" id="reTypePassword" class="form-control mb-4" placeholder="Confirmer mot de passe"
           required=""
           name="secondPassword" minlength="8" maxlength="64">

    <button class="btn btn-lg btn-primary btn-block" type="submit">Créer le compte</button>
</form>

<form class="form-signin" method="get" action="<%= request.getContextPath()%>/Connexion">
    <button class="btn btn-lg btn-warning btn-block" type="submit">J'ai déjà un compte</button>
</form>

<%@ include file="includes/subscribeErgonomics2.jsp" %>

<%@ include file="includes/tail.jsp" %>
</body>
</html>