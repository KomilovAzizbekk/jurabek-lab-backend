package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
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
            // Faylni qayta ishlash
            saveDataFromExcel(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void saveDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            Set<String> processedNamesAndAddresses = new HashSet<>();  // Excel va bazadagi name'larni to'plovchi Set
            List<Pharmacy> pharmaciesToSave = new ArrayList<>(); // Bazaga saqlash uchun ro'yxat

            // Har bir qatorni qayta ishlash
            for (Row row : sheet) {
                if (row.getRowNum() != 0) {  // Birinchi qatorni o'tkazib yuboramiz (header)
                    String address = getCellValue(row, 3);  // `address` qiymati 3-ustunda deb qabul qildik
                    String name = getCellValue(row, 4);  // `name` qiymati 4-ustunda deb qabul qildik
                    String combine = name.trim() + " " + address.trim();

                    // Bazada yoki Setda mavjud bo'lmagan name'larni qayta ishlaymiz
                    if (!processedNamesAndAddresses.contains(combine) &&
                            !pharmacyRepository.existsByNameAndAddressAndDeletedFalse(name, address)) {
                        processedNamesAndAddresses.add(combine);
                        Pharmacy pharmacy = processRow(row);  // Qatorni qayta ishlash
                        pharmaciesToSave.add(pharmacy);  // Bazaga saqlash uchun ro'yxatga qo'shamiz
                    }
                }
            }

            // Batch tarzida bazaga saqlaymiz (agar ro'yxat bo'sh bo'lmasa)
            if (!pharmaciesToSave.isEmpty()) {
                pharmacyRepository.saveAll(pharmaciesToSave);
            }

        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Har bir qatorni qayta ishlash uchun yordamchi method
    private Pharmacy processRow(Row row) {
        String regionName = getCellValue(row, 1);  // Region 1-ustun
        String districtName = getCellValue(row, 2);  // District 2-ustun
        String address = getCellValue(row, 3);  // Address 3-ustun
        String name = getCellValue(row, 4);  // Name 4-ustun

        // Region va District obyektlarini bazadan yoki keshdan olish
        Region savedRegion = getRegion(regionName);
        District savedDistrict = getDistrict(districtName, savedRegion);

        // Yangi Pharmacy obyektini yaratish va qaytarish
        return Pharmacy.builder()
                .name(name)
                .district(savedDistrict)
                .address(address)
                .deleted(false)
                .build();
    }

    // Regionni bazadan yoki keshdan olish funksiyasi
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

    // Districtni bazadan yoki keshdan olish funksiyasi
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

    // Excel fayldagi hujayralarni o'qish funksiyasi
    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString().trim();  // Bo'sh joylarni olib tashlaymiz
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
