package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.jurabeklabbackend.entity.Card;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
