package uz.mediasolutions.jurabeklabbackend.controller.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.controller.admin.abs.PharmacyController;
import uz.mediasolutions.jurabeklabbackend.payload.req.PharmacyReqDTO;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.PharmacyService;

@RestController("adminPharmacyController")
@RequiredArgsConstructor
public class PharmacyControllerImpl implements PharmacyController {

    private final PharmacyService service;

    @Override
    public ResponseEntity<Page<?>> getAll(int page, int size, String search) {
        return service.getAll(page, size, search);
    }

    @Override
    public ResponseEntity<?> addByFile(MultipartFile file) {
        return service.addByFile(file);
    }

//    @Override
//    public ResponseEntity<?> add(PharmacyReqDTO dto) {
//        return service.add(dto);
//    }
//
//    @Override
//    public ResponseEntity<?> edit(Long id, PharmacyReqDTO dto) {
//        return service.edit(id, dto);
//    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        return service.delete(id);
    }
}
