package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.jurabeklabbackend.entity.Region;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {

    boolean existsByName(String name);

    Region findByName(String name);

    List<Region> findAllByOrderByNameAsc();

}
