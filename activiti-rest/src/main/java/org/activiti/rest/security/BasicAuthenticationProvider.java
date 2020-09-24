package org.activiti.rest.security;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Configuration
public class BasicAuthenticationProvider implements AuthenticationProvider {


    @Value("${activiti.byblos.username}")
    private String username;
    @Value("${activiti.byblos.password}")
    private String password;
    @Value("#{'${activiti.byblos.roles}'.split(',')}")
    private List<String> roles;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (name.equals(this.username) && password.equals(this.password)) {

            Collection<GrantedAuthority> grantedAuthorities = this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
        } else {
            throw new BadCredentialsException("Authentication failed for this username and password");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
