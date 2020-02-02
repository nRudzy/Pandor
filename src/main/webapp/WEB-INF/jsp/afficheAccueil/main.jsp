<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ucbl.ptm.projet.modele.Tag" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="ucbl.ptm.projet.modele.Topic" %>
<%@ page import="ucbl.ptm.projet.modele.Rubrique" %>
<%@ page import="java.util.List" %>
<%@ page import="ucbl.ptm.projet.modele.User" %>

<jsp:useBean id="pandor" beanName="pandor" type="ucbl.ptm.projet.metier.Pandor" scope="application"/>

<%
    User user = (User) session.getAttribute("user");
    List<Topic> topics = (List<Topic>) request.getAttribute("topics");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="../includes/head.jsp" %>
    <title>Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="../includes/header.jsp" %>
<div class="container" style="max-width: 1300px">
    <div class="row">
        <div class="col-9">
            <div class="container pandor-foreground border border-primary rounded"
                style="margin-bottom: 1rem; margin-top: 1rem;">
                <div class="row">
                    <div class="col-12">
                        <form class="form-inline" method="GET" action="" style="margin: 1rem">
                            <div class="row" style="width: 100%">
                                <div class="col-3">
                                    <div style="margin-top: 1rem" class="row justify-content-center">
                                        <h5>Rubrique</h5>
                                    </div>
                                    <div class="row">
                                        <select style="margin-top: 1rem; width: 100%" class=form-control id="idRubrique" name="idRubrique">
                                            <option style="font-weight:bold" selected="selected" value="-1">Toutes
                                            </option>
                                            <% for (Rubrique rubrique : pandor.getAllRubriques()) { %>
                                            <option value="<%=rubrique.getId()%>"><%=rubrique.getNom()%>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div style="margin-top: 1rem" class="row justify-content-center">
                                        <h5>User</h5>
                                    </div>
                                    <div class="row">
                                        <select style="margin: 1rem; width: 100%" class=form-control id="idUser" name="idUser">
                                            <option style="font-weight:bold" selected="selected" value="-1">Tous
                                            </option>
                                            <% for (User u : pandor.getAllUser()) { %>
                                            <option value="<%=u.getId()%>"><%=u.getPseudo()%>
                                            </option>
                                            <% } %>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div style="margin-top: 1rem" class="row justify-content-center">
                                        <h5>Après le</h5>
                                    </div>
                                    <div class="row">
                                        <input style="margin-top: 1rem; width: 100%" class="form-control" type="date" name="date" value="2000-01-01">
                                    </div>
                                </div>
                                <div class="col-3">
                                    <div style="margin-top: 1rem" class="row justify-content-center">
                                        <h5>Mots clefs</h5>
                                    </div>
                                    <div class="row">
                                        <input style="margin: 1rem; width: 100%" type="text" class="form-control" name="mots" placeholder="Exemple : java,xml,jpa">
                                    </div>
                                </div>
                            </div>
                            <div class="row" style="width: 100%">
                                <div class="col col-md-auto">
                                    <div class="row">
                                        <label style="margin-right: 1rem">Trier par :</label>
                                        <div class="form-check-inline">
                                            <label class="form-check-label">
                                                <input type="radio" class="form-check-input" name="order" checked value="upvote">Mieux notés
                                            </label>
                                        </div>
                                        <div class="form-check-inline">
                                            <label class="form-check-label">
                                                <input type="radio" class="form-check-input" name="order" value="downvote">Moins bien notés
                                            </label>
                                        </div>
                                        <div class="form-check-inline">
                                            <label class="form-check-label">
                                                <input type="radio" class="form-check-input" name="order" value="date">Plus récents
                                            </label>
                                        </div>
                                        <div class="form-check-inline">
                                            <label class="form-check-label">
                                                <input type="radio" class="form-check-input" name="order" value="invdate">Plus anciens
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="row justify-content-end">
                                        <button type="submit" class="btn btn-warning">
                                            Chercher&nbsp;<em class="fa fa-search"></em>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>        
            <% if (topics.isEmpty()) { %>
            <jsp:include page="errorId.jsp"/>
            <% } else { %>
            <div class="container pandor-foreground border border-primary rounded"
                 style="margin-bottom: 1rem; margin-top: 1rem;">
                <br>
                <% for (Topic topic : topics) {%>
                <div class="row">
                    <% if (user == null) { %>
                    <div class="col-md-auto">
                        <em class="fas fa-chevron-up" style="color: #c8c8c8"></em>
                        <div class="row justify-content-center">
                            <%= topic.getMsg().getNbVotes()%>
                        </div>
                    </div>
                    <% } else { %>
                    <div class="col-md-auto">
                        <form method="POST" action="<%= request.getContextPath()%>/Topic">
                            <input type="hidden" name="function" value="upvote">
                            <input type="hidden" name="idMessage" value="<%= topic.getMsg().getId()%>">
                            <input type="hidden" name="idTopic" value="<%= topic.getId()%>">
                            <button type="submit" class="btn btn-default" title="Ce sujet est utile">
                                <% if (pandor.isUpvoteur(topic.getMsg(), user)) { %>
                                <em class="fas fa-chevron-up" style="color: #00ff00"></em>
                                <% } else { %>
                                <em class="fas fa-chevron-up"></em>
                                <% } %>
                            </button>
                        </form>
                        <div class="row justify-content-center">
                            <%= topic.getMsg().getNbVotes()%>
                        </div>
                    </div>
                    <% } %>
                    <div class="col">
                        <a href="<%= request.getContextPath()%>/Accueil?idRubrique=<%= topic.getRubrique().getId()%>">
                            <h5 style="display: inline">
                                <%= topic.getRubrique().getNom()%>
                            </h5>
                        </a>
                        <small>(<%= topic.getRubrique().getTopics().size()%> Topics
                            | <%= topic.getRubrique().getAbonneRubrique().size()%> Abonnés)
                        </small>
                        -
                        <h5 style="display: inline">
                            <% if (user != null && (user.getIsAdmin() || user.getAllModeratedRubrique().contains(topic.getRubrique()))) { %>
                            <% if (topic.getMsg().getSousPseudo() == null) { %>
                            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= topic.getMsg().getAuteur().getId()%>">
                                <%= topic.getMsg().getAuteur().getPseudo()%>
                            </a>
                            <% } else { %>
                            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= topic.getMsg().getAuteur().getId()%>" title="<%= topic.getMsg().getAuteur().getPseudo()%>">
                                <em>
                                    <%= topic.getMsg().getSousPseudo()%>
                                </em>
                            </a>
                            <% } %>
                            <% } else { %>
                            <% if (topic.getMsg().getSousPseudo() == null) { %>
                            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= topic.getMsg().getAuteur().getId()%>">
                                <%= topic.getMsg().getAuteur().getPseudo()%>
                            </a>
                            <% } else { %>
                            <em>
                                <%= topic.getMsg().getSousPseudo()%>
                            </em>
                            <% } %>
                            <% } %>
                        </h5>
                        <small>
                            (<%= topic.getMsg().getAuteur().getTitre()%>)&nbsp;
                            <%= topic.getMsg().getDatePost().toInstant().atZone(ZoneId.of("UTC+1")).format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss"))%>
                        </small>
                        <a href="<%= request.getContextPath()%>/Topic?idTopic=<%= topic.getId()%>">
                            <h3><%= topic.getTitre()%>
                            </h3>
                        </a>
                        <p>
                            <%= topic.getMsg().getContenu()%>
                        </p>
                        Tags :&nbsp;
                        <small>
                            <% for (Tag tag : topic.getTags()) { %>
                                <a href="<%= request.getContextPath()%>/Accueil?mots=<%= tag.getNom()%>">
                                    <%= tag.getNom()%>
                                </a>
                                &nbsp;
                            <% } %>
                        </small>
                        <hr>
                    </div>
                </div>
                <% }%>
            </div>
            <% }%>
        </div>

        <!-- PARTIE RUBRIQUE -->
        <div class="col-3">
            <div class="container pandor-foreground border border-primary rounded"
                 style="margin-bottom: 1rem; margin-top: 1rem;">
                <div class="row">
                    <div class="col">

                        <% if (user != null) { %>
                        <div class="row justify-content-center">
                            <form class="form-inline" method="GET" action="<%= request.getContextPath()%>/CreerTopic"
                                  style="margin: 1rem">
                                <button class="btn btn-warning" type="submit">Créer un topic</button>
                            </form>
                        </div>
                        <hr>
                        <h4>
                            Rubriques suivies :
                        </h4>
                        <ul>
                            <% for (Rubrique rubrique : user.getAbonneRubrique()) {%>
                            <li>
                                <a href="<%= request.getContextPath()%>/Accueil?idRubrique=<%= rubrique.getId()%>">
                                    <%= rubrique.getNom()%>
                                </a>
                            </li>
                            <% }%>
                        </ul>
                        <hr>
                        <% } %>
                        <h4>
                            Toutes les rubriques :
                        </h4>
                        <ul>
                            <% for (Rubrique rubrique : pandor.getAllRubriques()) {%>
                            <li>
                                <% if (user != null) { %>
                                <% if (user.getAbonneRubrique().contains(rubrique)) { %>
                                <form class="form-inline" method="POST" action="" style="display: inline">
                                    <input type="hidden" name="idSub" value="<%= rubrique.getId()%>"/>
                                    <button class="btn" type="submit">
                                        <em class="fas fa-rss" style="color: #00ff00">
                                        </em>
                                    </button>
                                </form>
                                <% } else { %>
                                <form class="form-inline" method="POST" action="" style="display: inline">
                                    <input type="hidden" name="idSub" value="<%= rubrique.getId()%>"/>
                                    <button class="btn" type="submit">
                                        <em class="fas fa-rss" style="color: #000000">
                                        </em>
                                    </button>
                                </form>
                                <% } %>
                                <% } %>
                                <a href="<%= request.getContextPath()%>/Accueil?idRubrique=<%= rubrique.getId()%>">
                                    <%= rubrique.getNom()%>
                                </a>
                            </li>
                            <% }%>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="../includes/tail.jsp" %>
</body>
</html>