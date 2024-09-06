package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.entity.District;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.PharmacyDTO;
import uz.mediasolutions.jurabeklabbackend.repository.DistrictRepository;
import uz.mediasolutions.jurabeklabbackend.repository.PharmacyRepository;
import uz.mediasolutions.jurabeklabbackend.repository.RegionRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.PharmacyService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.io.InputStream;
import java.util.*;

@Service("adminPharmacyService")
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;

    @Override
    public ResponseEntity<Page<?>> getAll(int page, int size, String search) {
        Page<PharmacyDTO> pharmacyDTOS = pharmacyRepository.findAllWithSearch(PageRequest.of(page, size), search);
        return ResponseEntity.ok(pharmacyDTOS);
    }

    @Override
    @Transactional
    public ResponseEntity<?> addByFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw RestException.restThrow("File cannot be empty", HttpStatus.BAD_REQUEST);
        }
        try {
            saveDataFromExcel(file.getInputStream());
        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
    }


    @Transactional
    protected void saveDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            List<Pharmacy> pharmacies = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() != 0) {

                    String regionName = getCellValue(row, 1);
                    String districtName = getCellValue(row, 2);

                    Region savedRegion;
                    District savedDistrict;

                    if (!regionRepository.existsByName(regionName)) {
                        Region region = Region.builder()
                                .name(regionName)
                                .build();
                        savedRegion = regionRepository.save(region);
                    } else {
                        savedRegion = regionRepository.findByName(regionName);
                    }

                    if (!districtRepository.existsByName(districtName)) {
                        District district = District.builder()
                                .name(districtName)
                                .region(savedRegion)
                                .build();
                        savedDistrict = districtRepository.save(district);
                    } else {
                        savedDistrict = districtRepository.findByName(districtName);
                    }

                    String address = getCellValue(row, 3);
                    String name = getCellValue(row, 4);

                    Pharmacy pharmacy = Pharmacy.builder()
                            .name(name)
                            .district(savedDistrict)
                            .address(address)
                            .build();

                    pharmacies.add(pharmacy);

                }
            }

            pharmacyRepository.saveAll(pharmacies);

        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString(); // Bu yerda turli xil formatlarni tekshirish va ularni qayta ishlash zarur bo'lishi mumkin
        }
        return ""; // Agar cell mavjud bo'lmasa, bo'sh string qaytarish
    }

//    @Override
//    public ResponseEntity<?> add(PharmacyReqDTO dto) {
//        Pharmacy.builder()
//                .name(dto.getName())
//                .address(dto.getAddress())
//                .build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
//    }
//
//    @Override
//    public ResponseEntity<?> edit(Long id, PharmacyReqDTO dto) {
//        Pharmacy pharmacy = pharmacyRepository.findById(id).orElseThrow(
//                () -> RestException.restThrow("Pharmacy not found", HttpStatus.NOT_FOUND)
//        );
//
//        Optional.ofNullable(dto.getName()).ifPresent(pharmacy::setName);
//        Optional.ofNullable(dto.getAddress()).ifPresent(pharmacy::setAddress);
//        pharmacyRepository.save(pharmacy);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
//    }
//
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
