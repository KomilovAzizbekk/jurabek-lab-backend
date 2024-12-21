package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.ConstantsController;
import uz.mediasolutions.jurabeklabbackend.entity.Constants;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.req.ConstantsDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ConstantsRepository;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ConstantsControllerImpl implements ConstantsController {

    private final ConstantsRepository constantsRepository;

    @Override
    public ResponseEntity<?> getConstants() {
        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Constants not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.ok(constants);
    }

    @Override
    public ResponseEntity<?> editConstants(ConstantsDTO dto) {

        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Constants not found", HttpStatus.NOT_FOUND)
        );

        Optional.ofNullable(dto.getCashbackPercent()).ifPresent(constants::setCashbackPercent);
        Optional.ofNullable(dto.getProductPercent()).ifPresent(constants::setProductPercent);
        Optional.ofNullable(dto.getAndroidVersion()).ifPresent(constants::setAndroidVersion);
        Optional.ofNullable(dto.getIosVersion()).ifPresent(constants::setIosVersion);
        Optional.ofNullable(dto.getIosUrl()).ifPresent(constants::setIosUrl);
        Optional.ofNullable(dto.getAndroidUrl()).ifPresent(constants::setAndroidUrl);
        Optional.ofNullable(dto.getMinOrderPrice()).ifPresent(constants::setMinOrderPrice);
        constantsRepository.save(constants);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }
}
