package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Pharmacy2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.PharmacyDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    boolean existsByIdAndDeletedFalse(Long id);

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.address,\n" +
            "       d.name as district,\n" +
            "       r.name as region\n" +
            "FROM pharmacies p\n" +
            "         LEFT JOIN districts d ON d.id = p.district_id\n" +
            "         LEFT JOIN regions r ON r.id = d.region_id\n" +
            "WHERE (:search IS NULL OR p.name ILIKE '%' || :search || '%'\n" +
            "    OR p.address ILIKE '%' || :search || '%'\n" +
            "    OR d.name ILIKE '%' || :search || '%'\n" +
            "    OR r.name ILIKE '%' || :search || '%')\n" +
            "  AND p.deleted = false\n" +
            "ORDER BY p.name", nativeQuery = true)
    Page<PharmacyDTO> findAllWithSearch(Pageable pageable,
                                        @Param("search") String search);

    List<Pharmacy> findAllByDeletedFalse();

    boolean existsByNameAndAddressAndDeletedFalse(String name, String address);

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.address,\n" +
            "       p.inn\n" +
            "FROM pharmacies p\n" +
            "WHERE p.district_id = :districtId\n" +
            "  AND p.deleted = false\n" +
            "  AND (:search IS NULL OR p.name ILIKE '%' || :search || '%')\n" +
            "ORDER BY p.name", nativeQuery = true)
    List<Pharmacy2DTO> findAllByDistrictId(@Param("districtId") Long districtId,
                                           @Param("search") String search);

    Optional<Pharmacy> findByIdAndDeletedFalse(Long id);

    List<Pharmacy> findByIdInAndDeletedFalse(Set<Long> pharmacyIds);
}
