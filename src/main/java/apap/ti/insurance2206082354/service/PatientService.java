package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    Patient addPatient(Patient patient);

    List<Patient> getAllPatient();

    Patient getPatientByNIK(String nik);

    String makePatientNIK();

    Date formatDateFromForm(String date);

    Patient updateClassPatient(Patient patient);

    Patient getPatientById(UUID patientId);

    Patient updatePatient(Patient patient);
} 

    
