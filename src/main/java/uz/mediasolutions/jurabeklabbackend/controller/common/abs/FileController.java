package uz.mediasolutions.jurabeklabbackend.controller.common.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "files")
public interface FileController {

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file);

    @GetMapping("/get/{filename}")
    ResponseEntity<?> getFile(@PathVariable String filename);

}
