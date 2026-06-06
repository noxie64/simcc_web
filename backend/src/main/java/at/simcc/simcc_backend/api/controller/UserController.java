package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.service.UserService;
import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.models.UserDto;
import at.simcc.simcc_backend.models.UserLoginDto;
import at.simcc.simcc_backend.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:16
 */
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepo;
    private final UserService userService;

    @PostMapping("/reg")
    public ResponseEntity<UserLoginDto> registerUser(@RequestBody UserLoginDto loginUser){
        return ResponseEntity.status(201).body(userService.registerUser(loginUser));
    }

    /**
     * Check if an admin account exists in database
     */
    @GetMapping("/check-if-exist")
    public ResponseEntity<Boolean> isAdminExist(){
        return ResponseEntity.ok(userRepo.existsById(0L));
    }

    /**
     * Obtain an QR-Code url by username parameter
     *
     * @param username the username associated with the account
     */
    @GetMapping("/obtain-qr-url")
    public ResponseEntity<String> getQRUrl(@RequestParam String username){
        return ResponseEntity.ok(userService.getQRUrl(username));
    }

    /**
     * Verify, if the code that the user wrote is correct or not.
     * @param code -> the code that user typed
     * @param username -> username of user, in order to get access to totp-secret
     */
    @GetMapping("/verify-2fa")
    public ResponseEntity<Boolean> isValid2FA(@RequestParam Integer code, @RequestParam String username){
        String secret = userRepo.getByUsername(username).orElseThrow().getTotpSecret();
        return ResponseEntity.ok(userService.isValid(secret, code));
    }

    /**
     * Verify, if the user credentials for login is right
     * @param credentials -> Map, which contains of the name of credential and the value of it.
     *                    email : test@test
     *                    password : 1234
     * @return -> return a boolean depending on the credentials
     */
    @PostMapping("/login-user")
    public ResponseEntity<Boolean> isValidUserCredential(@RequestBody Map<String, String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");
        if (email.isEmpty() || password.isEmpty()){
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(userService.isValidUserCredential(email, password));
    }

    /**
     * Verify the 2fa code, that user typed after the login
     * After successful verification, the session will be created
     * At first it creat token -> after which it creates context -> and in the end it will create session,
     *                                                              with this context
     * @param credentials -> Map, which contains of the name of credential and the value of it.
     *                    email -> email of the user
     *                    code -> 2fa code that user typed
     * @return -> returns if the code is correct or not
     */
    @PostMapping("/verify-2fa-after-login")
    public ResponseEntity<Boolean> isValid2FACode(
            @RequestBody Map<String, String> credentials,
            HttpServletRequest request){
        String code = credentials.get("code");
        String email = credentials.get("email");

        User user = userRepo.getUserByEmail(email);

        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        try{
            Integer codeL = Integer.parseInt(code);
            String secret = userRepo.getUserByEmail(email).getTotpSecret();
            if (code.isEmpty()){
                return ResponseEntity.ok(false);
            }

            if (!userService.isValid(secret ,codeL)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            return ResponseEntity.ok(true);
        }catch(NumberFormatException e){
            return ResponseEntity.badRequest().body(false);
        }
    }

}
