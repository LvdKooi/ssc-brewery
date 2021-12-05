package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;


//my solution for the assignment of section 7
@Slf4j
public class PathParameterAuthFilter extends AbstractCustomAuthFilter {
    public PathParameterAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }

    @Override
    protected String getUsername(HttpServletRequest request) {
        return request.getParameter("user");
    }
}
