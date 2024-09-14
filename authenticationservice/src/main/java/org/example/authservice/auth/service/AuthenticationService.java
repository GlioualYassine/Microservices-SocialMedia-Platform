package org.example.authservice.auth.service;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.authservice.Token.Token;
import org.example.authservice.Token.TokenRepository;
import org.example.authservice.auth.requests.AuthenticationRequest;
import org.example.authservice.auth.requests.ForgotPasswordRequest;
import org.example.authservice.auth.requests.RegistraitonRequest;
import org.example.authservice.auth.requests.ResetPasswordRequest;
import org.example.authservice.auth.responses.AuthenticationResponse;
import org.example.authservice.auth.responses.PasswordResetResponse;
import org.example.authservice.email.EmailService;
import org.example.authservice.email.EmailTemplateName;
import org.example.authservice.kafka.consumer.UserDeleteDto;
import org.example.authservice.kafka.consumer.UserUpdateDto;
import org.example.authservice.kafka.producer.UserDTO;
import org.example.authservice.kafka.producer.UserProducer;
import org.example.authservice.models.User;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserProducer userProducer;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl ;
    @Value("${application.mailing.frontend.reset-password-url}")
    private String resetPasswordUrl;

    public void register(RegistraitonRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                // todo - better exception handling
                .orElseThrow(()->new IllegalStateException("ROLE USER was  not initialized"));

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
       var u =  userRepository.save(user);
        UserDTO userDTO = UserDTO
                .builder()
                .id(u.getId())
                .email(u.getEmail())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .username(u.fullName())
                .build();
        userProducer.sendUser(userDTO);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newtoken = generateAndSaveActivationToken(user);
        /// TODO - send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newtoken,
                "Activate Account"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(15);
        var token = Token.builder()
                .token(generatedToken)
                .createdDate(LocalDateTime.now())
                .expiresDate(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String chracters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for(int i=0 ; i<length; i++){
            int randomIndex = random.nextInt(chracters.length()); // 0 - 9
            codeBuilder.append(chracters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        ); // if the authentication is successful , it will return the authenticated object, otherwise it will throw an exception

        // if we come to this line , it means the authentication is successful
        var claims = new HashMap<String,Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullName",user.fullName());
        var jwtToken = jwtService.generateToken(claims,user);
        return AuthenticationResponse
                .builder()
                .Token(jwtToken)
                .build();
    }

    //@Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("Token not found"));
        if(LocalDateTime.now().isAfter(savedToken.getExpiresDate())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to your email");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedDate(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }


    public void forgotPassword(@Valid ForgotPasswordRequest request) throws MessagingException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        var token = generateAndSaveActivationToken(user);

        var link = resetPasswordUrl + "?token=" + token;

        emailService.sendResetPasswordEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.RESET_PASSWORD,
                link,
                "Reset Password"
        );
    }


    public PasswordResetResponse resetPassword(ResetPasswordRequest request, String token) throws MessagingException {
        Token Token = tokenRepository.findByToken(token)
                .orElseThrow(()->new RuntimeException("Token not found"))
                ;
        User user = Token.getUser();

        if(LocalDateTime.now().isAfter(Token.getExpiresDate())){
            var link = resetPasswordUrl + "?token=" + token;
            emailService.sendResetPasswordEmail(
                    user.getEmail(),
                    user.fullName(),
                    EmailTemplateName.RESET_PASSWORD,
                    link,
                    "Reset Password"
            );
            throw new RuntimeException("Token has expired");
        }
        Token.setValidatedDate(LocalDateTime.now());
        tokenRepository.save(Token);
        String newPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
        return PasswordResetResponse.builder()
                .fullName(user.fullName())
                .email(user.getEmail())
                .build();
    }

    public void updateInfo(UserUpdateDto user){
        if(user.id() == null){
            throw new RuntimeException("User id is required");
        }
        var existingUser = userRepository.findById(user.id())
                .orElseThrow(()->new RuntimeException("User not found"));
        existingUser.setFirstName(user.firstName());
        existingUser.setLastName(user.lastName());
        existingUser.setEmail(user.email());
        userRepository.save(existingUser);}

    public void deleteUser(UserDeleteDto userDeleteDto) {
        if(userDeleteDto.id() == null){
            throw new RuntimeException("User id is required");
        }
        deleteUserWithTokens(userDeleteDto.id());
        userRepository.deleteById(userDeleteDto.id());
    }

    @Transactional
    public void deleteUserWithTokens(UUID userID) {
        // Récupérer l'utilisateur
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Supprimer tous les tokens associés à cet utilisateur
        List<Token> tokens = tokenRepository.findByUser(user);
        tokenRepository.deleteAll(tokens);

    }

}
