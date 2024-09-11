package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Card;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.CardDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.InfoDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.TransactionHistoryDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.CardReqDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.WithdrawReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.CardRepository;
import uz.mediasolutions.jurabeklabbackend.repository.TransactionRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.TransactionService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;

@Service("userTransactionService")
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Override
    public ResponseEntity<?> getInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        InfoDTO balance = transactionRepository.getUserBalance(user.getId());
        return ResponseEntity.ok(balance);
    }

    @Override
    public ResponseEntity<List<?>> getCards() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CardDTO> cards = transactionRepository.getCardsByUserId(user.getId());
        return ResponseEntity.ok(cards);
    }

    @Override
    public ResponseEntity<Page<?>> getHistory(int page, int size, String type) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<TransactionHistoryDTO> transactionHistory = transactionRepository.getTransactionHistory(user.getId(), type, PageRequest.of(page, size));
        return ResponseEntity.ok(transactionHistory);
    }

    @Override
    public ResponseEntity<?> addCard(CardReqDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Card card = Card.builder()
                .name(dto.getName())
                .number(dto.getCardNumber())
                .user(user)
                .build();
        cardRepository.save(card);
        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }

    @Override
    public ResponseEntity<?> withdraw(WithdrawReqDTO dto) {
        Card card = cardRepository.findById(dto.getCardId()).orElseThrow(
                () -> RestException.restThrow("Card not found", HttpStatus.NOT_FOUND)
        );
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .amount(dto.getAmount())
                .card(card)
                .user(user)
                .status(TransactionStatus.WAITING)
                .build();
        transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }
}
