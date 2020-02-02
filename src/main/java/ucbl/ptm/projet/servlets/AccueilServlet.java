package ucbl.ptm.projet.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.Rubrique;
import ucbl.ptm.projet.modele.Topic;
import ucbl.ptm.projet.modele.User;

@WebServlet(name = "Accueil", urlPatterns = {"/Accueil", ""})
public class AccueilServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AccueilServlet.class.getName());
    private static final String ACCUEIL = "/Accueil";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        int idRubrique = 0;
        if (request.getParameter("idRubrique") == null)
            idRubrique = -1;
        else {
            try {
                idRubrique = Integer.parseInt(request.getParameter("idRubrique"));
            } catch (NumberFormatException e) {
                try {
                    response.sendRedirect(request.getContextPath() + ACCUEIL);
                } catch(IOException ex) {
                    LOGGER.log(Level.INFO, ex.getMessage(), ex);
                }
            }
        }
        
        int idUser = 0;
        if (request.getParameter("idUser") == null)
            idUser = -1;
        else {
            try {
                idUser = Integer.parseInt(request.getParameter("idUser"));
            } catch (NumberFormatException e) {
                try {
                    response.sendRedirect(request.getContextPath() + ACCUEIL);
                } catch(IOException ex) {
                    LOGGER.log(Level.INFO, ex.getMessage(), ex);
                }
            }
        }
        
        Date date;
        if (request.getParameter("date") == null) {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
            } catch (ParseException ex) {
                date = new Date();
            }
        } else {
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
            } catch (ParseException ex) {
                date = new Date();
            }
        }
        
        String[] mots;
        if (request.getParameter("mots") == null || request.getParameter("mots").equals("")) {
            mots = new String[0];
        } else {
            String s = request.getParameter("mots");
            mots = s.split(",");
        }
        
        String order;
        if (request.getParameter("order") == null)
            order = "upvote";
        else
            order = request.getParameter("order");

        List<Topic> topics;
        
        if (idRubrique == -1) {
            if (idUser == -1) {
                topics = pandor.getTopicBefore(date, mots);
            } else {
                if (user != null && user.getIsAdmin()) {
                    topics = pandor.getTopicByUserWithAnonyme(pandor.getUser(idUser), date, mots);
                } else {
                    topics = pandor.getTopicByUser(pandor.getUser(idUser), date, mots);
                }
            }
        } else {
            if (idUser == -1) {
                topics = pandor.getTopicByRubrique(pandor.getRubrique(idRubrique), date, mots);
            } else {
                if (user != null && user.getIsAdmin()) {
                    topics = pandor.getTopicByRubriqueAndUserWithAnonyme(pandor.getRubrique(idRubrique), pandor.getUser(idUser), date, mots);
                } else {
                    topics = pandor.getTopicByRubriqueAndUser(pandor.getRubrique(idRubrique), pandor.getUser(idUser), date, mots);
                }
            }
        }
        
        switch (order) {
            case "downvote":
                topics.sort((o1, o2) -> (o1.getMsg().getNbVotes() - o2.getMsg().getNbVotes()));
                break;
            case "date":
                topics.sort((o1, o2) -> (o2.getMsg().getDatePost().compareTo(o1.getMsg().getDatePost())));
                break;
            case "invdate":
                topics.sort((o1, o2) -> (o1.getMsg().getDatePost().compareTo(o2.getMsg().getDatePost())));
                break;
            default:
                topics.sort((o1, o2) -> (o2.getMsg().getNbVotes() - o1.getMsg().getNbVotes()));
                break;
        }

        request.setAttribute("topics", topics);

        try {
            request.getRequestDispatcher("WEB-INF/jsp/afficheAccueil/main.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

        int idRubrique = 0;
        try {
             idRubrique = Integer.parseInt(request.getParameter("idSub"));
        } catch (NumberFormatException e) {
            try {
                response.sendRedirect(request.getContextPath() + ACCUEIL);
            } catch(IOException ex) {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
            }
        }

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        Rubrique rubrique = pandor.getRubrique(idRubrique);

        if (user.getAbonneRubrique().contains(rubrique)) {
            pandor.unFollowRubrique(user, rubrique);
        } else {
            pandor.followRubrique(user, rubrique);
        }

        try {
            response.sendRedirect(request.getContextPath() + "/Accueil?idRubrique=" + Integer.toString(idRubrique));
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
