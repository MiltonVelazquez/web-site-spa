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

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metodologia.models.UserEntity;
import com.metodologia.security.jwt.JwtUtils;

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
        UserEntity userEntity = null;
        String username = "";
        String password = "";
        
        try{
            userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class); // mappea lo que viene de la request hacia la entidad jpa
            username = userEntity.getUsername();
            password = userEntity.getPassword();
        } catch(StreamReadException e){
            throw new RuntimeException(e);
        } catch(DatabindException e){
            throw new RuntimeException(e);
        } catch(IOException e){
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password); // autenticarse en la aplicacion
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    // si el inicio de sesion es correcto genera el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        User user = (User) authResult.getPrincipal(); // obtenemos los detalles del usuario
        String token = jwtUtils.generateAccesToken(user.getUsername());

        response.addHeader("Authorization", token);

        Map<String, Object> httpResponse = new HashMap<>();

        httpResponse.put(token, token);
        httpResponse.put("Message", "Autenticacion Correcta");
        httpResponse.put("Username", user.getUsername());

        response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
  
    
}
