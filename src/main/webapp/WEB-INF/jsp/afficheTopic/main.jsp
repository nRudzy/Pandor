<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ucbl.ptm.projet.modele.Message" %>
<%@ page import="ucbl.ptm.projet.modele.Topic" %>
<%@ page import="ucbl.ptm.projet.modele.Tag" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ucbl.ptm.projet.modele.User" %>

<jsp:useBean id="pandor" scope="application" type="ucbl.ptm.projet.metier.Pandor"/>

<%
    User user = (User) session.getAttribute("user");
    Topic topic = (Topic) request.getAttribute("topic");
%>


<!DOCTYPE html>
<html lang="fr">
<head>
    <%@ include file="../includes/head.jsp" %>
    <title>Topic | Pandor</title>
</head>

<body class="pandor-background">
<%@ include file="../includes/header.jsp" %>
<div class="container" style="max-width: 1300px">
    <div class="row">
        <% if (topic == null) { %>
        <jsp:include page="errorId.jsp"/>
        <% } else { %>
        <div class="col-9">
            <div class="container pandor-foreground border border-primary rounded"
                 style="margin-bottom: 1rem; margin-top: 1rem;">
                <br>
                <div class="row">
                    <%--hidden form to get the users used Pseudo--%>
                    <form>
                        <input type="hidden" class="message-posted"
                               value="<%if(topic.getMsg().getSousPseudo() == null) {%><%=topic.getMsg().getAuteur().getPseudo()%><%
                               } else { %><%= topic.getMsg().getSousPseudo() %><% } %>"/>
                    </form>

                    <!-- PARTIE UPVOTE / DOWNVOTE -->
                    <% if (user == null) { %>
                    <div class="col-md-auto">
                        <em class="fas fa-chevron-up" style="color: #c8c8c8"></em>
                        <div class="row justify-content-center">
                            <%= topic.getMsg().getNbVotes()%>
                        </div>
                    </div>
                    <% } else { %>
                    <div class="col-md-auto">
                        <div class="row justify-content-center">
                            <form method="POST" action="">
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
                        </div>
                        <div class="row justify-content-center">
                            <%= topic.getMsg().getNbVotes()%>
                        </div>
                    </div>
                    <% } %>

                    <!-- PARTIE AFFICHAGE -->
                    <div class="col">
                        <div class="row">
                            <div class="col col-md-auto">
                                <a href="<%= request.getContextPath()%>/Accueil?idRubrique=<%= topic.getRubrique().getId()%>">
                                    <h5 style="display: inline">
                                        <%= topic.getRubrique().getNom()%>
                                    </h5>
                                </a>
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
                                <% if (user != null && (user.getIsAdmin() || user.getAllModeratedRubrique().contains(topic.getRubrique()))) { %>
                                <form method="POST" action="" style="display: inline">
                                    <input type="hidden" name="function" value="delete">
                                    <input type="hidden" name="idMessage" value="<%= topic.getMsg().getId()%>">
                                    <input type="hidden" name="idTopic" value="<%= topic.getId()%>">
                                    <button type="submit" name="rm" value="rm" class="btn-link">
                                        <small>
                                            Supprimer
                                        </small>
                                    </button>
                                </form>
                                <% } %>
                            </div>
                            <div class="col">
                                <div class="row justify-content-end">
                                    <% if (pandor.getTopic("Report du message " + Integer.toString((int) topic.getMsg().getId()) + " dans le topic " +  Integer.toString((int) topic.getId())) != null) { %>
                                    <form method="GET" action="<%= request.getContextPath()%>/Topic" style="display: inline; margin-right: 1rem">
                                        <input type="hidden" name="idTopic" value="<%= pandor.getTopic("Report du message " + Integer.toString((int) topic.getMsg().getId()) + " dans le topic " +  Integer.toString((int) topic.getId())).getId()%>">
                                        <button type="submit" name="report" value="report" class="btn-link">
                                            <i class="fas fa-flag"></i>
                                        </button>
                                    </form>
                                    <% } else { %>
                                    <form method="GET" action="<%= request.getContextPath()%>/CreerTopic" style="display: inline; margin-right: 1rem">
                                        <input type="hidden" name="rubrique" value="Modération">
                                        <input type="hidden" name="titre" value="Report du message <%= topic.getMsg().getId()%> dans le topic <%= topic.getId()%>">
                                        <input type="hidden" name="tags" value="Report, Msg-<%= topic.getMsg().getId()%>, Topic-<%= topic.getId()%>">
                                        <button type="submit" name="report" value="report" class="btn-link">
                                            <i class="fas fa-flag"></i>
                                        </button>
                                    </form>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                        <h3>
                            <%= topic.getTitre()%>
                        </h3>
                        <p>
                            <%= topic.getMsg().getContenu()%>
                        </p>
                        <small>
                            Tags :&nbsp;
                            <% for (Tag tag : topic.getTags()) { %>
                                <a href="<%= request.getContextPath()%>/Accueil?mots=<%= tag.getNom()%>">
                                    <%= tag.getNom()%>
                                </a>
                                &nbsp;
                            <% } %>
                        </small>

                        <!-- PARTIE REPONDRE -->
                        <% if (user != null) { %>
                        <br/>
                        <a data-toggle="collapse" href="#reponse-form-<%= topic.getMsg().getId()%>">
                            <small><strong>Répondre</strong></small>
                        </a>
                        <form class="collapse" id="reponse-form-<%= topic.getMsg().getId()%>" method="POST" action="">
                            <input type="hidden" name="function" value="respond">
                            <input type="hidden" name="idMessage" value="<%= topic.getMsg().getId()%>">
                            <input type="hidden" name="idTopic" value="<%= topic.getId()%>">
                            <div class="form-group">
                                <textarea maxlength="2000" class="form-control" rows="2" id="responseInput"
                                          placeholder="Ecrivez votre réponse..." required=""
                                          name="responseInput" onkeyup="autocompleteName()"></textarea>
                                <%--                           field that needs autocompletion--%>
                                <label for="responseInput" class="sr-only">Champ réponse</label>
                            </div>
                            <div class="form-group">
                                <button class="btn btn-primary" type="submit">Envoyer</button>
                            </div>
                            <div class="form-group">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="anonymeInput" value="anonyme"> Poster en anonyme
                                    </label>
                                </div>
                            </div>
                        </form>
                        <% } %>
                    </div>
                </div>
                <hr>

                <!-- PARTIE REPONSES -->
                <% for (Message m : topic.getMsg().getReponses()) {%>
                <jsp:include page="reponse.jsp">
                    <jsp:param name="msgId" value="<%= m.getId()%>"/>
                    <jsp:param name="topicId" value="<%= topic.getId()%>"/>
                    <jsp:param name="isGuest" value="<%= user == null%>"/>
                    <jsp:param name="isModo"
                               value="<%= user != null && (user.getIsAdmin() || user.getAllModeratedRubrique().contains(topic.getRubrique()))%>"/>
                </jsp:include>
                <% }%>
                <br>
                <br>
            </div>
        </div>

        <!-- PARTIE RUBRIQUE -->
        <div class="col-3">
            <div class="container pandor-foreground border border-primary rounded"
                 style="margin-bottom: 1rem; margin-top: 1rem;">
                <div class="row">
                    <div class="col">
                        <div class="row justify-content-center">
                            <a href="<%= request.getContextPath()%>/Accueil?idRubrique=<%= topic.getRubrique().getId()%>">
                                <h3>
                                    <%= topic.getRubrique().getNom()%>
                                </h3>
                            </a>
                        </div>
                        <div class="row justify-content-center">
                            <small>
                                <%= topic.getRubrique().getTopics().size()%> Topics
                                | <%= topic.getRubrique().getAbonneRubrique().size()%> Abonnés
                            </small>
                        </div>
                        <hr>
                        <p>
                            <%= topic.getRubrique().getPresentation()%>
                        </p>
                        <hr>
                        <h4>
                            Listes des topics
                        </h4>
                        <ul>
                            <% for (Topic t : topic.getRubrique().getTopics()) {%>
                            <li>
                                <a href="<%= request.getContextPath()%>/Topic?idTopic=<%= t.getId()%>">
                                    <%= t.getTitre()%>
                                </a>
                            </li>
                            <% }%>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <% }%>
    </div>
</div>

<%@ include file="../includes/tail.jsp" %>
</body>
</html>

