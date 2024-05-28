package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.dto.RegisterUserDto;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.LocationRepository;
import fpt.CapstoneSU24.repository.RoleRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setEmail(input.getEmail());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setPhone(input.getPhone());
        user.setRole(roleRepository.findOneByRoleId(input.getRole()));
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setCreateAt(System.currentTimeMillis());
        user.setCreateAt(System.currentTimeMillis());
        Location location = new Location(0,input.getAddress(), input.getCity(), input.getCountry(), "");
        locationRepository.save(location);
        user.setLocation(location);
        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        return userRepository.findByEmail(email)
                .orElseThrow();
    }
}