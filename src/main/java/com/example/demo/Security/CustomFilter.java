package com.example.demo.Security;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomFilter extends UsernamePasswordAuthenticationFilter {

    public CustomFilter(String url, AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);

        setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler(){
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                Cookie pCookie = new Cookie("password", request.getParameter("password"));
                Cookie lCookie = new Cookie("login", request.getParameter("username"));
                pCookie.setMaxAge(60*60*24*365);
                lCookie.setMaxAge(60*60*24*365);
                response.addCookie(pCookie);
                response.addCookie(lCookie);
                super.setDefaultTargetUrl("/postCookie");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        });

        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(){
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                super.setDefaultFailureUrl("/login?error");
                super.onAuthenticationFailure(request, response, exception);
            }
        });

        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(url,HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return super.attemptAuthentication(request, response);
    }
}
