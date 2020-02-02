package ucbl.ptm.projet.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.User;


@WebServlet(name = "Inscription", urlPatterns = "/Inscription")
public class RegisterServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());
    private static final String ERROR = "error";
    private static final String INSCRIPTION = "/Inscription";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.getRequestDispatcher("WEB-INF/jsp/register.jsp").forward(request, response);
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
        
        HttpSession session = request.getSession(true);

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String numEtu = request.getParameter("numEtu");
        String email = request.getParameter("email");
        String pseudo = request.getParameter("pseudo");
        String password = request.getParameter("password");

        //for ergonomic purposes
        session.setAttribute("enteredNom", nom);
        session.setAttribute("enteredPrenom", prenom);
        session.setAttribute("enteredNumEtu", numEtu);
        session.setAttribute("enteredEmail", email);
        session.setAttribute("enteredPseudo", pseudo);

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        if (pandor.pseudoTaken(pseudo) || pseudo.equals("jUnit")) {
            session.setAttribute("enteredPseudo", null);
            session.setAttribute(ERROR, "pseudoAlreadyTaken");
            try {
                response.sendRedirect(request.getContextPath() + INSCRIPTION);
            } catch(IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            return;
        }
        if (pandor.emailTaken(email) || email.equals("test@junit.fr")) {
            session.setAttribute("enteredEmail", null);
            session.setAttribute(ERROR, "emailAlreadyTaken");
            try {
                response.sendRedirect(request.getContextPath() + INSCRIPTION);
            } catch(IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            return;
        }
        if (pandor.numEtuTaken(numEtu) || numEtu.equals("p1909999")) {
            session.setAttribute("enteredNumEtu", null);
            session.setAttribute(ERROR, "numEtuAlreadyTaken");
            try {
                response.sendRedirect(request.getContextPath() + INSCRIPTION);
            } catch(IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            return;
        }
        if (!password.equals(request.getParameter("secondPassword"))) {
            session.setAttribute(ERROR, "wrongSecondPassword");
            try {
                response.sendRedirect(request.getContextPath() + INSCRIPTION);
            } catch(IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
            return;
        }

        User user = pandor.addUser(email, nom, prenom, pseudo, getSHA512(password), numEtu, "Etudiant");

        session.setAttribute("user", user);

        try {
            response.sendRedirect(request.getContextPath() + "/Accueil");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    private static String getSHA512(String input) {
        String toReturn = null;
        input = "Deux" + input + "Mots";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

        return toReturn;
    }
}
