package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.CardDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.InfoDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.TrRequestDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.TransactionHistoryDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT u.balance FROM users u WHERE u.id = :userId", nativeQuery = true)
    InfoDTO getUserBalance(@Param("userId") UUID userId);

    @Query(value = "SELECT c.id,\n" +
            "       c.name,\n" +
            "       c.number\n" +
            "FROM cards c\n" +
            "         LEFT JOIN public.users u ON u.id = c.user_id\n" +
            "WHERE u.id = :userId" +
            "  AND c.deleted = false", nativeQuery = true)
    List<CardDTO> getCardsByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT t.amount,\n" +
            "       t.type,\n" +
            "       t.status,\n" +
            "       t.number,\n" +
            "       c.name                                                               as cardName,\n" +
            "       CASE WHEN c.number IS NOT NULL THEN RIGHT(c.number, 4) ELSE NULL END as cardNumber,\n" +
            "       p.name                                                               as pharmacy,\n" +
            "       t.updated_at                                                         as updatedTime,\n" +
            "       t.created_at                                                         as createdTime\n" +
            "FROM transactions t\n" +
            "         LEFT JOIN cards c ON c.id = t.card_id\n" +
            "         LEFT JOIN pharmacies p ON p.id = t.pharmacy_id\n" +
            "WHERE t.user_id = :userId\n" +
            "  AND (:type IS NULL\n" +
            "    OR t.type = :type)\n" +
            "ORDER BY t.updated_at DESC", nativeQuery = true)
    Page<TransactionHistoryDTO> getTransactionHistory(@Param("userId") UUID userId,
                                                      @Param("type") String type,
                                                      Pageable pageable);

    @Query(value = """
            SELECT t.id,
                   c.number       as card,
                   t.amount,
                   t.number,
                   t.status,
                   u.phone_number as phoneNumber
            FROM transactions t
                     LEFT JOIN cards c ON c.id = t.card_id
                     LEFT JOIN users u ON u.id = t.user_id
            WHERE (:status IS NULL OR t.status = :status)
              AND t.type = 'WITHDRAWAL'
            ORDER BY t.updated_at DESC
            """, nativeQuery = true)
    Page<TrRequestDTO> getTransactionRequests(@Param("status") String status,
                                              Pageable pageable);

    @Query(value = """
            SELECT coalesce(sum(t.amount), 0) as amount
            FROM transactions t
            WHERE t.user_id = :user_id
              AND t.status = 'WAITING'
            """, nativeQuery = true)
    Integer getTransactionSum(@Param("user_id") UUID userId);

    Transaction findDistinctFirstByStatusAndUserIdAndCardId(TransactionStatus status, UUID userId, UUID cardId);

}
