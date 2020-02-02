<%@ page contentType="text/html; charset=UTF-8" %>

<% if (session.getAttribute("error") != null) { %>
<%! String error; %>
<% error = (String) session.getAttribute("error"); %>
<% if ("wrongSecondPassword".equals(error)) { %>
<p class="error-message">Les mots de passe doivent-être identiques</p>
<% } else if ("emailAlreadyTaken".equals(error)) { %>
<p class="error-message">Un compte existe déjà pour cette adresse mail</p>
<% } else if ("pseudoAlreadyTaken".equals(error)) { %>
<p class="error-message">Ce pseudo est déjà pris</p>
<% } else if ("oldPasswordNotSame".equals(error)) { %>
<p class="error-message">L'ancien mot de passe renseigné ne correspond pas</p>
<% } else if ("notRegistered".equals(error)) { %>
<p class="error-message">Cette combinaison pseudo - mot de passe n'est pas valide</p>
<% } else if ("topicAlreadyCreated".equals(error)) { %>
<p class="error-message">Un topic avec le même nom a déja été créé</p>
<% } else if ("numEtuAlreadyTaken".equals(error)) { %>
<p class="error-message">Il y a déjà un compte pour ce numéro étudiant</p>
<% } else if ("userBanned".equals(error)) { %>
<p class="error-message">Ce compte a été banni jusqu'au <br/>
    <%=session.getAttribute("tempsBan")%> <br/>
    pour le motif suivant : <%=session.getAttribute("motifBan")%></p>
<% session.setAttribute("tempsBan", null); %>
<% session.setAttribute("motifBan", null); %>
<% } else { %>
<p class="error-message">Erreur inconnue</p>
<% } %>
<% } %>

<% session.setAttribute("error", null); %>

