package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.payload.req.ConstantsDTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/constants")
public interface ConstantsController {

    @GetMapping("/get")
    ResponseEntity<?> getConstants();

    @PatchMapping("/edit")
    ResponseEntity<?> editConstants(@RequestBody ConstantsDTO dto);

}
