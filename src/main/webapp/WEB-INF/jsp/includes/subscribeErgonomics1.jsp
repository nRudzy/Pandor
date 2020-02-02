<%@ page contentType="text/html; charset=UTF-8" %>

<%!private String enteredNom = "";%>
<%!private String enteredPrenom = "";%>
<%!private String enteredNumEtu = "";%>
<%!private String enteredEmail = "";%>
<%!private String enteredPseudo = "";%>
<% if (session.getAttribute("enteredNom") != null) { %>
<% enteredNom = (String) session.getAttribute("enteredNom"); %>
<% } %>
<% if (session.getAttribute("enteredPrenom") != null) { %>
<% enteredPrenom = (String) session.getAttribute("enteredPrenom"); %>
<% } %>
<% if (session.getAttribute("enteredNumEtu") != null) { %>
<% enteredNumEtu = (String) session.getAttribute("enteredNumEtu"); %>
<% } %>
<% if (session.getAttribute("enteredEmail") != null) { %>
<% enteredEmail = (String) session.getAttribute("enteredEmail"); %>
<% } %>
<% if (session.getAttribute("enteredPseudo") != null) { %>
<% enteredPseudo = (String) session.getAttribute("enteredPseudo"); %>
<% } %>
