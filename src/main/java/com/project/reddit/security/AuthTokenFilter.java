package com.project.reddit.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUserDetailesService userDetailesService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtTokenUtil.validateToken(jwt)) {
                String username = jwtTokenUtil.getUsernameByJwt(jwt);
                UserDetails userDetails = userDetailesService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }catch (Exception e) {
            log.error("Some exception", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }


    private List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/swagger-ui.html",
            "/swagger-uui.html", "/webjars/springfox-swagger-ui/springfox.css",
            "/webjars/springfox-swagger-ui/swagger-ui-bundle.js", "/webjars/springfox-swagger-ui/swagger-ui.css",
            "/webjars/springfox-swagger-ui/swagger-ui-standalone-preset.js",
            "/webjars/springfox-swagger-ui/springfox.js", "/swagger-resources/configuration/ui",
            "/webjars/springfox-swagger-ui/favicon-32x32.png", "/swagger-resources/configuration/security",
            "/swagger-resources", "/v2/api-docs",
            "/webjars/springfox-swagger-ui/fonts/titillium-web-v6-latin-700.woff2",
            "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-regular.woff2",
            "/webjars/springfox-swagger-ui/fonts/open-sans-v15-latin-700.woff2",
            "/webjars/springfox-swagger-ui/favicon-16x16.png"));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        if (excludeUrlPatterns.contains(path)) {
            return true;
        }
        return false;
    }
}
