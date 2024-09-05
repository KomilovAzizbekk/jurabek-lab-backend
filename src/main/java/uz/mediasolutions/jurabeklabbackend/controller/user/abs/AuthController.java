package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/auth")
public interface AuthController {

    @PostMapping("/sign-in")
    ResponseEntity<?> signIn(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String lang,
                             @RequestBody @Valid SignInDTO dto);

    @PostMapping("/sign-up")
    ResponseEntity<TokenDTO> signUp(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String lang,
                                    @RequestBody @Valid SignUpDTO dto);

}
