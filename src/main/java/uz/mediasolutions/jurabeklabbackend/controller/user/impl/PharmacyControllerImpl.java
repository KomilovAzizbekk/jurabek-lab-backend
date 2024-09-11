package uz.mediasolutions.jurabeklabbackend.controller.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uz.mediasolutions.jurabeklabbackend.controller.user.abs.PharmacyController;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.PharmacyService;

import java.util.List;

@RestController("userPharmacyController")
@RequiredArgsConstructor
public class PharmacyControllerImpl implements PharmacyController {

    private final PharmacyService service;

    @Override
    public ResponseEntity<List<Region>> getAllRegions() {
        return service.getAllRegions();
    }

    @Override
    public ResponseEntity<List<?>> getAllDistricts(Long regionId) {
        return service.getAllDistricts(regionId);
    }

    @Override
    public ResponseEntity<List<?>> getAllPharmacies(Long districtId, String search) {
        return service.getAllPharmacies(districtId, search);
    }
}
