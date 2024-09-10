package uz.mediasolutions.jurabeklabbackend.controller.common.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;

@RequestMapping(Rest.BASE_PATH + "transaction-type")
public interface TransactionTypeController {

    @GetMapping
    ResponseEntity<List<TransactionType>> getTransactionTypes();

}
