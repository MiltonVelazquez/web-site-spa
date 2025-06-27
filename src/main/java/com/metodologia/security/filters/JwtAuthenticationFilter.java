package com.metodologia.security.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.User;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metodologia.models.UserEntity;
import com.metodologia.security.jwt.JwtUtils;
import com.metodologia.controller.request.LoginRequest;
import com.metodologia.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    @Autowired
    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        this.jwtUtils = jwtUtils;
    }


    // autentifica el intento de inicio de sesion
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                                                throws AuthenticationException {
        String username = "";
    String password = "";
    
    try {
        LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class); // CAMBIO ACÁ
        username = loginRequest.getUsername();
        password = loginRequest.getPassword();
    } catch (IOException e) {
        throw new RuntimeException("Error parsing login request: " + e.getMessage());
    }

    UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

    return getAuthenticationManager().authenticate(authenticationToken);
    }

    // si el inicio de sesion es correcto genera el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        // User user = (User) authResult.getPrincipal(); // obtenemos los detalles del usuario
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        UserEntity userEntity = userDetails.getUserEntity();
        String token = jwtUtils.generateAccesToken(userDetails.getUsername());

        response.addHeader("Authorization", token);

        Map<String, Object> httpResponse = new HashMap<>();

        httpResponse.put("id", userEntity.getId());
        httpResponse.put("token", token);
        httpResponse.put("Message", "Autenticacion Correcta");
        httpResponse.put("email", userEntity.getUsername());
        httpResponse.put("role",
            userEntity.getRoles().stream()
            .findFirst()
            .map(role -> role.getName().name())
            .orElse("UNKNOWN"));

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
  
    
}
