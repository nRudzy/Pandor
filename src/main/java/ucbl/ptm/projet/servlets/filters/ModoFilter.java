package ucbl.ptm.projet.servlets.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.User;

@WebFilter(filterName = "ModoFilter")
public class ModoFilter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig fc) {
        // No need
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        HttpSession session = request.getSession(true);

        if (request.getMethod().equalsIgnoreCase("GET")) {
            fc.doFilter(sr, sr1);
            return;
        }

        if (request.getParameter("function") != null && request.getParameter("idTopic") != null && session.getAttribute("user") != null) {
            if (!request.getParameter("function").equals("delete")) {
                fc.doFilter(sr, sr1);
                return;
            }

            int idTopic = Integer.parseInt(request.getParameter("idTopic"));
            User user = (User) session.getAttribute("user");

            Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

            if (user.getIsAdmin() || user.getAllModeratedRubrique().contains(pandor.getTopic(idTopic).getRubrique())) {
                fc.doFilter(sr, sr1);
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/Accueil");
    }

    @Override
    public void destroy() {
        //No need
    }
}
