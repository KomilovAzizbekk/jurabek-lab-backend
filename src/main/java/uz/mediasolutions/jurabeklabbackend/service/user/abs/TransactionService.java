package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.CardReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.WithdrawReqDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionService {

    ResponseEntity<?> getInfo();

    ResponseEntity<List<?>> getCards();

    ResponseEntity<Page<?>> getHistory(int page, int size, String type);

    ResponseEntity<?> addCard(CardReqDTO dto);

    ResponseEntity<?> withdraw(WithdrawReqDTO dto);

    ResponseEntity<?> editCard(UUID id, CardReqDTO dto);

    ResponseEntity<?> deleteCard(UUID id);
}
