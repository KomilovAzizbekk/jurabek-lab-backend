package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.CardDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.WithdrawReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "app/payments")
public interface TransactionController {

    @GetMapping("/info")
    ResponseEntity<?> getInfo();

    @GetMapping("/cards")
    ResponseEntity<?> getCards();

    @GetMapping("/get-history")
    ResponseEntity<?> getHistory();

    @PostMapping("/add-card")
    ResponseEntity<?> addCard(@RequestBody @Valid CardDTO dto);

    @PostMapping("/withdraw")
    ResponseEntity<?> withdraw(@RequestBody @Valid WithdrawReqDTO dto);

}
