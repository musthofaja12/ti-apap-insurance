package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.CoverageDb;
import apap.ti.insurance2206082354.repository.PatientDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDb patientDb;

    @Override
    public List<Patient> getAllPatient() {
        return patientDb.findAll();
    }

    @Override
    public Patient addPatient(Patient patient) {
        if (patientDb.findPatientByNik(patient.getNik()) != null) {
            return null;
        }
        if (patient.getAvailableLimit() < 0) {
            return null;
        }
        return patientDb.save(patient);
    }

    @Override
    public Patient getPatientByNIK(String nik) {
        return patientDb.findPatientByNik(nik);
    }

    @Override
    public String makePatientNIK(){
        Random rand = new Random();
        String nik = "";
        List<String> listNik = new ArrayList<String>();
        boolean isNikUnique= false;

        for (Patient patientExisted: getAllPatient()) {
            listNik.add(patientExisted.getNik());
        }
        while(isNikUnique == false) {
            String kodeWilayah = String.format("%06d", rand.nextInt(999999));
            String tanggal = String.format("%02d", rand.nextInt(31) + 1);
            String bulan = String.format("%02d", rand.nextInt(12) + 1);
            String tahun = String.format("%02d", rand.nextInt(100));
            String nomorUrut = String.format("%04d", rand.nextInt(10000)); //
            String beforeChecksum = kodeWilayah + tanggal + bulan + tahun + nomorUrut;

            int sum = 0;
            for (int i = 0; i < beforeChecksum.length(); i++) {
                int digit = Character.getNumericValue(beforeChecksum.charAt(i));
                sum += digit * (i + 1);
            }
            sum %= 10;
            nik = beforeChecksum + sum;

            if (!listNik.contains(nik)) {
                isNikUnique = true;
            }
        }
        return nik;
    }

    @Override
    public Date formatDateFromForm(String date) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public Patient updateClassPatient(Patient patient) {
        Patient patientToUpdate = patientDb.findPatientByNik(patient.getNik());

        if (patientToUpdate != null) {
            patientToUpdate.setPClass(patient.getPClass());
            patientToUpdate.setUpdatedAt(patient.getUpdatedAt());
            return patientDb.save(patientToUpdate);
        }

        return null;
    }

    @Override
    public Patient getPatientById(UUID idPatient) {
        return patientDb.findById(idPatient).orElse(null);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Patient patientToUpdate = patientDb.findPatientByNik(patient.getNik());

        if (patientToUpdate != null) {
            patientToUpdate.setPClass(patient.getPClass());
            patientToUpdate.setUpdatedAt(patient.getUpdatedAt());
            patientToUpdate.setGender(patient.getGender());
            patientToUpdate.setName(patient.getName());
            patientToUpdate.setListPolicy(patient.getListPolicy());
            patientToUpdate.setBirthDate(patient.getBirthDate());
            patientToUpdate.setNik(patient.getNik());
            patientToUpdate.setEmail(patient.getEmail());

            return patientDb.save(patientToUpdate);
        }

        return null;
    }
}