package ucbl.ptm.projet.servlets;

import javax.servlet.ServletException;
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

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.User;

@WebServlet(name = "MonCompte", urlPatterns = "/MonCompte")
public class AccountServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AccountServlet.class.getName());
    private static final String MONCOMPTE = "/MonCompte";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        
        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");
        User user = (User) request.getSession(false).getAttribute("user");
        HttpSession session = request.getSession(false);

        if (request.getMethod().equals("post") || request.getMethod().equals("POST")) {
            if (request.getParameter("changePseudo") != null) {
                String nouveauPseudo = request.getParameter("changePseudo");
                pandor.modifierPseudo(user, nouveauPseudo);
                try {
                    response.sendRedirect(request.getContextPath() + MONCOMPTE);
                } catch(IOException e) {
                    LOGGER.log(Level.INFO, e.getMessage(), e);
                }
            }

            if (request.getParameter("oldMdp") != null &&
                    request.getParameter("newMdp") != null &&
                    request.getParameter("confirmMdp") != null) {

                String ancienMdp = request.getParameter("oldMdp");
                String nouveauMdp = request.getParameter("newMdp");
                String confirmationMdp = request.getParameter("confirmMdp");

                if (!nouveauMdp.equals(confirmationMdp)) {
                    session.setAttribute("error", "wrongSecondPassword");
                    try {
                        response.sendRedirect(request.getContextPath() + MONCOMPTE);
                    } catch(IOException e) {
                        LOGGER.log(Level.INFO, e.getMessage(), e);
                    }
                } else if (!getSHA512(ancienMdp).equals(user.getHashMdp())) {
                    session.setAttribute("error", "oldPasswordNotSame");
                    try {
                        response.sendRedirect(request.getContextPath() + MONCOMPTE);
                    } catch(IOException e) {
                        LOGGER.log(Level.INFO, e.getMessage(), e);
                    }
                } else {
                    pandor.modifierMdp(user, getSHA512(nouveauMdp));
                    try {
                        response.sendRedirect(request.getContextPath() + MONCOMPTE);
                    } catch(IOException e) {
                        LOGGER.log(Level.INFO, e.getMessage(), e);
                    }
                }
            }
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        request.setAttribute("user", user);
        try {
            request.getRequestDispatcher("WEB-INF/jsp/account.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    private static String getSHA512(String input) {
        String toReturn = "";
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
