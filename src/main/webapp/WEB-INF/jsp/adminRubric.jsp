<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.io.UnsupportedEncodingException"%>
<%@page import="java.net.URLEncoder"%>
<%@ page import="ucbl.ptm.projet.modele.Rubrique" %>
<%@ page import="java.util.List" %>
<%@ page import="ucbl.ptm.projet.modele.Topic" %>
<%@ page import="ucbl.ptm.projet.modele.User" %>
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
                <h1>Gestion des rubriques</h1>
            </div>
        </div>
        <div class="col-4">
            <div class="row justify-content-end mr-5">
                <form action="<%= request.getContextPath() %>/AdministrationUtilisateur" method="get">
                    <input type="submit" class="btn btn-primary mt-2" value="Gestion des utilisateurs">
                </form>
            </div>
        </div>
        <form action="AdministrationUtilisateur" method="get">
        </form>
    </div>
    <div class="row">
        <div class="col-3 text-center">
            <div class="py-3 border border-primary rounded pandor-foreground w-75 ml-auto mr-auto">
                <div class="mb-3">
                    <h4> Selection rubrique </h4>
                </div>
                <div class="list-container border-primary border-top border-bottom">
                    <% List<Rubrique> relevantRubriques; %>
                    <% if (request.getParameter("rubriqueFilter") == null) { %>
                    <% relevantRubriques = pandor.getAllRubriques(); %>
                    <% } else { %>
                    <% relevantRubriques = pandor.getAllRubriqueWithNameContaining(request.getParameter("rubriqueFilter")); %>
                    <% } %>
                    <% for (Rubrique rubrique : relevantRubriques) { %>
                    <form action="<%= request.getContextPath()%>/AdministrationRubrique" method="get"
                          class="rubriqueForm">
                        <input type="hidden" name="rubriqueId" value="<%=rubrique.getId()%>">
                        <input class="btn btn-primary nameRubrique listedButton" type="submit"
                               value="<%= rubrique.getNom() %>">
                        <% if (request.getParameter("rubriqueFilter") != null) { %>
                        <input type="hidden" name="rubriqueFilter"
                               value="<%= request.getParameter("rubriqueFilter") %>">
                        <% } %>
                    </form>
                    <% } %>
                </div>

                <form class="my-3 justify-content-center" method="get"
                      action="<%= request.getContextPath()%>/AdministrationRubrique">
                    <label for="rubriqueFilter"></label>
                    <input type="text" id="rubriqueFilter" name="rubriqueFilter" placeholder="rechercher rubrique"
                           maxlength="64" onkeyup="filterFunction()">
                    <% if (request.getParameter("rubriqueId") != null) { %>
                    <input type="hidden" name="rubriqueId" value="<%= request.getParameter("rubriqueId") %>">
                    <% } %>
                </form>
                <form class="my-3" method="get" action="<%= request.getContextPath()%>/AdministrationRubrique">
                    <input type="submit" class="btn btn-primary" value="Afficher toutes les rubriques">
                    <input type="hidden" name="rubriqueFilter" value="">
                    <% if (request.getParameter("rubriqueId") != null) { %>
                    <input type="hidden" name="rubriqueId" value="<%= request.getParameter("rubriqueId") %>">
                    <% } %>
                </form>
                <form action="<%= request.getContextPath()%>/CreateRubrique" method="get">
                    <input type="submit" class="btn btn-primary mt-2" value="Creer une rubrique">
                </form>
            </div>
        </div>
        <div class="col mr-5">
            <% if (request.getParameter("rubriqueId") != null) { %>
            <% Rubrique rubrique = pandor.getRubrique(Integer.parseInt(request.getParameter("rubriqueId"))); %>
            <div class="row border-primary border-left border-right border-top pandor-foreground ">
                <div class="col-2">
                    <h5>Nom</h5>
                    <p><%= rubrique.getNom() %>
                    </p>
                </div>
                <div class="col">
                    <h5>Présentation</h5>
                    <p><%= rubrique.getPresentation() %>
                    </p>
                </div>
            </div>
            <div class="row border-bottom border-left border-right border-primary pandor-foreground">
                <div class="col text-center">
                </div>
            </div>
            <div class="row border-right border-bottom border-primary pandor-foreground">
                <div class="col border-left border-primary text-center">
                    <div class="row justify-content-center">
                        <h3> Changer le nom</h3>
                    </div>

                    <form action="<%= request.getContextPath()%>/AdministrationRubrique" method="post">
                        <input type="hidden" name="action" value="modifierNomRubrique">
                        <input type="hidden" name="rubriqueId" value="<%=rubrique.getId()%>">
                        <label>
                            <input type="text" name="modifierNomRubrique" maxlength="64" minlength="2">
                        </label>
                        <div class="row justify-content-center">
                            <input type="submit" class="btn btn-primary mt-2" value="Valider" maxlength="2047">
                        </div>
                    </form>
                </div>
                <div class="col border-left border-right border-primary text-center">
                    <div class="row justify-content-center">
                        <h3>Changer la description</h3>
                    </div>

                    <form action="<%= request.getContextPath()%>/AdministrationRubrique" method="post">
                        <input type="hidden" name="action" value="modifierPresentationRubrique">
                        <input type="hidden" name="rubriqueId" value="<%=rubrique.getId()%>">
                        <div class="form-group textarea-pandor">
                            <label for="modifierPresentationRubrique"></label>
                            <textarea class="form-control" id="modifierPresentationRubrique"
                                      name="modifierPresentationRubrique" rows="2"></textarea>
                        </div>
                        <div class="row justify-content-center">
                            <input type="submit" class="btn btn-primary mt-2" value="Valider">
                        </div>
                    </form>
                </div>
                <div class="col border-bottom border-left border-right text-center">
                    <form action="<%= request.getContextPath()%>/Accueil" method="get">
                        <input type="hidden" name="idRubrique" value="<%=rubrique.getId()%>">
                        <input type="submit" class="row justify-content-center btn-primary m-1 rounded form-signin mt-2"
                               value="Voir les topics dans la rubrique">
                    </form>

                    <form action="<%= request.getContextPath()%>/AdministrationRubrique" method="get">
                        <input type="hidden" name="nomRubrique" value="<%=rubrique.getNom()%>">
                        <input type="submit" class="row justify-content-center btn-primary m-1 rounded form-signin mt-2"
                               value="Voir les abonnés">
                    </form>

                </div>
            </div>
            <% } else if (request.getParameter("nomRubrique") != null) { 
            String nomRubrique = request.getParameter("nomRubrique");
            %>

            <div class="col">
                <% Rubrique rubrique = pandor.getRubriqueByName(nomRubrique);%>
                <div class="row border-primary border-left border-right border-top pandor-foreground ">
                    <div class="col-4">
                        <p>Liste des utilisateurs abonnés à : <strong><%= rubrique.getNom() %>
                        </strong></p>
                    </div>
                </div>
                <div class="row border-bottom border-left border-right border-primary pandor-foreground">
                    <div class="col-3">
                        <% for (User u : rubrique.getAbonneRubrique()) {%>
                        <a class="pandor-link"
                           href="<%= request.getContextPath()%>/AdministrationUtilisateur?userId=<%=u.getId()%>"><%=u.getPseudo()%>
                        </a><br/>
                        <% } %>
                    </div>
                </div>
                <div class="row border-right border-bottom border-primary pandor-foreground">
                </div>
                <% } else if (request.getParameter("nomRubriqueActuel") != null) {
                String nomRubriqueActuel = request.getParameter("nomRubriqueActuel");
                %>
                <div class="col">
                    <% Rubrique rubrique = pandor.getRubriqueByName(nomRubriqueActuel);%>
                    <div class="row border-primary border-left border-right border-top pandor-foreground ">
                        <div class="col-4">
                            <p>Liste des topics liés : <strong><%= rubrique.getNom() %>
                            </strong></p>
                        </div>
                    </div>
                    <div class="row border-bottom border-left border-right border-primary pandor-foreground">
                        <div class="col-3">
                            <% for (Topic t : rubrique.getTopics()) {%>
                            <a class="pandor-link"
                               href="<%= request.getContextPath()%>/Topic?idTopic=<%=t.getId()%>"><%=t.getTitre()%>
                            </a><br/>
                            <% } %>
                        </div>
                    </div>
                    <div class="row border-right border-bottom border-primary pandor-foreground">
                    </div>
                    <% } else { %>
                    <div class="row pandor-foreground border-primary border rounded justify-content-center mt-5">
                        Selectionnez une rubrique
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function filterFunction() {
        var input, filter, i, arrayForm, nameRubrique;
        input = document.getElementById("rubriqueFilter");
        filter = input.value.toUpperCase();
        arrayForm = document.getElementsByClassName("rubriqueForm");
        for (i = 0; i < arrayForm.length; i++) {
            nameRubrique = arrayForm[i].getElementsByClassName("nameRubrique")[0].value;
            if (nameRubrique.toUpperCase().indexOf(filter) > -1) {
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
