package ucbl.ptm.projet.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Disconnect", urlPatterns = {"/Disconnect"})
public class DisconnectServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DisconnectServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", null);

        try {
            response.sendRedirect(request.getContextPath() + "/Accueil");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        
        HttpSession session = request.getSession(true);
        session.setAttribute("user", null);

        try {
            response.sendRedirect(request.getContextPath() + "/Accueil");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
