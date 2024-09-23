package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mediasolutions.jurabeklabbackend.entity.SmsToken;

public interface SmsTokenRepository extends JpaRepository<SmsToken, Long> {

    @Query(value = "SELECT count(st.id) FROM sms_token st", nativeQuery = true)
    int countSmsToken();

}
