package uz.mediasolutions.jurabeklabbackend.controller.common.abs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH)
public interface RefreshTokenController {

    @PostMapping("/refresh-token")
    ResponseEntity<TokenDTO> refresh(HttpServletRequest request);

}
