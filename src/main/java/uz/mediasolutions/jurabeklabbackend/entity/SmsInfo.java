package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "sms_infos")
@Builder
public class SmsInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private String messageId;

    private String userSmsId;

    private String country;

    private String phoneNumber;

    private Integer smsCount;

    private String status;

    private Timestamp statusDate;

}
