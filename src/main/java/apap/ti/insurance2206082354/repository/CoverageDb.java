package apap.ti.insurance2206082354.repository;

import apap.ti.insurance2206082354.model.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoverageDb extends JpaRepository<Coverage, Long> {
    List<Coverage> findAll();
}
