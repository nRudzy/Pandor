<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="ucbl.ptm.projet.modele.User" %>
<%@ page import="java.time.ZoneId" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="ucbl.ptm.projet.modele.Message" %>

<jsp:useBean id="pandor" scope="application" type="ucbl.ptm.projet.metier.Pandor"/>

<%
    Message msg = pandor.getMessage(Integer.parseInt(request.getParameter("msgId")));
    int topicId = Integer.parseInt(request.getParameter("topicId"));
    boolean isGuest = request.getParameter("isGuest").equals("true");
    boolean isModo = request.getParameter("isModo").equals("true");
%>

<div class="row">
    <%--hidden form to get the users used Pseudo--%>
    <form>
        <input type="hidden" class="message-posted" value="<%if(msg.getSousPseudo() == null) {%><%=msg.getAuteur().getPseudo()%><%
     } else { %><%= msg.getSousPseudo() %><% } %>"/>
    </form>

    <!-- PARTIE UPVOTE / DOWNVOTE -->
    <div class="col-md-auto">
        <div class="h-100 d-flex flex-column">
            <% if (isGuest) { %>
            <em class="fas fa-chevron-up" style="color: #c8c8c8"></em>
            <div class="row justify-content-center">
                <%= msg.getNbVotes()%>
            </div>
            <em class="fas fa-chevron-down" style="color: #c8c8c8"></em>
            <% } else { %>
            <form method="POST" action="">
                <input type="hidden" name="function" value="upvote">
                <input type="hidden" name="idMessage" value="<%= msg.getId()%>">
                <input type="hidden" name="idTopic" value="<%= topicId%>">
                <button type="submit" class="btn btn-default" title="Ce commentaire est utile">
                    <% if (pandor.isUpvoteur(msg, (User) session.getAttribute("user"))) {%>
                    <em class="fas fa-chevron-up" style="color: #00ff00"></em>
                    <% } else {%>
                    <em class="fas fa-chevron-up"></em>
                    <% }%>
                </button>
            </form>
            <div class="row justify-content-center">
                <%= msg.getNbVotes()%>
            </div>
            <form method="POST" action="">
                <input type="hidden" name="function" value="downvote">
                <input type="hidden" name="idMessage" value="<%= msg.getId()%>">
                <input type="hidden" name="idTopic" value="<%= topicId%>">
                <button type="submit" class="btn btn-default" title="Ce commentaire est inutile ou pas intéressant">
                    <% if (pandor.isDownvoteur(msg, (User) session.getAttribute("user"))) {%>
                    <em class="fas fa-chevron-down" style="color: #ff0000"></em>
                    <% } else {%>
                    <em class="fas fa-chevron-down"></em>
                    <% }%>
                </button>
            </form>
            <% } %>
            <div class="row justify-content-center flex-grow-1">
                <% if (!msg.getReponses().isEmpty()) {%>
                <a data-toggle="collapse" href="#reponse-<%= msg.getId()%>">
                    <div class="hr-msg" style="padding-right: 4px; padding-left: 4px; height: 100%;">
                        <hr class="vh">
                    </div>
                </a>
                <% }%>
            </div>
        </div>
    </div>

    <!-- PARTIE AFFICHAGE -->
    <div class="col">
        <% if (!isGuest) { %>
        <br>
        <% } %>
        <h5 style="display: inline">
            <% if (isModo) { %>
            <% if (msg.getSousPseudo() == null) { %>
            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= msg.getAuteur().getId()%>">
                <%= msg.getAuteur().getPseudo()%>
            </a>
            <% } else { %>
            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= msg.getAuteur().getId()%>" title="<%= msg.getAuteur().getPseudo()%>">
                <em>
                    <%= msg.getSousPseudo()%>
                </em>
            </a>
            <% } %>
            <% } else { %>
            <% if (msg.getSousPseudo() == null) { %>
            <a href="<%= request.getContextPath()%>/Accueil?idUser=<%= msg.getAuteur().getId()%>">
                <%= msg.getAuteur().getPseudo()%>
            </a>
            <% } else { %>
            <em>
                <%= msg.getSousPseudo()%>
            </em>
            <% } %>
            <% } %>
        </h5>
        <small>
            (<%= msg.getAuteur().getTitre()%>)&nbsp;
            <%= msg.getDatePost().toInstant().atZone(ZoneId.of("UTC+1")).format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss"))%>
        </small>
        <% if (isModo) { %>
        <form method="POST" action="" style="display: inline">
            <input type="hidden" name="function" value="delete">
            <input type="hidden" name="idMessage" value="<%= msg.getId()%>">
            <input type="hidden" name="idTopic" value="<%= topicId%>">
            <button type="submit" name="rm" value="rm" class="btn-link">
                <small>
                    Supprimer
                </small>
            </button>
        </form>
        <% } %>
        <p>
            <%= msg.getContenu()%>
        </p>

        <!-- PARTIE REPONDRE -->
        <% if (!isGuest) { %>
        <a data-toggle="collapse" href="#reponse-form-<%= msg.getId()%>">
            <small>Répondre</small>
        </a>
        <form action="" class="collapse" id="reponse-form-<%= msg.getId()%>" method="POST">
            <input type="hidden" name="function" value="respond">
            <input type="hidden" name="idMessage" value="<%= msg.getId()%>">
            <input type="hidden" name="idTopic" value="<%= topicId%>">
            <div class="form-group">
                <textarea maxlength="2000" class="form-control" rows="2" id="responseInput<%=msg.getId()%>"
                          placeholder="Ecrivez votre réponse..." required="" name="responseInput"
                          onkeyup="autocompleteName(<%= msg.getId() %>)"></textarea>
                <%--               field that needs autocompletion--%>
                <label for="responseInput<%=msg.getId()%>" class="sr-only">Champ réponse</label>
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

        <!-- PARTIE REPONSES -->
        <% if (!msg.getReponses().isEmpty()) {%>
        <% if (!isGuest) { %>
        <br>
        <% } %>
        <div id="reponse-<%= msg.getId()%>" class="collapse show">
            <% for (Message m : msg.getReponses()) {%>
            <jsp:include page="reponse.jsp">
                <jsp:param name="msgId" value="<%= m.getId()%>"/>
            </jsp:include>
            <% }%>
        </div>
        <% }%>
    </div>
</div>

<%--fnc that creates an autocompletion pop-up (currently just gets all users pseudo in the topic)--%>
<%--most things used are here but also 2 commented lines on main.jsp--%>
<script>
    function autocompleteName(id) {
        var input, lastTypedChar, users, i, messageList;
        input = document.getElementById("responseInput" + id);
        lastTypedChar = input.value.charAt(input.value.length - 1);
        //procs only on @ typed
        if (lastTypedChar === '@') {
            messageList = document.getElementsByClassName("message-posted");
            for (i = 0; i < messageList.length; i++) {
                input.value += messageList[i].value;
                console.log(messageList[i].value);
                user.add(messageList[i].value);
            }
        }
    }
</script>