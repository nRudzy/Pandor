<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/head.jsp" %>
    <title>Mon Compte | Pandor</title>
</head>

<script>
    function openPseudo() {
        var x = document.getElementById("divPseudo");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }

    function openMdp() {
        var x = document.getElementById("divMdp");
        if (x.style.display === "none") {
            x.style.display = "block";
        } else {
            x.style.display = "none";
        }
    }
</script>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>
<% User user = (User) request.getAttribute("user"); %>
<div class="py-3 border border-primary rounded pandor-foreground w-75 ml-auto mr-auto mt-3 gshape">
    <div class="mb-3">
        <h4> Informations sur le compte de : <%=user.getPseudo()%>
        </h4>
    </div>

    <hr class="hrp">

    <p>
        <strong>Pseudo</strong> : <%=user.getPseudo()%>
        <button class="btn-sm btn-primary" onclick="openPseudo()"><em class="fa fa-edit"></em></button>
    </p>

    <div id="divPseudo" style="display: none;">
        <form class="my-3 justify-content-center" method="post" action="<%= request.getContextPath()%>/MonCompte">
            <label for="changePseudo"></label>
            <input type="text" id="changePseudo" name="changePseudo" placeholder="Nouveau pseudo" maxlength="64"
                   minlength="4">
            <input type="submit" class="btn btn-primary" value="Modifier"/>
        </form>
    </div>

    <p><strong>Nom</strong> : <%=user.getNom()%>
    </p>

    <p><strong>Prénom</strong> : <%=user.getPrenom()%>
    </p>

    <p><strong>Email</strong> : <%=user.getEmail()%>
    </p>

    <p><strong>Numéro</strong> : <%=user.getNumEtu()%>
    </p>

    <p><strong>Statut</strong> : <%=user.getTitre()%>
    </p>

    <p>
        <button class="btn btn-primary" onclick="openMdp()">Modifier le mot de passe</button>
        <%@ include file="includes/errorHandling.jsp" %>
    <div id="divMdp" style="display: none;">
        <form class="my-3 justify-content-center" method="post" action="<%= request.getContextPath()%>/MonCompte">
            <label for="oldMdp"></label>
            <input type="password" id="oldMdp" name="oldMdp" placeholder="Ancien mot de passe" minlength="8"
                   maxlength="64" required>
            <label for="newMdp"></label>
            <input type="password" id="newMdp" name="newMdp" placeholder="Nouveau mot de passe" minlength="8"
                   maxlength="64" required>
            <label for="confirmMdp"></label>
            <input type="password" id="confirmMdp" name="confirmMdp" placeholder="Confirmer le mot de passe"
                   minlength="8" maxlength="64" required>

            <input type="submit" class="btn btn-primary" value="Modifier">
        </form>
    </div>
    </p>


</div>
<%@ include file="includes/tail.jsp" %>
</body>
</html>