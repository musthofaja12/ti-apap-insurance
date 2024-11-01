package apap.ti.insurance2206082354.restservice;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206082354.restdto.response.*;
import apap.ti.insurance2206082354.service.CompanyService;
import apap.ti.insurance2206082354.service.PatientService;
import apap.ti.insurance2206082354.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PolicyRestServiceImpl implements PolicyRestService {

    @Autowired
    private PolicyService policyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PatientService patientService;

    @Override
    public PolicyStatisticsResponseDTO getPolicyStatistics(String period, String year) {
        List<String> newPeriod = new ArrayList<String>();
        List<Integer> newValues = new ArrayList<Integer>();
        List<Policy> policyList = policyService.getAllPolicy();

        if (period.equals("monthly")) {
            newPeriod.add("January");
            newPeriod.add("February");
            newPeriod.add("March");
            newPeriod.add("April");
            newPeriod.add("May");
            newPeriod.add("June");
            newPeriod.add("July");
            newPeriod.add("August");
            newPeriod.add("September");
            newPeriod.add("October");
            newPeriod.add("November");
            newPeriod.add("December");

            int totalJanuary = 0;
            int totalFebruary = 0;
            int totalMarch = 0;
            int totalApril = 0;
            int totalMay = 0;
            int totalJune = 0;
            int totalJuly = 0;
            int totalAugust = 0;
            int totalSeptember = 0;
            int totalOctober = 0;
            int totalNovember = 0;
            int totalDecember = 0;

            for (Policy policy : policyList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(policy.getCreatedAt());

                if (calendar.get(Calendar.YEAR) != Integer.parseInt(year)) {
                    continue;
                }

                switch (calendar.get(Calendar.MONTH)) {
                    case 0:
                        totalJanuary++;
                        continue;
                    case 1:
                        totalFebruary++;
                        continue;
                    case 2:
                        totalMarch++;
                        continue;
                    case 3:
                        totalApril++;
                        continue;
                    case 4:
                        totalMay++;
                        continue;
                    case 5:
                        totalJune++;
                        continue;
                    case 6:
                        totalJuly++;
                        continue;
                    case 7:
                        totalAugust++;
                        continue;
                    case 8:
                        totalSeptember++;
                        continue;
                    case 9:
                        totalOctober++;
                        continue;
                    case 10:
                        totalNovember++;
                        continue;
                    case 11:
                        totalDecember++;
                        continue;
                }
            }
            newValues.add(totalJanuary);
            newValues.add(totalFebruary);
            newValues.add(totalMarch);
            newValues.add(totalApril);
            newValues.add(totalMay);
            newValues.add(totalJune);
            newValues.add(totalJuly);
            newValues.add(totalAugust);
            newValues.add(totalSeptember);
            newValues.add(totalOctober);
            newValues.add(totalNovember);
            newValues.add(totalDecember);

        } else {
            newPeriod.add("Q1");
            newPeriod.add("Q2");
            newPeriod.add("Q3");
            newPeriod.add("Q4");

            int totalQ1 = 0;
            int totalQ2 = 0;
            int totalQ3 = 0;
            int totalQ4 = 0;

            for (Policy policy : policyList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(policy.getCreatedAt());

                if (calendar.get(Calendar.YEAR) != Integer.parseInt(year)) {
                    continue;
                }

                if (calendar.get(Calendar.MONTH) < 3) {
                    totalQ1++;
                }
                else if (calendar.get(Calendar.MONTH) < 6) {
                    totalQ2++;
                }
                else if (calendar.get(Calendar.MONTH) < 9) {
                    totalQ3++;
                }
                else if (calendar.get(Calendar.MONTH) < 12) {
                    totalQ4++;
                }
            }

            newValues.add(totalQ1);
            newValues.add(totalQ2);
            newValues.add(totalQ3);
            newValues.add(totalQ4);
        }

        PolicyStatisticsResponseDTO policyStatisticsResponseDTO = new PolicyStatisticsResponseDTO();
        policyStatisticsResponseDTO.setLabels(newPeriod);
        policyStatisticsResponseDTO.setValues(newValues);
        return policyStatisticsResponseDTO;
    }

    @Override
    public PolicyResponseDTO getPolicyById(String idPolicy) {
        Policy policy = policyService.getPolicyById(idPolicy);

        if (policy != null) {
            return policyToPolicyResponseDTO(policy);
        }

        return null;
    }

    @Override
    public PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO addPolicyRequestRestDTO) {
        Policy policy = new Policy();

        Patient patientToAdd = patientService.getPatientByNIK(addPolicyRequestRestDTO.getNik());
        Company companyToAdd = companyService.getCompanyById(addPolicyRequestRestDTO.getCompany());

        policy.setId(policyService.makePolicyId(patientToAdd.getName(), companyToAdd.getName()));
        policy.setCompany(companyToAdd);
        policy.setPatient(patientToAdd);
        policy.setExpiryDate(addPolicyRequestRestDTO.getExpiryDate());
        policy.setTotalCoverage(companyToAdd.getTotalCoverage());
        policy.setTotalCovered(0);
        policy.setCreatedAt(new Date());
        policy.setUpdatedAt(new Date());
        policy.setStatus();

        if (patientToAdd.getAvailableLimit() < policy.getTotalCoverage()) {
            return null;
        }

        return policyToPolicyResponseDTO(policyService.addPolicy(policy));
    }

    private PolicyResponseDTO policyToPolicyResponseDTO(Policy policy) {
        PolicyResponseDTO policyResponseDTO = new PolicyResponseDTO();

        policyResponseDTO.setId(policy.getId());

        PatientResponseDTO patientResponseDTO = patientToPatientResponseDTO(policy.getPatient());
        policyResponseDTO.setPatient(patientResponseDTO);

        CompanyResponseDTO companyResponseDTO = companyToCompanyResponseDTO(policy.getCompany());
        policyResponseDTO.setCompany(companyResponseDTO);

        policyResponseDTO.setStatus(policy.getStatus());
        policyResponseDTO.setExpiryDate(policy.getExpiryDate());
        policyResponseDTO.setTotalCoverage(policy.getTotalCoverage());
        policyResponseDTO.setTotalCovered(policy.getTotalCovered());
        policyResponseDTO.setCreatedAt(policy.getCreatedAt());
        policyResponseDTO.setUpdatedAt(policy.getUpdatedAt());

        return policyResponseDTO;
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
        patientResponseDTO.setCreatedAt(patient.getCreatedAt());
        patientResponseDTO.setUpdatedAt(patient.getUpdatedAt());

        return patientResponseDTO;
    }

    private CompanyResponseDTO companyToCompanyResponseDTO(Company company) {
        CompanyResponseDTO companyResponseDTO = new CompanyResponseDTO();

        companyResponseDTO.setId(company.getId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setContact(company.getContact());
        companyResponseDTO.setAddress(company.getAddress());
        companyResponseDTO.setEmail(company.getEmail());

        companyResponseDTO.setListCoverage(new ArrayList<CoverageResponseDTO>());
        for (Coverage coverage : company.getListCoverage()) {
            CoverageResponseDTO coverageResponseDTO = coverageToCoverageResponseDTO(coverage);

            companyResponseDTO.getListCoverage().add(coverageResponseDTO);
        }

        companyResponseDTO.setCreatedAt(company.getCreatedAt());
        companyResponseDTO.setUpdatedAt(company.getUpdatedAt());
        companyResponseDTO.setDeletedAt(company.getDeletedAt());

        return companyResponseDTO;
    }

    private CoverageResponseDTO coverageToCoverageResponseDTO(Coverage coverage) {
        CoverageResponseDTO coverageResponseDTO = new CoverageResponseDTO();

        coverageResponseDTO.setId(coverage.getId());
        coverageResponseDTO.setName(coverage.getName());
        coverageResponseDTO.setCoverageAmount(coverage.getCoverageAmount());

        coverageResponseDTO.setListCompany(new ArrayList<UUID>());
        for (Company company : coverage.getListCompany()) {
            coverageResponseDTO.getListCompany().add(company.getId());
        }
        coverageResponseDTO.setCreatedAt(coverage.getCreatedAt());
        coverageResponseDTO.setUpdatedAt(coverage.getUpdatedAt());
        return coverageResponseDTO;
    }
}
