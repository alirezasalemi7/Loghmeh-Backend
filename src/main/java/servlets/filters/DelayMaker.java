package servlets.filters;

import javax.servlet.*;
import java.io.IOException;

public class DelayMaker implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Thread.sleep(1500);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (InterruptedException e){

        }
    }

    @Override
    public void destroy() {

    }
}
