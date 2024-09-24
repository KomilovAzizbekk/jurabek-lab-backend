package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenUserDTO;
import uz.mediasolutions.jurabeklabbackend.service.user.impl.SmsService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Map;
import java.util.Objects;

@RequestMapping(Rest.BASE_PATH + "app/auth")
public interface AuthController {

    @PostMapping("/sign-in")
    ResponseEntity<?> signIn(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String lang,
                             @RequestBody @Valid SignInDTO dto);

    @PostMapping("/sign-up")
    ResponseEntity<TokenUserDTO> signUp(@RequestHeader(name = "Accept-Language", defaultValue = "uz") String lang,
                                        @RequestBody @Valid SignUpDTO dto);

    @PostMapping("/logout")
    ResponseEntity<?> logout();

    @PostMapping("/sms-callback")
    void getSmsInfo(@RequestBody Map<String, Object> callbackData);

}
