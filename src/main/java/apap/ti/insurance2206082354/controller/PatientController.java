package apap.ti.insurance2206082354.controller;

import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.request.AddPatientPolicyRequestDTO;
import apap.ti.insurance2206082354.request.UpgradePatientClassDTO;
import apap.ti.insurance2206082354.service.CompanyService;
import apap.ti.insurance2206082354.service.CoverageService;
import apap.ti.insurance2206082354.service.PatientService;
import apap.ti.insurance2206082354.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    CompanyService companyService;

    @Autowired
    PolicyService policyService;

    @Autowired
    PatientService patientService;

    @Autowired
    CoverageService coverageService;

    @GetMapping("/search")
    private String searchPatientForm(@RequestParam(name = "nik", defaultValue = "") String nik, Model model) {

        model.addAttribute("NIK", nik);

        return "form-search-patient";
    }

    @PostMapping("/search")
    private String searchPatient(@RequestParam(name = "nik", defaultValue = "") String nik, Model model) {
        Patient patient = patientService.getPatientByNIK(nik);

        if (patient == null) {
            model.addAttribute("nik", nik);
            return "not-found-patient";
        }
        model.addAttribute("patient", patient);
        return "found-patient";
    }

//    @GetMapping("/viewall")
//    private String listCompany(@RequestParam(name = "status", defaultValue = "") String status,
//                               @RequestParam(name = "from", defaultValue = "") String from,
//                               @RequestParam(name = "to", defaultValue = "") String to, Model model) {
//
//        model.addAttribute("listPolicy", policyService.getAllPolicyWithFilter(status, from, to));
//        model.addAttribute("status", status);
//        model.addAttribute("from", from);
//        model.addAttribute("to", to);
//
//        return "viewall-policy";
//    }
//
//    @GetMapping("/{idPolicy}")
//    private String viewPolicy(@PathVariable("idPolicy") String idPolicy, Model model) {
//        Policy policy = policyService.getPolicyById(idPolicy);
//        model.addAttribute("policy", policy);
//
//        return "view-policy";
//    }
//
    @GetMapping("/add")
    public String addPatientForm(Model model) {
        var patientPolicyDTO = new AddPatientPolicyRequestDTO();

        model.addAttribute("patientPolicyDTO", patientPolicyDTO);;
        model.addAttribute("listCompany", companyService.getAllCompany());

        return "form-add-patient-policy";
    }

    @PostMapping("/add")
    public String addPatient(@Valid @ModelAttribute AddPatientPolicyRequestDTO patientPolicyDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            String responseMessage = "Error: ";

            for (Object object : result.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    responseMessage += fieldError.getDefaultMessage().toString() + "\t";
                }
            }

            if (!responseMessage.equals("Error: ")) {
                model.addAttribute("responseMessage", responseMessage);

                return "response-patientpolicy";
            }
        }

        Patient patient = new Patient();

        patient.setName(patientPolicyDTO.getName());
        patient.setNik(patientPolicyDTO.getNik());
        patient.setEmail(patientPolicyDTO.getEmail());
        patient.setGender(Integer.parseInt(patientPolicyDTO.getGender()));
        patient.setBirthDate(patientService.formatDateFromForm(patientPolicyDTO.getBirthDate()));
        patient.setPClass(Integer.parseInt(patientPolicyDTO.getInsuranceClass()));
        patient.setCreatedAt(new Date());
        patient.setUpdatedAt(new Date());
        patient.setListPolicy(new ArrayList<Policy>());

        Policy policy = new Policy();
        Policy policyCreated = null;

        policy.setId(policyService.makePolicyId(patient.getName(), patientPolicyDTO.getCompany().getName()));
        policy.setCompany(patientPolicyDTO.getCompany());
        policy.setPatient(patient);
        policy.setStatus(0);
        policy.setExpiryDate(policyService.formatDateFromForm(patientPolicyDTO.getExpiryDate()));
        policy.setTotalCoverage(patientPolicyDTO.getCompany().getTotalCoverage());
        policy.setTotalCovered(0);
        policy.setCreatedAt(new Date());
        policy.setUpdatedAt(new Date());

        if(patient.getAvailableLimit() < policy.getTotalCoverage()) {
            model.addAttribute("patientAvailableLimit", patient.getAvailableLimitToString());
            model.addAttribute("companyTotalCoverage", policy.getCompany().getTotalCoverageToString());
            model.addAttribute("idPatient", patient.getId());
            model.addAttribute("patientClass", 1);
            return "response-exceed-patient-limit-policy";
        }

        Patient patientCreated = patientService.addPatient(patient);
        if (patientCreated != null) {
            policyService.addPolicy(policy);
        }

        if (patientCreated == null ) {
            model.addAttribute("responseMessage",
                    String.format("Patient gagal dibuat."));

            return "response-patientpolicy";
        }

        model.addAttribute("responseMessage",
                String.format("Patient %s dengan NIK %s berhasil ditambahkan dan meiliki Policy dengan ID %s.", patient.getName(), patient.getNik(), policy.getId()));

        return "response-patientpolicy";
    }

    @GetMapping("/{id}/upgrade-class")
    public String upgradeClassForm(@PathVariable UUID id, Model model) {
        UpgradePatientClassDTO patientClassDTO = new UpgradePatientClassDTO();
        Patient patientExisting = patientService.getPatientById(id);

        patientClassDTO.setId(patientExisting.getId());
        patientClassDTO.setNik(patientExisting.getNik());
        patientClassDTO.setInsuranceClass(String.valueOf(patientExisting.getPClass()));

        model.addAttribute("patientClassDTO", patientClassDTO);
        model.addAttribute("patient", patientService.getPatientByNIK(patientExisting.getNik()));

        return "form-upgrade-class-patient";
    }

    @PostMapping("/upgrade-class")
    public String upgradeClass(UpgradePatientClassDTO patientClassDTO, Model model) {
        Patient patient = patientService.getPatientByNIK(patientClassDTO.getNik());
        Patient newPatient = new Patient();
        int oldClass = patient.getPClass();

        System.out.println(patientClassDTO.getInsuranceClass());
        System.out.println(oldClass);

        newPatient.setId(patient.getId());
        newPatient.setNik(patient.getNik());
        newPatient.setUpdatedAt(new Date());
        newPatient.setPClass(Integer.parseInt(patientClassDTO.getInsuranceClass()));

        Patient patientUpdated = patientService.updateClassPatient(newPatient);

        if (patientUpdated == null ) {
            model.addAttribute("responseMessage",
                    String.format("Patient gagal diupgrade classnya."));

            return "response-patientpolicy";
        }

        model.addAttribute("responseMessage",
                String.format("Patient %s dengan NIK %s berhasil diupgrade dari class %s menjadi class %s", patient.getName(), patient.getNik(), oldClass, patientUpdated.getPClass()));
        model.addAttribute("nik", patientUpdated.getNik());
        return "response-upgrade-patient-class";
    }
}