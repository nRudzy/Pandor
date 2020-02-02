<%@ page import="ucbl.ptm.projet.modele.User" %>
<%@ page import="java.util.List" %>
<%@ page import="ucbl.ptm.projet.modele.Rubrique" %>
<%@ page import="ucbl.ptm.projet.modele.Ban" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean id="pandor" scope="application" type="ucbl.ptm.projet.metier.Pandor"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="includes/head.jsp" %>

    <title>Interface administrateur | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="includes/header.jsp" %>

<div class="admin-container container mw-100 ">
    <div class="row mt-3 mb-3">
        <div class="col-4">
        </div>
        <div class="col-4">
            <div class="row justify-content-center">
                <h1>Gestion des utilisateurs</h1>
            </div>
        </div>
        <div class="col-4">
            <div class="row justify-content-end mr-5">
                <form action="<%= request.getContextPath() %>/AdministrationRubrique" method="get">
                    <input type="submit" class="btn btn-primary mt-2" value="Gestion des rubriques">
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-3 text-center">
            <div class="py-3 border border-primary rounded pandor-foreground w-75 ml-auto mr-auto">
                <div class="mb-3">
                    <h4> Selection utilisateur </h4>
                </div>
                <div class="list-container border-primary border-top border-bottom">
                    <% List<User> relevantUsers1; %>
                    <% if (request.getParameter("userFilter") == null) { %>
                    <% relevantUsers1 = pandor.getAllUser(); %>
                    <% } else { %>
                    <% relevantUsers1 = pandor.getAllUserWithNameContaining(request.getParameter("userFilter")); %>
                    <% } %>
                    <% for (User user : relevantUsers1) { %>
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="get"
                          class="userForm">
                        <input type="hidden" name="userId" value="<%=user.getId()%>">
                        <input class="btn btn-primary pseudoUser listedButton" type="submit"
                               value="<%= user.getPseudo() %>">
                        <% if (request.getParameter("userFilter") != null) { %>
                        <input type="hidden" name="userFilter" value="<%= request.getParameter("userFilter") %>">
                        <% } %>
                    </form>
                    <% } %>
                </div>
                <form class="my-3 justify-content-center" method="get"
                      action="<%= request.getContextPath() %>/AdministrationUtilisateur">
                    <label for="userFilter"></label>
                    <input type="text" id="userFilter" name="userFilter" placeholder="Rechercher pseudo"
                           maxlength="64" onkeyup="filterFunction()">
                    <% if (request.getParameter("userId") != null) { %>
                    <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                    <% } %>
                </form>
                <form class="my-3" method="get" action="<%= request.getContextPath() %>/AdministrationUtilisateur">
                    <input type="submit" class="btn btn-primary" value="Afficher tous les utilisateurs">
                    <input type="hidden" name="userFilter" value="">
                    <% if (request.getParameter("userId") != null) { %>
                    <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                    <% } %>
                </form>
            </div>
        </div>
        <div class="col mr-5">
            <% if (request.getParameter("userId") == null) { %>
            <div class="row pandor-foreground border-primary border rounded justify-content-center mt-5">
                Selectionnez un utilisateur
            </div>
            <% } else { %>
            <% User user = pandor.getUser(Integer.parseInt(request.getParameter("userId"))); %>
            <div class="row border-primary border-left border-right border-top pandor-foreground text-center">
                <div class="col-2">
                    <h5>Pseudo</h5> <%= user.getPseudo() %>
                </div>
                <div class="col-2">
                    <h5>Prénom - Nom</h5> <%= user.getPrenom() %> - <%= user.getNom() %>
                </div>
                <div class="col-2">
                    <h5>Numéro étudiant</h5> <%= user.getNumEtu() %>
                </div>
                <div class="col">
                    <h5>Email</h5> <%= user.getEmail() %>
                </div>
                <div class="col-3">
                    <h5>Statut</h5>
                    <%= user.getTitre() %>
                    <% if (user.getIsAdmin()) { %> - Admin
                    <% } else { %> - NonAdmin
                    <% } %>
                </div>
            </div>
            <div class="row border-bottom border-left border-right border-primary pandor-foreground">
                <div class="col my-3">
                    <h5 style="display: inline">Etat du ban : </h5>
                    <% if (!pandor.isBanned(user)) { %>
                    <%= user.getPseudo() %> n'est pas banni·e.
                    <% } else { %>
                    <% Ban ban = pandor.getBanByUser(user); %>
                    <%= user.getPseudo() %> est banni·e jusqu'au
                    <% DateTime dt = new DateTime(ban.getFin()); %>
                    <%= dt.getDayOfMonth() %>
                    <%= dt.monthOfYear().getAsText() %>
                    <%= dt.getYear() %> à
                    <%= formatTime(dt) %>
                    pour le motif suivant : <%= ban.getMotif() %>
                    <% } %>
                </div>
            </div>
            <div class="row border-bottom border-left border-right border-primary pandor-foreground">
                <div class="col border-primary text-center">
                    <div class="row justify-content-center">
                        <h3> Bannissement </h3>
                    </div>
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post">
                        <input type="hidden" name="action" value="ban">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <label for="permaBan">Bannir de manière permanente</label>
                        <input type="checkbox" name="permaBan" id="permaBan">
                        <br/>
                        <label for="nbrJours">Nombre de jours :</label>
                        <input type="number" id="nbrJours" name="nbrJours" required value="0">
                        <br/>
                        <label for="motif">Motif :</label>
                        <input type="text" id="motif" name="motif" required maxlength="2000">
                        <input type="submit" class="col btn-primary m-1 rounded"
                               value="<% if(!pandor.isBanned(user)) { %>Bannir<% } else { %>Augmenter la peine<% } %>">
                    </form>
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post">
                        <input type="hidden" name="action" value="unBan">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <input type="submit" class="col btn-primary m-1 rounded" value="Débannir">
                    </form>
                </div>
                <div class="col border-right border-left border-primary">
                    <div class="row justify-content-center">
                        <h3> Moderation des rubriques </h3>
                    </div>

                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post">
                        <div class="list-container border-primary border-top border-bottom" style="height: 130px">
                            <% for (Rubrique rubrique : pandor.getAllRubriques()) { %>
                            <div>
                                <input type="checkbox" value="<%= rubrique.getId() %>"
                                       name="<%= rubrique.getId() %>" id="<%= rubrique.getId() %>"
                                    <% if (user.getAllModeratedRubrique().contains(rubrique)) { %> checked <% } %> >
                                <label for="<%= rubrique.getId() %>"><%= rubrique.getNom() %>
                                </label>
                            </div>
                            <% } %>
                        </div>
                        <input type="submit" class="mr-3 btn btn-primary" value="Mettre à jour la modération">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <input type="hidden" name="action" value="updateMod">
                    </form>
                </div>
                <div class="col text-center">
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post" class="mt-1">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <input type="hidden" name="action" value="updateStatue">
                        <input type="text" id="statusUpdate" name="status" maxlength="64" required class="mb-1"
                               placeholder="Nouveau Statut">
                        <label for="statusUpdate" class="sr-only">Statut</label>
                        <input type="submit" class="btn btn-primary rounded" value="Mettre à jour le status">
                    </form>
                    <form action="<%= request.getContextPath() %>/Accueil" method="get">
                        <input type="hidden" name="idUser" value="<%= request.getParameter("userId") %>">
                        <input type="submit" class="row justify-content-center btn-primary m-1 rounded form-signin mt-2"
                               value="Voir les topics">
                    </form>
                    <% if (!user.getIsAdmin()) { %>
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <input type="hidden" name="action" value="setAdmin">
                        <input type="submit" class="row justify-content-center btn-primary m-1 rounded form-signin mt-2"
                               value="Passer administrateur">
                    </form>
                    <% } else { %>
                    <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="post">
                        <input type="hidden" name="userId" value="<%= request.getParameter("userId") %>">
                        <input type="hidden" name="action" value="rmAdmin">
                        <input type="submit" class="row justify-content-center btn-primary m-1 rounded form-signin mt-2"
                               value="Retirer privilèges administrateur">
                    </form>
                    <% } %>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>

<script>
    function filterFunction() {
        var input, filter, i, arrayForm, nameUser;
        input = document.getElementById("userFilter");
        filter = input.value.toUpperCase();
        arrayForm = document.getElementsByClassName("userForm");
        for (i = 0; i < arrayForm.length; i++) {
            nameUser = arrayForm[i].getElementsByClassName("pseudoUser")[0].value;
            if (nameUser.toUpperCase().indexOf(filter) > -1) {
                arrayForm[i].style.display = "";
            } else {
                arrayForm[i].style.display = "none";
            }
        }
    }
</script>

<%@ include file="includes/tail.jsp" %>
</body>
</html>
<%!
    private String formatTime(DateTime dt) {
        String ret = "";
        if (dt.getHourOfDay() < 10) {
            ret += "0";
        }
        ret += dt.getHourOfDay();
        ret += ":";
        if (dt.getMinuteOfHour() < 10) {
            ret += "0";
        }
        ret += dt.getMinuteOfHour();
        return ret;
    }
%>