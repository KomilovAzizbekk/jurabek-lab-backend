package uz.mediasolutions.jurabeklabbackend.controller.admin.abs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.mediasolutions.jurabeklabbackend.entity.LanguagePs;
import uz.mediasolutions.jurabeklabbackend.manual.ApiResult;
import uz.mediasolutions.jurabeklabbackend.payload.req.TranslateDto;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(Rest.BASE_PATH + "admin/language")
public interface LanguageController {

    @GetMapping("/get-all")
    ApiResult<Page<LanguagePs>> getAllPageable(@RequestParam(defaultValue = Rest.DEFAULT_PAGE_NUMBER) int page,
                                               @RequestParam(defaultValue = Rest.DEFAULT_PAGE_SIZE) int size,
                                               @RequestParam(defaultValue = "null", required = false) String key);

    @GetMapping("/by-lang")
    ResponseEntity<Map<String, String>> getAllByLang(@RequestParam(defaultValue = "UZ") String language);

    @PostMapping("/create-edit")
    ApiResult<?> createTranslation(@RequestBody TranslateDto dto);

    @PostMapping("/create-with-key")
    ApiResult<?> createMainKey(@RequestBody List<TranslateDto> dtos);

    @PostMapping("/create-key")
    ApiResult<?> createKey(@RequestBody HashMap<String, String> dto);

}
