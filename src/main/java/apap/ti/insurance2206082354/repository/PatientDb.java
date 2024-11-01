package apap.ti.insurance2206082354.repository;

import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    List<Patient> findAll();

    Patient findPatientByNik(String nik);
}
