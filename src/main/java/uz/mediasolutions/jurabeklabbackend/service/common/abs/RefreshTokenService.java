package uz.mediasolutions.jurabeklabbackend.service.common.abs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;

public interface RefreshTokenService {

    ResponseEntity<TokenDTO> refresh(HttpServletRequest request);

}
