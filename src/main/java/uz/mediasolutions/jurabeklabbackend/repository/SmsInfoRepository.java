package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.jurabeklabbackend.entity.SmsInfo;

public interface SmsInfoRepository extends JpaRepository<SmsInfo, Long> {
}
