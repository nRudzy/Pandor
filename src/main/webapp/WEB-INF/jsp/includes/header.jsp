<%@ page import="ucbl.ptm.projet.modele.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<script>
    function fonctionDrop() {
        document.getElementById("elementsDuDropdown").classList.toggle("show");
    }

    //Close dropdown if click elsewhere
    window.onclick = function (event) {
        if (!event.target.matches('.pandor-dropbtn')) {
            var dropdowns = document.getElementsByClassName("pandor-dropdown-content");
            var i;
            for (i = 0; i < dropdowns.length; i++) {
                var openDropdown = dropdowns[i];
                if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                }
            }
        }
    }
</script>


<nav class="navbar navbar-expand-md navbar-dark sticky-top pandor-header" style="height: 80px">
    <img class="logo-pandor" src="https://zupimages.net/up/19/47/dxzi.png" alt="pandor-logo"/>
    <h2 class="pandor-header">PANDOR</h2>
    <div class="collapse navbar-collapse pl-3" id="navbarCollapse">
        <div class="navbar-nav mr-auto">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="<%= request.getContextPath()%>/Accueil">Page d'accueil</a>
                </li>
                <% if (session.getAttribute("user") != null && ((User) session.getAttribute("user")).getIsAdmin()) { %>
                <li class="nav-item active">
                    <a class="nav-link" href="<%= request.getContextPath()%>/AdministrationUtilisateur">Interface
                        administrateur</a>
                </li>
                <% } %>
            </ul>
        </div>
           
        <form method="get" action="<%= request.getContextPath()%>/Accueil">
        <div class="wrap">
            <div class="search">
                <input type="text" class="searchTerm" name="mots" placeholder="Rechercher par mots-clés (exemple : java,xml,jpa)">
                <button type="submit" class="searchButton">
                    <i class="fa fa-search"></i>
                </button>
            </div>
        </div>
        </form>

            
        <% if (session.getAttribute("user") != null) { %>
        <div class="pandor-dropdown mr-5">
            <button onclick="fonctionDrop()" class="btn btn-warning pandor-dropbtn">
                <em class="fas fa-user"></em> <%= ((User) session.getAttribute("user")).getPseudo()%>
            </button>
            <div id="elementsDuDropdown" class="pandor-dropdown-content rounded dropdown-menu-right ">
                <a href="<%= request.getContextPath()%>/MonCompte"><em class="fas fa-user-cog"></em> Mon compte</a>
                <a href="<%= request.getContextPath()%>/Disconnect"><em class="fas fa-sign-out-alt"></em> Déconnexion</a>
            </div>
        </div>
        <% } else { %>
        <form class="form-inline mt-2 mr-3" action="<%= request.getContextPath()%>/Connect">
            <button class="btn btn-primary my-2 my-sm-0" type="submit">Connexion</button>
        </form>
        <form class="form-inline mt-2 mr-5" action="<%= request.getContextPath()%>/Inscription">
            <button class="btn btn-warning my-2 my-sm-0" type="submit">Créer un compte</button>
        </form>
        <% } %>
    </div>
</nav>
