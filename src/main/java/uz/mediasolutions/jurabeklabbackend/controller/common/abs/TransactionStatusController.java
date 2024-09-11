package uz.mediasolutions.jurabeklabbackend.controller.common.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;

@RequestMapping(Rest.BASE_PATH + "transaction-status")
public interface TransactionStatusController {

    @GetMapping
    ResponseEntity<List<TransactionStatus>> getTransactionStatuses();

}
