package uz.mediasolutions.jurabeklabbackend.controller.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.common.abs.TransactionTypeController;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionTypeControllerImpl implements TransactionTypeController {

    @Override
    public ResponseEntity<List<TransactionType>> getTransactionTypes() {
        return ResponseEntity.ok(Arrays.asList(TransactionType.values()));
    }

}
