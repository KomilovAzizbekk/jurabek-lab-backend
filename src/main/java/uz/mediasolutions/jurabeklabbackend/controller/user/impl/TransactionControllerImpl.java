package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.TransactionController;
import uz.mediasolutions.jurabeklabbackend.payload.req.CardReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.WithdrawReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.TransactionService;

import java.util.List;

@RestController("userTransactionController")
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService service;

    @Override
    public ResponseEntity<?> getInfo() {
        return service.getInfo();
    }

    @Override
    public ResponseEntity<List<?>> getCards() {
        return service.getCards();
    }

    @Override
    public ResponseEntity<Page<?>> getHistory(int page, int size, String type) {
        return service.getHistory(page, size, type);
    }

    @Override
    public ResponseEntity<?> addCard(CardReqDTO dto) {
        return service.addCard(dto);
    }

    @Override
    public ResponseEntity<?> withdraw(WithdrawReqDTO dto) {
        return service.withdraw(dto);
    }
}
