package ucbl.ptm.projet.servlets.filters;

import ucbl.ptm.projet.metier.Pandor;
import ucbl.ptm.projet.modele.User;

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

@WebFilter(filterName = "CoFilter")
public class CoFilter implements javax.servlet.Filter {
    @Override
    public void init(FilterConfig fc) {
        //No need
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        HttpSession session = request.getSession(true);

        if (request.getMethod().equalsIgnoreCase("GET") && !request.getRequestURI().equals(request.getContextPath() + "/MonCompte")) {
            fc.doFilter(sr, sr1);
            return;
        }

        if (request.getRequestURI().equals(request.getContextPath() + "/Connect") || request.getRequestURI().equals(request.getContextPath() + "/Inscription")) {
            fc.doFilter(sr, sr1);
            return;
        }

        Pandor pandor = (Pandor) request.getServletContext().getAttribute("pandor");

        if (session.getAttribute("user") != null && !pandor.isBanned((User) session.getAttribute("user"))) {
            fc.doFilter(sr, sr1);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/Accueil");
    }

    @Override
    public void destroy() {
        //No need
    }
}
