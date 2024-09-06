package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.PharmacyDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.PharmacyReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.PharmacyService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;

    @Override
    public ResponseEntity<Page<?>> getAll(int page, int size, String search) {
        Page<PharmacyDTO> pharmacyDTOS = pharmacyRepository.findAllWithSearch(PageRequest.of(page, size), search);
        return ResponseEntity.ok(pharmacyDTOS);
    }

    @Override
    public ResponseEntity<?> add(PharmacyReqDTO dto) {
        Pharmacy.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }

    @Override
    public ResponseEntity<?> edit(Long id, PharmacyReqDTO dto) {
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElseThrow(
                () -> RestException.restThrow("Pharmacy not found", HttpStatus.NOT_FOUND)
        );

        Optional.ofNullable(dto.getName()).ifPresent(pharmacy::setName);
        Optional.ofNullable(dto.getAddress()).ifPresent(pharmacy::setAddress);
        pharmacyRepository.save(pharmacy);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        if (!pharmacyRepository.existsById(id)) {
            throw RestException.restThrow("Pharmacy not found", HttpStatus.NOT_FOUND);
        }

        try {
            pharmacyRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
        } catch (Exception e) {
            throw RestException.restThrow("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
