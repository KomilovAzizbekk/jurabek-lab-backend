package uz.mediasolutions.jurabeklabbackend.controller.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.controller.common.abs.FileController;
import uz.mediasolutions.jurabeklabbackend.service.common.abs.FileService;

@RestController
@RequiredArgsConstructor
public class FileControllerImpl implements FileController {

    private final FileService service;

    @Override
    public ResponseEntity<?> uploadFile(MultipartFile file) {
        return service.uploadFile(file);
    }

    @Override
    public ResponseEntity<?> getFile(String filename) {
        return service.getFile(filename);
    }
}
