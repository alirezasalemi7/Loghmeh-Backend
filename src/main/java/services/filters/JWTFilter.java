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
            if(jwtToken==null || !jwtToken.startsWith(AuthenticationManager.getInstance().getAUTH_TOKEN_PREFIX())){
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
            try {
                Claims claims = Jwts.parserBuilder()
                        .requireSubject("userId")
                        .requireSubject("iat")
                        .requireSubject("iss")
                        .requireSubject("exp")
                        .setSigningKey(AuthenticationManager.getInstance().getSECRET_KEY())
                        .build()
                        .parseClaimsJws(jwtToken.replace(AuthenticationManager.getInstance().getAUTH_TOKEN_PREFIX(), ""))
                        .getBody();
                String userId = (String) claims.get("userId");
                request.setAttribute("userId", userId);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            catch (JwtException e){
                ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
