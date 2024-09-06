package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.District;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.DistrictDTO;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    boolean existsByName(String name);

    District findByName(String name);

    @Query(value = "SELECT d.id,\n" +
            "       d.name\n" +
            "FROM districts d\n" +
            "WHERE d.region_id = :regionId\n" +
            "ORDER BY d.name", nativeQuery = true)
    List<DistrictDTO> findAllDistrictsByRegionId(@Param("regionId") Long regionId);

}
