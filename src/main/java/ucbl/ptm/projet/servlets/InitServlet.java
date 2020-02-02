package ucbl.ptm.projet.servlets;

import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import ucbl.ptm.projet.metier.Pandor;

@WebListener
public class InitServlet implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        application.setAttribute("pandor", new Pandor(Persistence.createEntityManagerFactory("pu-pandor").createEntityManager()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //Nothing to do here
    }
}
