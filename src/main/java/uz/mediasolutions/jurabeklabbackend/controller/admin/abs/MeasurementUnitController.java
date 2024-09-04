package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@RequestMapping(Rest.BASE_PATH + "admin/measurement-unit")
public interface MeasurementUnitController {

    @GetMapping("/get-all")
    ResponseEntity<?> getAll(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                             @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size);

    @PostMapping("/add")
    ResponseEntity<?> add();

    @PatchMapping("/edit/{id}")
    ResponseEntity<?> edit(@PathVariable Long id);

}
