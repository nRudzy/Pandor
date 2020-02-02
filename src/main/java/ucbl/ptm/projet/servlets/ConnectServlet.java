package ucbl.ptm.projet.servlets;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.User;

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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Connect", urlPatterns = "/Connect")
public class ConnectServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(ConnectServlet.class.getName());
    private static final String ERROR = "error";

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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.getRequestDispatcher("WEB-INF/jsp/connect.jsp").forward(request, response);
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

        HttpSession session = request.getSession(true);

        String pseudo = request.getParameter("pseudo");
        String password = getSHA512(request.getParameter("password"));

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");
        User user = pandor.validateUser(pseudo, password);

        if (pandor.isBanned(user)) {
            session.setAttribute(ERROR, "userBanned");
            session.setAttribute("motifBan", pandor.getBanByUser(user).getMotif());
            session.setAttribute("tempsBan",
                    pandor.getBanByUser(user).getFin().toInstant().atZone(ZoneId.of("UTC+1")).format(DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss")));
            response.sendRedirect(request.getContextPath() + "/Connect");
            return;
        }

        session.setAttribute("user", user);

        if (user != null) {
            try {
                response.sendRedirect(request.getContextPath() + "/Accueil");
            } catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
        } else {
            session.setAttribute("error", "notRegistered");
            try {
                response.sendRedirect(request.getContextPath() + "/Connect");
            } catch (IOException e) {
                LOGGER.log(Level.INFO, e.getMessage(), e);
            }
        }
    }
}
