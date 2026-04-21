package at.simcc.simcc_backend.api.service;

import at.simcc.simcc_backend.entities.User;
import at.simcc.simcc_backend.mapper.UserLoginDtoMapper;
import at.simcc.simcc_backend.models.UserLoginDto;
import at.simcc.simcc_backend.repo.UserRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: simcc_web
 * Created by: Marko Kushlyk
 * Date: 27.03.2026
 * Time: 10:17
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private static final String ISSUER = "SimCC";
    private final UserRepository userRepo;
    private final UserLoginDtoMapper userLoginAdminMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserLoginDto registerUser(UserLoginDto loginUser) {
        String secret = generateKey();
        String password = loginUser.getPassword();
        String hashed = passwordEncoder.encode(password);
        loginUser.setPassword(hashed);
        loginUser.setCreatedAt(LocalDateTime.now());
        loginUser.setTotpSecret(secret);
        User user = userLoginAdminMapper.toEntity(loginUser);
        user.setIsAdmin(true);
        userRepo.save(user);
        return userLoginAdminMapper.toDto(user);
    }

    /**
     * Generate a new TOTP key by using google authenticator
     * @return
     */
    public String generateKey(){
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    /**
     * Validates a TOTP (Time-based One-Time Password) code entered by the user.
     * Uses Google Authenticator to verify whether the provided 6-digit code
     * matches the expected code generated from the user's secret key at the current time.
     *
     * @param secret -> current totp-secret
     * @param code -> code, that user typed
     * @return -> gives a boolean back, which contains result of authorization
     */
    public Boolean isValid(String secret, int code){
        GoogleAuthenticator gAuth = new GoogleAuthenticator(
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build()
        );
        return gAuth.authorize(secret, code);
    }

    /**
     * Generates a QR code image from the given text and encodes it as a Base64 string.
     * The QR code is generated as a 200x200 PNG image using the ZXing library
     * and encoded to Base64 so it can be sent over HTTP and rendered directly
     * in the frontend as an image.
     * @param qrCodeText the text or URL to encode into the QR code
     * @return
     */
    public static String generateQRBase64(String qrCodeText) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200, hintMap);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Generates a Base64 encoded QR code image for Google Authenticator setup.
     * Builds an URL using the provided secret and username,
     * then converts it into a scannable QR code image encoded as a Base64 string.
     * The generated URL follows this format:
     *   otpauth://totp/{ISSUER}:{username}?secret={secret}&issuer={ISSUER}
     * The returned Base64 string can be rendered directly in the frontend:
     *   <img src="data:image/png;base64,{returned value}" />
     *
     * @param secret   the TOTP secret key generated for the user during registration
     * @param username the username associated with the account
     */
    public String generateQRUrl(String secret, String username) {
        String url = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(
                ISSUER,
                username,
                new GoogleAuthenticatorKey.Builder(secret).build());
        try {
            return generateQRBase64(url);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtain an QR-Code url by username parameter
     * @param username the username associated with the account
     */
    public String getQRUrl(String username) {
        User user = userRepo.getByUsername(username);
        String url = generateQRUrl(user.getTotpSecret(), user.getUsername());

        return url;
    }
}
