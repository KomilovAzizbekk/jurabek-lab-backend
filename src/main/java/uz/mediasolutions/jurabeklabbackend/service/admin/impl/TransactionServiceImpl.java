package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Notification;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.NotificationType;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.TrRequestDTO;
import uz.mediasolutions.jurabeklabbackend.repository.NotificationRepository;
import uz.mediasolutions.jurabeklabbackend.repository.TransactionRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.TransactionService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.UUID;

@Service("adminTransactionService")
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

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
        User user = transaction.getUser();

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setViewed(false);
        notification.setCardNumber(transaction.getCard().getNumber());
        notification.setCardName(transaction.getCard().getName());
        notification.setAmount(transaction.getAmount());
        notification.setTransactionId(transaction.getId().toString());

        if (paidOrRejected) {
            if (user.getBalance().subtract(transaction.getAmount()).floatValue() < 0) {
                throw RestException.restThrow("Transaction amount error", HttpStatus.BAD_REQUEST);
            }
            user.setBalance(user.getBalance().subtract(transaction.getAmount()));
            userRepository.save(user);

            notification.setType(NotificationType.TRANSACTION_ACCEPTED);
            transaction.setStatus(TransactionStatus.DONE);
        } else {
            notification.setType(NotificationType.TRANSACTION_REJECTED);
            transaction.setStatus(TransactionStatus.REJECTED);
        }
        notificationRepository.save(notification);
        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }
}
