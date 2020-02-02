package ucbl.ptm.projet.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ucbl.ptm.projet.metier.Pandor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

import ucbl.ptm.projet.modele.Topic;
import ucbl.ptm.projet.modele.User;

@WebServlet(name = "Topic", urlPatterns = "/Topic")
public class TopicServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(TopicServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        String idTopicString = request.getParameter("idTopic");
        int idTopic;
        Topic topic = null;

        if (idTopicString != null && idTopicString.equals("")) {
            idTopicString = null;
        }

        if (idTopicString != null) {
            try {
                idTopic = Integer.parseInt(idTopicString);
                topic = pandor.getTopic(idTopic);
            } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
        }

        request.setAttribute("topic", topic);

        try {
            request.getRequestDispatcher("WEB-INF/jsp/afficheTopic/main.jsp").forward(request, response);
        } catch (IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

        int idMessage = 0;
        int idTopic = 0;
        try {
            idMessage = Integer.parseInt(request.getParameter("idMessage"));
            idTopic = Integer.parseInt(request.getParameter("idTopic"));
        } catch (NumberFormatException e) {
            try {
                response.sendRedirect(request.getContextPath() + "/Accueil");
            } catch (IOException ex) {
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
            }
        }


        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        String function = request.getParameter("function");

        switch (function) {
            case "respond":
                String contenu = request.getParameter("responseInput");
                String anonyme = request.getParameter("anonymeInput");

                contenu = new String(contenu.getBytes("iso-8859-1"), "utf8");

                if (anonyme != null) {
                    pandor.addResponse(pandor.getMessage(idMessage), pandor.addMessage(user, contenu, "Anonyme-" + Integer.toString((new Random(idTopic + user.getId())).nextInt(1000000000))));
                } else {
                    pandor.addResponse(pandor.getMessage(idMessage), pandor.addMessage(user, contenu, null));
                }
                break;
            case "upvote":
                pandor.upVote(pandor.getMessage(idMessage), user);
                break;
            case "downvote":
                pandor.downVote(pandor.getMessage(idMessage), user);
                break;
            case "delete":
                pandor.changeContenu(pandor.getMessage(idMessage), "<i>Message supprimé par la modération</i>");
                break;
            default:
                break;
        }

        try {
            response.sendRedirect(request.getContextPath() + "/Topic?idTopic=" + Integer.toString(idTopic) + "#reponse-" + Integer.toString(idMessage));
        } catch (IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
