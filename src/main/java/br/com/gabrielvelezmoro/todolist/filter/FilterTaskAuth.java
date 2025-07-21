package br.com.gabrielvelezmoro.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.gabrielvelezmoro.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component

public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired

    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var serveletPath = request.getServletPath();

        if (serveletPath.equals("/tasks/")) {

            var autorization = request.getHeader("Authorization");

            var user_password = autorization.substring("Basic".length()).trim();

            byte[] authDecoded = Base64.getDecoder().decode(user_password);

            var authString = new String(authDecoded);
            String[] credentials = authString.split(":");
            var username = credentials[0];
            var password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            System.out.println(username);
            System.out.println(password);

            if (user == null) {
                response.sendError(401);
            } else {

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }
        } else {
            filterChain.doFilter(request, response);

        }


    }
}
