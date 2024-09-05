package uz.mediasolutions.jurabeklabbackend.service.common.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.RefreshToken;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.repository.RefreshTokenRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.secret.JwtService;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.UserService;
import uz.mediasolutions.jurabeklabbackend.service.common.abs.RefreshTokenService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<TokenDTO> refresh(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final UUID userId;

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw RestException.notFound("Authorization header is missing");
        }
        refreshToken = authorization.substring(7);

        userId = jwtService.extractUserId(refreshToken);
        if (userId == null) {
            throw RestException.restThrow("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty() || !refreshTokenRepository.existsByUserId(optionalUser.get().getId())) {
            throw RestException.restThrow("User not found with this token", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
        if (!passwordEncoder.matches(refreshToken, token.getToken())) {
            throw RestException.restThrow("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        if (jwtService.isTokenValid(refreshToken, user)) {
            String accessToken = jwtService.generateToken(user);
            var tokenDTO = new TokenDTO("Bearer", accessToken, refreshToken);
            return ResponseEntity.ok(tokenDTO);
        } else {
            throw RestException.restThrow("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }
    }
}
