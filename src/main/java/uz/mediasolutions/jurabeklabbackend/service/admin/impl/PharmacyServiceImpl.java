package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service("adminPharmacyService")
@RequiredArgsConstructor
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;

    // Keshlar
    private final Map<String, Region> regionCache = new HashMap<>();
    private final Map<String, District> districtCache = new HashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // 4 ta oqim uchun

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
            // Asinxron tarzda faylni qayta ishlash
            saveDataFromExcelAsync(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Async
    @Transactional
    public void saveDataFromExcelAsync(InputStream is) {
        try {
            // Streaming uchun SXSSFWorkbook dan foydalaniladi
            Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(is));
            Sheet sheet = workbook.getSheetAt(0);

            List<Future<List<Pharmacy>>> futures = new ArrayList<>();

            // Har bir qatorni parallel qayta ishlash
            for (Row row : sheet) {
                if (row.getRowNum() != 0) {
                    Future<List<Pharmacy>> future = executorService.submit(() -> processRow(row));
                    futures.add(future);
                }
            }

            List<Pharmacy> allPharmacies = new ArrayList<>();
            for (Future<List<Pharmacy>> future : futures) {
                allPharmacies.addAll(future.get());  // Barcha parallel qayta ishlangan natijalarni bir joyga to'playmiz
            }

            // Batch tarzida saqlaymiz
            pharmacyRepository.saveAll(allPharmacies);

        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Har bir qatorni parallel qayta ishlash uchun yordamchi method
    private List<Pharmacy> processRow(Row row) {
        List<Pharmacy> pharmacies = new ArrayList<>();

        String regionName = getCellValue(row, 1);
        String districtName = getCellValue(row, 2);

        Region savedRegion = getRegion(regionName);
        District savedDistrict = getDistrict(districtName, savedRegion);

        String address = getCellValue(row, 3);
        String name = getCellValue(row, 4);

        if (!pharmacyRepository.existsByNameAndDeletedFalse(name)) {
            Pharmacy pharmacy = Pharmacy.builder()
                    .name(name)
                    .district(savedDistrict)
                    .address(address)
                    .deleted(false)
                    .build();

            pharmacies.add(pharmacy);
        }

        return pharmacies;
    }

    // Kesh orqali region olish
    private Region getRegion(String regionName) {
        if (regionCache.containsKey(regionName)) {
            return regionCache.get(regionName);
        }

        Region region;
        if (!regionRepository.existsByName(regionName)) {
            region = Region.builder().name(regionName).build();
            region = regionRepository.save(region);
        } else {
            region = regionRepository.findByName(regionName);
        }

        regionCache.put(regionName, region);  // Keshga qo'shamiz
        return region;
    }

    // Kesh orqali district olish
    private District getDistrict(String districtName, Region region) {
        if (districtCache.containsKey(districtName)) {
            return districtCache.get(districtName);
        }

        District district;
        if (!districtRepository.existsByName(districtName)) {
            district = District.builder().name(districtName).region(region).build();
            district = districtRepository.save(district);
        } else {
            district = districtRepository.findByName(districtName);
        }

        districtCache.put(districtName, district);  // Keshga qo'shamiz
        return district;
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString();
        }
        return "";
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
        Pharmacy pharmacy = pharmacyRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> RestException.restThrow("Pharmacy not found", HttpStatus.NOT_FOUND)
        );

        try {
            pharmacy.setDeleted(true);
            pharmacyRepository.save(pharmacy);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
        } catch (Exception e) {
            throw RestException.restThrow("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
