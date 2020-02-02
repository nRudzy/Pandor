package ucbl.ptm.projet.servlets;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.Rubrique;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AdministrationRubrique", urlPatterns = "/AdministrationRubrique")
public class AdministrationRubrique extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdministrationRubrique.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");
        int idRubrique = 0;
        try {
            idRubrique = Integer.parseInt(request.getParameter("rubriqueId"));
        } catch (NumberFormatException e) {
            try {
                response.sendRedirect(request.getContextPath() + "/Accueil");
            } catch(IOException ex) {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
            }
        }
        request.setCharacterEncoding("UTF-8");
        if(request.getParameter("action") != null && request.getParameter("rubriqueId") != null) {
            Rubrique rubrique = pandor.getRubrique(idRubrique);
            String nomRubriqueEnCours = rubrique.getNom();
            String action = request.getParameter("action");
            switch (action) {
                case "modifierNomRubrique" :
                    String nouveauNom = request.getParameter("modifierNomRubrique");
                    pandor.modifierNomRubrique(
                            pandor.getRubriqueByName(nomRubriqueEnCours), nouveauNom);
                    break;
                case "modifierPresentationRubrique" :
                    String nouvellePresentation = request.getParameter("modifierPresentationRubrique");
                    pandor.modifierPresentationRubrique(
                            pandor.getRubriqueByName(nomRubriqueEnCours), nouvellePresentation);
                    break;
                default:
                    break;
            }
        }

        try {
            request.getRequestDispatcher("WEB-INF/jsp/adminRubric.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("WEB-INF/jsp/adminRubric.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
