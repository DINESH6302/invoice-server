package com.invoice.service;

import com.invoice.dto.Tokens;
import com.invoice.dto.LoginRequestDto;
import com.invoice.dto.SignUpRequestDto;
import com.invoice.exception.AccountNotActiveException;
import com.invoice.exception.AuthenticationFailedException;
import com.invoice.exception.DuplicateResourceException;
import com.invoice.models.Enum.UserStatus;
import com.invoice.models.RefreshToken;
import com.invoice.models.User;
import com.invoice.repositorie.RefreshTokenRepository;
import com.invoice.repositorie.UserRepository;
import com.invoice.repositorie.UserStatusView;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${security.jwt.access-ttl-minutes}")
    private long accessTtlMinutes;

    @Value("${security.jwt.refresh-ttl-days}")
    private long refreshTtlDays;
    private final JWTService jwtService;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepo;

    @Autowired
    public AuthService(UserRepository userRepo, PasswordEncoder passwordEncoder, JWTService jwtService, RefreshTokenRepository refreshTokenRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {

        if (userRepo.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("User with email " + requestDto.getEmail() + " already exists");
        }

        String passwordHash = passwordEncoder.encode(requestDto.getPassword());

        User user = new User();
        user.setName(requestDto.getUserName());
        user.setEmail(requestDto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setUserStatus(UserStatus.ACTIVE);

        userRepo.save(user);
    }

    public Tokens login(LoginRequestDto requestDto) {

        User userObject = userRepo.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new AuthenticationFailedException("User with this email does not exist."));

        if (!passwordEncoder.matches(requestDto.getPassword(), userObject.getPasswordHash())) {
            throw new AuthenticationFailedException("Invalid password.");
        }

        if (!userObject.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new AccountNotActiveException("Account is not active.");
        }

        // Tokens Creation
        String refreshTokenId = UUID.randomUUID().toString();
        String access = jwtService.createAccessToken(userObject.getUserId(), Duration.ofMinutes(accessTtlMinutes));
        String refresh = jwtService.createRefreshToken(userObject.getUserId(), refreshTokenId, Duration.ofDays(refreshTtlDays));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshTokenId(refreshTokenId);
        refreshToken.setUser(userObject);
        refreshToken.setToken(refresh);
        refreshToken.setIssuedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS));

        refreshTokenRepo.save(refreshToken);

        return new Tokens(access, refresh);
    }

    public void logout(Cookie[] cookie) {
        String refreshToken = null;

        if (cookie != null) {
            refreshToken = Arrays.stream(cookie)
                    .filter(c -> "refresh_token".equals(c.getName()))
                    .map(Cookie::getValue).findFirst().orElse(null);
        }

        Jws<Claims> parsed = jwtService.parseAndValidate(refreshToken);
        String refreshTokenId = parsed.getBody().getId();

        refreshTokenRepo.deleteById(refreshTokenId);
    }

    @Transactional
    public Tokens refresh(Cookie[] cookie) {
        Long userId = null;
        try {
            String refreshToken = null;

            if (cookie != null) {
                refreshToken = Arrays.stream(cookie)
                        .filter(c -> "refresh_token".equals(c.getName()))
                        .map(Cookie::getValue).findFirst().orElse(null);
            }

            if (refreshToken == null) {
                throw new AuthenticationFailedException("Refresh token is missing.");
            }

            Jws<Claims> parsed = jwtService.parseAndValidate(refreshToken);

            String refreshTokenId = parsed.getBody().getId();
            userId = Long.valueOf(parsed.getBody().getSubject());


            // Check if user is active
            UserStatusView userStatus = userRepo.findStatusByUserId(userId);
            if (userStatus == null || !userStatus.getUserStatus().equals(UserStatus.ACTIVE)) {
                throw new AccountNotActiveException("Account is not active.");
            }

            Optional<RefreshToken> userRefreshToken = refreshTokenRepo.findById(refreshTokenId);

            if (userRefreshToken.isEmpty() || userRefreshToken.get().getExpiresAt().isBefore(Instant.now())) {
                throw new AuthenticationFailedException("Session has expired, Please login.");
            }

            String access = jwtService.createAccessToken(userId, Duration.ofMinutes(accessTtlMinutes));

            return new Tokens(access, userRefreshToken.get().getToken());
        } catch (Exception e) {
            throw new AuthenticationFailedException("something went wrong.");
        }
    }
}
