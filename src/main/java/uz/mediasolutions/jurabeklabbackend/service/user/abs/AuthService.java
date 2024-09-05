package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;

public interface AuthService {

    ResponseEntity<?> signIn(String lang, SignInDTO dto);

    ResponseEntity<TokenDTO> signUp(String lang, SignUpDTO dto);

}
