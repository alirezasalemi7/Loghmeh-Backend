package services.filters;

import business.ServiceManagers.AuthenticationManager;
import io.jsonwebtoken.*;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class JWTFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
        if(AuthenticationManager.getInstance().getExcludedPath().contains(path)){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            String jwtToken = request.getHeader(AuthenticationManager.getInstance().getAUTH_TOKEN());
            System.err.println(jwtToken);
            if(jwtToken==null || !jwtToken.startsWith(AuthenticationManager.getInstance().getAUTH_TOKEN_PREFIX())){
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(AuthenticationManager.getInstance().getKey())
                        .build()
                        .parseClaimsJws(jwtToken.replace(AuthenticationManager.getInstance().getAUTH_TOKEN_PREFIX(), ""))
                        .getBody();
                String userId = (String) claims.get("userId");
                if(userId==null){
                    System.err.println("here");
                    throw new JwtException("user id not found");
                }
                request.setAttribute("userId", userId);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            catch (JwtException e){
                System.err.println(e.getMessage());
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            catch (NullPointerException e){
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void destroy() {

    }
}
