package at.simcc.simcc_backend.api.controller;

import at.simcc.simcc_backend.api.service.UserService;
import at.simcc.simcc_backend.models.UserLoginDto;
import at.simcc.simcc_backend.repo.UserRepository;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:16
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
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
        String secret = userRepo.getByUsername(username).getTotpSecret();
        return ResponseEntity.ok(userService.isValid(secret, code));
    }

    /**
     * Verify, if the user credentials for login is right
     * @param email -> email of the user
     * @param password -> his password
     * @return -> return a boolean depending on the credentials
     */
    @GetMapping("/login-user")
    public ResponseEntity<Boolean> isValidUserCredential(@RequestParam String email, @RequestParam String password){
        if (email.isEmpty() || password.isEmpty()){
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(userService.isValidUserCredential(email, password));
    }

}
