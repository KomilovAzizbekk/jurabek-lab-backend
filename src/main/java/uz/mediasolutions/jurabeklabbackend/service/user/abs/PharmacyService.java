package uz.mediasolutions.jurabeklabbackend.service.user.abs;

import org.springframework.http.ResponseEntity;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.payload.req.PharmacyReqDTO;

import java.util.List;

public interface PharmacyService {

    ResponseEntity<List<Region>> getAllRegions();

    ResponseEntity<List<?>> getAllDistricts(Long regionId);

    ResponseEntity<List<?>> getAllPharmacies(Long districtId, String search);

    ResponseEntity<?> addPharmacy(PharmacyReqDTO dto);
}
