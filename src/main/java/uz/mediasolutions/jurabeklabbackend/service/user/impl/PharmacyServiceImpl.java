package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.District;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.DistrictDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Pharmacy2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.PharmacyReqDTO;
import uz.mediasolutions.jurabeklabbackend.repository.DistrictRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.repository.RegionRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.PharmacyService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

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
    public ResponseEntity<List<?>> getAllPharmacies(Long districtId, String search) {
        List<Pharmacy2DTO> pharmacies = pharmacyRepository.findAllByDistrictId(districtId, search);
        return ResponseEntity.ok(pharmacies);
    }

    @Override
    public ResponseEntity<?> addPharmacy(PharmacyReqDTO dto) {

        District district = districtRepository.findById(dto.getDistrictId()).orElseThrow(
                () -> RestException.restThrow("District not found", HttpStatus.NOT_FOUND)
        );

        Pharmacy pharmacy = Pharmacy.builder()
                .name(dto.getName())
                .district(district)
                .inn(dto.getInn())
                .deleted(false)
                .address(dto.getAddress())
                .build();
        pharmacyRepository.save(pharmacy);

        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }
}
