package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TransactionService {

    ResponseEntity<Page<?>> getAll(int page, int size, String status);

    ResponseEntity<?> paidOrRejected(UUID id, boolean paidOrRejected);

}
