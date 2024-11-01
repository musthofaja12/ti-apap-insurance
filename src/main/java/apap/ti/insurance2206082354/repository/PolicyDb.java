package apap.ti.insurance2206082354.repository;

import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyDb extends JpaRepository<Policy, String> {

    List<Policy> findAll();

    List<Policy> findByStatusAndTotalCoverageBetween(int status, Long min, Long max);

    List<Policy> findByTotalCoverageBetween(Long min,Long max);
}
