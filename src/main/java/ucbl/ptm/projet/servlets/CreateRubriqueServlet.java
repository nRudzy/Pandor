package ucbl.ptm.projet.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ucbl.ptm.projet.metier.Pandor;

@WebServlet(name = "CreateRubrique", urlPatterns = "/CreateRubrique")
public class CreateRubriqueServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreateRubriqueServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e){
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }
        
        String name = request.getParameter("inputTitre");
        String presentation = request.getParameter("inputPresentation");

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        if (!pandor.rubriqueTaken(name) && !name.equals("jUnit")) {
            pandor.addRubrique(name, presentation);
        }

        try {
            response.sendRedirect(request.getContextPath() + "/AdministrationRubrique");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.sendRedirect(request.getContextPath() + "/CreerRubrique");
        } catch(IOException e) {
            LOGGER.log(Level.INFO, e.getMessage(), e);
        }

    }
}
