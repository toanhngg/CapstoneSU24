package fpt.CapstoneSU24.service;


import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.model.AuthToken;
import fpt.CapstoneSU24.model.Location;
import fpt.CapstoneSU24.model.Role;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.RegisterRequest;
import fpt.CapstoneSU24.repository.AuthTokenRepository;
import fpt.CapstoneSU24.repository.LocationRepository;
import fpt.CapstoneSU24.repository.RoleRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.Const;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final LocationRepository locationRepository;
    private final AuthTokenRepository authTokenRepository;
    private final EmailService mailService;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            LocationRepository locationRepository,
            AuthTokenRepository authTokenRepository,
            EmailService mailService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.locationRepository = locationRepository;
        this.authTokenRepository = authTokenRepository;
        this.mailService = mailService;
    }

    public int signup(RegisterRequest input) {
        if (userRepository.findOneByEmail(input.getEmail()) == null) {
            if (roleRepository.findOneByRoleId(2) == null) {
                roleRepository.save(new Role(0, "admin"));
                roleRepository.save(new Role(0, "manufacture"));
            }
            User user = new User();
            user.setEmail(input.getEmail());
            user.setFirstName(input.getFirstName());
            user.setLastName(input.getLastName());
            user.setPhone(input.getPhone());
            user.setRole(roleRepository.findOneByRoleId(2));
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            user.setCreateAt(System.currentTimeMillis());
            Location location = new Location(0, input.getAddress(), input.getCity(), input.getCountry(), input.getCoordinateX(), input.getCoordinateY(), input.getDistrict(), input.getWard()); //manhDT sua bang
            locationRepository.save(location);
            user.setLocation(location);
            userRepository.save(user);
            authTokenRepository.save(new AuthToken(0, user, null));
            return 0;
        }
        return 1;
    }

    public int logout(User currentUser) {
        if (currentUser != null) {
            AuthToken authToken = authTokenRepository.findOneById(currentUser.getUserId());
            if (authToken != null) {
                authToken.setJwtHash(null);
                authTokenRepository.save(authToken);
                return 0;
            } else return 2;
        }
        return 1;
    }

    public int forgotPassword(String req) {
        try {
            JSONObject jsonNode = new JSONObject(req);
            String email = jsonNode.getString("email");
            User user = userRepository.findOneByEmail(email);
            if (user == null) {
                return 1;
            } else {
                String rndPass = generateRandomPassword();
                user.setPassword(rndPass);
                ChangePassword(user);
                DataMailDTO dataMail = new DataMailDTO();

                dataMail.setTo(user.getEmail());

                dataMail.setSubject(Const.SEND_MAIL_SUBJECT.SUBJECT_CHANGEPASS);

                Map<String, Object> props = new HashMap<>();
                props.put("name", user.getFirstName() + user.getLastName());
                props.put("newPassword", rndPass);

                dataMail.setProps(props);

                mailService.sendHtmlMail(dataMail, Const.TEMPLATE_FILE_NAME.CHANGEPASSWORD);
            }
            return 0;
        } catch (Exception e) {
            return 2;
        }
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

    public User ChangePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String generateRandomPassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String specialCharacters = "@#$%^&+=!*()";
        String allCharacters = lowercase + uppercase + digits + specialCharacters;
        SecureRandom random = new SecureRandom();

        int length = 8 + random.nextInt(9);
        StringBuilder password = new StringBuilder(length);

        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));

        for (int i = 4; i < length; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return shuffleString(password.toString());
    }

    private static String shuffleString(String string) {
        SecureRandom random = new SecureRandom();
        char[] characters = string.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

}