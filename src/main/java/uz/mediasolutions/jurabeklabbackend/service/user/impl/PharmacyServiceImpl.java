package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.DistrictDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Pharmacy2DTO;
import uz.mediasolutions.jurabeklabbackend.repository.DistrictRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.repository.RegionRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.PharmacyService;

import java.util.List;

@Service("userPharmacyService")
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;

    @Override
    public ResponseEntity<List<Region>> getAllRegions() {
        List<Region> regions = regionRepository.findAllByOrderByNameAsc();
        return ResponseEntity.ok(regions);
    }

    @Override
    public ResponseEntity<List<?>> getAllDistricts(Long regionId) {
        List<DistrictDTO> districts = districtRepository.findAllDistrictsByRegionId(regionId);
        return ResponseEntity.ok(districts);
    }

    @Override
    public ResponseEntity<List<?>> getAllPharmacies(Long districtId) {
        List<Pharmacy2DTO> pharmacies = pharmacyRepository.findAllByDistrictId(districtId);
        return ResponseEntity.ok(pharmacies);
    }
}
