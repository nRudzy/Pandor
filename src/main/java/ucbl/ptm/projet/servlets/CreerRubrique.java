package ucbl.ptm.projet.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CreerRubrique", urlPatterns = "/CreerRubrique")
public class CreerRubrique extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreerRubrique.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.getRequestDispatcher("WEB-INF/jsp/createRubric.jsp").forward(request, response);
        } catch(IOException | ServletException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
    }
}
