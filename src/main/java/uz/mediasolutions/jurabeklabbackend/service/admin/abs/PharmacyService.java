package uz.mediasolutions.jurabeklabbackend.service.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.payload.req.PharmacyReqDTO;

public interface PharmacyService {

    ResponseEntity<Page<?>> getAll(int page, int size, String search);

    ResponseEntity<?> add(PharmacyReqDTO dto);

    ResponseEntity<?> edit(Long id, PharmacyReqDTO dto);

    ResponseEntity<?> delete(Long id);
}
