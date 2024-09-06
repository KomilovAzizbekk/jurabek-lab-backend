package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Pharmacy;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.PharmacyDTO;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    boolean existsById(Long id);

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.address\n" +
            "FROM pharmacies p\n" +
            "WHERE (:search IS NULL OR p.name ILIKE '%' || :search || '%'\n" +
            "    OR p.address ILIKE '%' || :search || '%')\n" +
            "ORDER BY p.name", nativeQuery = true)
    Page<PharmacyDTO> findAllWithSearch(Pageable pageable,
                                        @Param("search") String search);

}
