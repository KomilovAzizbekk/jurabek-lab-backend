package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Transaction;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.CardDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.InfoDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT u.balance FROM users u WHERE u.id = :userId", nativeQuery = true)
    InfoDTO getUserBalance(@Param("userId") UUID userId);

    @Query(value = "SELECT c.name,\n" +
            "       c.number\n" +
            "FROM cards c\n" +
            "         LEFT JOIN public.users u ON u.id = c.user_id\n" +
            "WHERE u.id = :userId", nativeQuery = true)
    List<CardDTO> getCardsByUserId(@Param("userId") UUID userId);

//    Page<> getTransactionHistory(@Param("userId") UUID userId, Pageable pageable);

}
