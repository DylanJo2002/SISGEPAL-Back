package com.SISGEPAL.services;

import com.SISGEPAL.DTO.session.request.User;
import com.SISGEPAL.DTO.session.response.Session;
import com.SISGEPAL.entities.Empleado;
import com.SISGEPAL.entities.Login;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.exceptions.UnauthorizedException;
import com.SISGEPAL.repositories.LoginRepository;
import com.SISGEPAL.utils.JWTUtil;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    JWTUtil jwtUtil;

    private PasswordEncoder encoder;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private AdministradorService administradorService;

    public Session validateSession(User user) throws NotFoundException, UnauthorizedException {
        Login userLogin = loginRepository.findByUsuario(user.getUsername());

        if(userLogin == null) {
            throw new NotFoundException(String.format(
                    "No existe el usuario %s", user.getUsername()
            ));
        }
        Session session = null;

        if(encoder.matches(user.getPassword(),userLogin.getPassword())){
            session = new Session();
            session.setToken(createToken(user,userLogin));
        }

        if(session == null) {
            throw new UnauthorizedException(String.format(
                    "Contraseña incorrecta para el usuario %s", user.getUsername()
            ));
        }

        return session;
    }

    public String createToken(User user, Login userLogin){


        Empleado empleado = userLogin.getEmpleado();
        Map<String,Object> claims = new HashMap<>();
        List<GrantedAuthority> authorities = createAuthorities(empleado);

        claims.put("nombre",empleado.getNombre());
        claims.put("nombre",empleado.getNombre());
        claims.put("usuario",userLogin.getUsuario());
        claims.put("correo",empleado.getCorreo());
        claims.put("authorities",
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));


        return jwtUtil.createToken(claims,empleado.getCedula());
    }

    public List<GrantedAuthority> createAuthorities(Empleado empleado){
        List<GrantedAuthority> grantedAuthorities = null;
        if(administradorService.isAdministrador(empleado)){
            grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER, ROLE_ADMIN");
        } else {
            grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
        }
        return grantedAuthorities;
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}