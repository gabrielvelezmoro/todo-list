package br.com.gabrielvelezmoro.todolist.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component

public class FilterTaskAuth extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var autorization = request.getHeader("Authorization");

        var user_password = autorization.substring("Basic".length()).trim();

        byte[] authDecoded= Base64.getDecoder().decode(user_password);

        var authString = new String(authDecoded);
        String[] credentials = authString.split(":");
        var username = credentials[0];
        var password = credentials[1];

        System.out.println(username);
        System.out.println(password);
        filterChain.doFilter(request,response);
    }
}
