package apap.ti.insurance2206082354.restservice;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.CompanyDb;
import apap.ti.insurance2206082354.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206082354.restdto.response.CoverageResponseDTO;
import apap.ti.insurance2206082354.restdto.response.PatientResponseDTO;
import apap.ti.insurance2206082354.restdto.response.PolicyResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyRestServiceImpl implements CompanyRestService {

    @Autowired
    private CompanyDb companyDb;

    @Override
    public List<CompanyResponseDTO> getAllCompany() {
        List<Company> companyList = companyDb.findAll();

        List<CompanyResponseDTO> companyResponseDTOList = new ArrayList<>();
        for (Company company : companyList) {
            companyResponseDTOList.add(companyToCompanyResponseDTO(company));
        }

        return companyResponseDTOList;
    }

    @Override
    public CompanyResponseDTO getCompanyById(UUID id) {
        Company company = companyDb.findById(id).orElse(null);

        if (company != null) {
            return companyToCompanyResponseDTO(company);
        }
        return null;
    }

    private CompanyResponseDTO companyToCompanyResponseDTO(Company company) {
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();

        companyResponseDTO.setId(company.getId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setContact(company.getContact());
        companyResponseDTO.setAddress(company.getAddress());
        companyResponseDTO.setEmail(company.getEmail());

        companyResponseDTO.setListPolicy(new ArrayList<PolicyResponseDTO>());
        for (Policy policy : company.getListPolicy()) {
            PolicyResponseDTO policyResponseDTO = policyToPolicyResponseDTO(policy);

//            policyResponseDTO.setCompany(companyResponseDTO);
            companyResponseDTO.getListPolicy().add(policyResponseDTO);
        }

        companyResponseDTO.setListCoverage(new ArrayList<CoverageResponseDTO>());
        for (Coverage coverage : company.getListCoverage()) {
            CoverageResponseDTO coverageResponseDTO = coverageToCoverageResponseDTO(coverage);

//            coverageResponseDTO.getListCompany().add(companyResponseDTO);
            companyResponseDTO.getListCoverage().add(coverageResponseDTO);
        }

        companyResponseDTO.setCreatedAt(company.getCreatedAt());
        companyResponseDTO.setUpdatedAt(company.getUpdatedAt());
        companyResponseDTO.setDeletedAt(company.getDeletedAt());

        return companyResponseDTO;
    }

    private PolicyResponseDTO policyToPolicyResponseDTO(Policy policy) {
        PolicyResponseDTO policyResponseDTO = new PolicyResponseDTO();

        policyResponseDTO.setId(policy.getId());

//        PatientResponseDTO patientResponseDTO = patientToPatientResponseDTO(policy.getPatient());
//        patientResponseDTO.getListPolicy().add(policyResponseDTO);
//        policyResponseDTO.setPatient(patientResponseDTO);

        policyResponseDTO.setStatus(policy.getStatus());
        policyResponseDTO.setExpiryDate(policy.getExpiryDate());
        policyResponseDTO.setTotalCoverage(policy.getTotalCoverage());
        policyResponseDTO.setTotalCovered(policy.getTotalCovered());
        policyResponseDTO.setCreatedAt(policy.getCreatedAt());
        policyResponseDTO.setUpdatedAt(policy.getUpdatedAt());

        return policyResponseDTO;
    }

    private CoverageResponseDTO coverageToCoverageResponseDTO(Coverage coverage) {
        CoverageResponseDTO coverageResponseDTO = new CoverageResponseDTO();

        coverageResponseDTO.setId(coverage.getId());
        coverageResponseDTO.setName(coverage.getName());
        coverageResponseDTO.setCoverageAmount(coverage.getCoverageAmount());

        coverageResponseDTO.setListCompany(new ArrayList<UUID>());
        for(Company company : coverage.getListCompany()) {
            coverageResponseDTO.getListCompany().add(company.getId());
        }

        coverageResponseDTO.setCreatedAt(coverage.getCreatedAt());
        coverageResponseDTO.setUpdatedAt(coverage.getUpdatedAt());
        return coverageResponseDTO;
    }

    private PatientResponseDTO patientToPatientResponseDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();

        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setNik(patient.getNik());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setGender(patient.getGender());
        patientResponseDTO.setBirthDate(patient.getBirthDate());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setPClass(patient.getPClass());
//        patientResponseDTO.setListPolicy(new ArrayList<PolicyResponseDTO>());
        patientResponseDTO.setCreatedAt(patient.getCreatedAt());
        patientResponseDTO.setUpdatedAt(patient.getUpdatedAt());

        return patientResponseDTO;
    }
}
