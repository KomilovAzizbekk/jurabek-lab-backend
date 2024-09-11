package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.TransactionController;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.TransactionService;

import java.util.UUID;

@RestController("adminTransactionController")
@RequiredArgsConstructor
public class TransactionControllerImpl implements TransactionController {

    private final TransactionService service;

    @Override
    public ResponseEntity<Page<?>> getAll(int page, int size, String status) {
        return service.getAll(page, size, status);
    }

    @Override
    public ResponseEntity<?> paidOrRejected(UUID id, boolean paidOrRejected) {
        return service.paidOrRejected(id, paidOrRejected);
    }
}
