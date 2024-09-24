package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenUserDTO;

import java.util.Map;

public interface AuthService {

    ResponseEntity<?> signIn(String lang, SignInDTO dto);

    ResponseEntity<TokenUserDTO> signUp(String lang, SignUpDTO dto);

    ResponseEntity<?> logout();

    void saveSmsInfo(Map<String, Object> callbackData);
}
