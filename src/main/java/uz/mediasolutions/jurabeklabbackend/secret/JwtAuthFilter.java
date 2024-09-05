package uz.mediasolutions.jurabeklabbackend.secret;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(Rest.AUTHORIZATION_HEADER);
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        // Faqat mavjud autentifikatsiya mavjud bo'lmaganda yangisini sozlash
        if (authorization != null && (existingAuth == null || !existingAuth.isAuthenticated())) {
            User user = null;
            if (authorization.startsWith("Bearer ")) {
                user = getUserFromBearerToken(authorization);
            }
            if (user != null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }


    public User getUserFromBearerToken(String token) {
        try {
            token = token.substring("Bearer".length()).trim();
            if (jwtService.validToken(token)) {
                UUID userId = jwtService.extractUserId(token);
                Optional<User> optionalUser = userRepository.findById(userId);
                return optionalUser.orElse(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
