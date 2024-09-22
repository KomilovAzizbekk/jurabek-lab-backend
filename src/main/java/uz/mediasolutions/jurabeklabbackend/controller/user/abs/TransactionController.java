package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.payload.req.CardReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.WithdrawReqDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;
import java.util.UUID;

@RequestMapping(Rest.BASE_PATH + "app/transactions")
public interface TransactionController {

    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> getInfo();

    @GetMapping("/cards")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<List<?>> getCards();

    @GetMapping("/get-history")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<Page<?>> getHistory(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                       @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                       @RequestParam(required = false) String type);

    @PostMapping("/add-card")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> addCard(@RequestBody @Valid CardReqDTO dto);

    @PostMapping("/edit-card/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> editCard(@PathVariable UUID id,
                               @RequestBody CardReqDTO dto);

    @DeleteMapping("/delete-card/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> deleteCard(@PathVariable UUID id);

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<?> withdraw(@RequestBody @Valid WithdrawReqDTO dto);

}
