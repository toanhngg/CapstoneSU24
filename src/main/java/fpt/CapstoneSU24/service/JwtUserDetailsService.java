package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.Crypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    public Crypto crypto;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        fpt.CapstoneSU24.model.User c = userRepository.findOneByEmail(username);
        if (c != null) {
            try {
                return new User(c.getEmail(), new BCryptPasswordEncoder().encode(crypto.decrypt(c.getPassword())),
                        Collections.singleton(new SimpleGrantedAuthority(c.getRole().getRoleName())));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
