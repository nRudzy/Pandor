package ucbl.ptm.projet.servlets;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AdministrationUtilisateur", urlPatterns = "/AdministrationUtilisateur")
public class AdministrationUtilisateur extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdministrationUtilisateur.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("WEB-INF/jsp/adminUser.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        
        if (request.getParameter("action") != null && request.getParameter("userId") != null) {
            Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");
            User user = new User();
            try {
                user = pandor.getUser(Integer.parseInt(request.getParameter("userId")));
            } catch (NumberFormatException e) {
                try {
                    response.sendRedirect(request.getContextPath() + "/Accueil");
                } catch(IOException ex) {
                    LOGGER.log(Level.INFO, ex.getMessage(), ex);
                }
            }
            switch (request.getParameter("action")) {
                case "setAdmin":
                    pandor.setAdmin(user);
                    break;
                case "rmAdmin":
                    pandor.rmAdmin(user);
                    break;
                case "ban":
                    User banner = (User) request.getSession().getAttribute("user");
                    int nbjours = 0;
                    if (request.getParameter("permaBan") != null) {
                        nbjours = 1000 * 365;
                    } else {
                        try {
                            nbjours = Integer.parseInt(request.getParameter("nbrJours"));
                        } catch (NumberFormatException e) {
                            try {
                                response.sendRedirect(request.getContextPath() + "/Accueil");
                            } catch(IOException ex) {
                                LOGGER.log(Level.INFO, ex.getMessage(), ex);
                            }
                        }
                    }
                    String motif = request.getParameter("motif");
                    pandor.banUser(banner, user, nbjours, motif);
                    break;
                case "unBan":
                    pandor.unBanUser(user);
                    break;
                case "updateMod":
                    for (Rubrique rubrique : pandor.getAllRubriques()) {
                        if (request.getParameter(Long.toString(rubrique.getId())) != null &&
                                !user.getAllModeratedRubrique().contains(rubrique)) {
                            pandor.setModerator(user, rubrique);
                        } else if (request.getParameter(Long.toString(rubrique.getId())) == null &&
                                user.getAllModeratedRubrique().contains(rubrique)) {
                            pandor.rmModerator(user, rubrique);
                        }
                    }
                    break;
                case "updateStatus":
                    pandor.updateTitle(user, request.getParameter("status"));
                    break;
                default:
                    break;
            }
        }
        try {
            request.getRequestDispatcher("WEB-INF/jsp/adminUser.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
