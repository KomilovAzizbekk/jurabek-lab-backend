package uz.mediasolutions.jurabeklabbackend.controller.common.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.common.abs.TransactionStatusController;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;

import java.util.Arrays;
import java.util.List;

@RestController
public class TransactionStatusControllerImpl implements TransactionStatusController {
    @Override
    public ResponseEntity<List<TransactionStatus>> getTransactionStatuses() {
        return ResponseEntity.ok(Arrays.asList(TransactionStatus.values()));
    }
}
