package ucbl.ptm.projet.servlets;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CreateTopic", urlPatterns = "/CreateTopic")
public class CreateTopicServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateTopicServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        
        HttpSession session = request.getSession(true);
        String pseudo;


        String titre = request.getParameter("inputTitle");
        String description = request.getParameter("inputDescription");
        String stringRubrique = request.getParameter("selectRubrique");
        String tags = request.getParameter("inputTags");
        String[] tabTags = tags.split(",");
        User user = (User) session.getAttribute("user");

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        Rubrique rubrique = pandor.getRubriqueByName(stringRubrique);

        Message message = pandor.addMessage(user, description, null);

        if (!pandor.topicTaken(titre)) {
            Topic topic = pandor.addTopic(message, rubrique, titre);
            for (String tabTag : tabTags) {
                if (pandor.tagTaken(tabTag)) {
                    Tag tag = pandor.getTag(tabTag.trim());
                    pandor.addTagToTopic(topic, tag);
                } else {
                    Tag tag = pandor.addTag(tabTag.trim());
                    pandor.addTagToTopic(topic, tag);
                }
            }
            if (request.getParameter("anonPost") != null) {
                pseudo = "Anonyme-" + Integer.toString((new Random(topic.getId() + user.getId())).nextInt(1000000000));
                pandor.changeSousPseudo(topic, pseudo);
            }
        } else {
            try {
                redirectToCreateTopicPage(response, session, "topicAlreadyCreated");
            } catch(IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            return;
        }

        try {
            response.sendRedirect(request.getContextPath() + "/Accueil?idRubrique=" + Integer.toString((int) rubrique.getId()));
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.sendRedirect(request.getContextPath() + "/CreerTopic");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    private void redirectToCreateTopicPage(HttpServletResponse response, HttpSession session, String error) throws IOException {
        session.setAttribute("error", error);
        try {
            response.sendRedirect("CreerTopic");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

}
