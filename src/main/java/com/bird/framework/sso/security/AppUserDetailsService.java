package com.bird.framework.sso.security;

import com.bird.framework.sso.rest.consumer.UserRest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRest userRest;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        String user = userRest.getByUsername(s);
        if (user == null) {
            user = userRest.getByNick(s);
        }
        if (user == null) {
            user = userRest.getByMobile(s);
        }
        if (StringUtils.isEmpty(user)) {
            throw new UsernameNotFoundException(String.format("The username %s doesn't exist", s));
        }
        try {
            Map<String, Object> userMap = objectMapper.readValue(user, new TypeReference<Map<String, Object>>() {
            });
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("admin"));

            UserDetails userDetails = new org.springframework.security.core.userdetails.
                    User(s, (String) userMap.get("password"), authorities);
            return userDetails;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }

    }
}
