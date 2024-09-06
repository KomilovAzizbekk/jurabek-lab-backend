package uz.mediasolutions.jurabeklabbackend.controller.user.abs;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.mediasolutions.jurabeklabbackend.entity.Region;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.DistrictDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Pharmacy2DTO;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.util.List;

@RequestMapping(Rest.BASE_PATH + "app/pharmacy")
public interface PharmacyController {

    @GetMapping("/regions")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<List<Region>> getAllRegions();

    @GetMapping("/districts/{regionId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DistrictDTO.class))})
    })
    ResponseEntity<List<?>> getAllDistricts(@PathVariable Long regionId);

    @GetMapping("get/{districtId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pharmacy2DTO.class))})
    })
    ResponseEntity<List<?>> getAllPharmacies(@PathVariable Long districtId);

}
