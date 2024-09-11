package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.TrRequestDTO;
import uz.mediasolutions.jurabeklabbackend.repository.TransactionRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.TransactionService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@Service("adminTransactionService")
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public ResponseEntity<Page<?>> getAll(int page, int size, String status) {
        Page<TrRequestDTO> transactionRequests = transactionRepository.getTransactionRequests(status, PageRequest.of(page, size));
        return ResponseEntity.ok(transactionRequests);
    }

    @Override
    public ResponseEntity<?> paidOrRejected(UUID id, boolean paidOrRejected) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Transaction not found", HttpStatus.NOT_FOUND)
        );
        if (paidOrRejected) {
            transaction.setStatus(TransactionStatus.DONE);
        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
        }
        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }
}
