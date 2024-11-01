package apap.ti.insurance2206082354.controller;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.request.AddPolicyRequestDTO;
import apap.ti.insurance2206082354.request.UpdatePolicyRequestDTO;
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

import java.util.Date;

@Controller
@RequestMapping("/policy")
public class PolicyController {
    @Autowired
    CompanyService companyService;

    private final PolicyService policyService;

    @Autowired
    PatientService patientService;

    @Autowired
    CoverageService coverageService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/viewall")
    private String listCompany(@RequestParam(name = "status", defaultValue = "") String status,
                               @RequestParam(name = "from", defaultValue = "") String from,
                               @RequestParam(name = "to", defaultValue = "") String to, Model model) {

        model.addAttribute("listPolicy", policyService.getAllPolicyWithFilter(status, from, to));
        model.addAttribute("status", status);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "viewall-policy";
    }

    @GetMapping("/{idPolicy}")
    private String viewPolicy(@PathVariable("idPolicy") String idPolicy, Model model) {
        Policy policy = policyService.getPolicyById(idPolicy);
        model.addAttribute("policy", policy);

        return "view-policy";
    }

    @GetMapping("/add")
    public String addPolicyForm(@RequestParam("nik") String nik, Model model) {
        var policyDTO = new AddPolicyRequestDTO();
        Patient patient = patientService.getPatientByNIK(nik);

        policyDTO.setPatient(patient);

        model.addAttribute("policyDTO", policyDTO);
        model.addAttribute("listCompany", companyService.getAllCompany());
        model.addAttribute("patient", patient);
        model.addAttribute("listCompanyRegistered", patient.getAllCompanyRegistered());

        return "form-add-policy";
    }

    @PostMapping("/add")
    public String addPolicy(@Valid @ModelAttribute AddPolicyRequestDTO policyDTO, BindingResult result, Model model) {
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

        if (policyDTO.getPatient().getAvailableLimit() < policyDTO.getCompany().getTotalCoverage()) {
            model.addAttribute("patientAvailableLimit", policyDTO.getPatient().getAvailableLimitToString());
            model.addAttribute("companyTotalCoverage", policyDTO.getCompany().getTotalCoverageToString());
            model.addAttribute("idPatient", policyDTO.getPatient().getId());
            model.addAttribute("patientClass", policyDTO.getPatient().getPClass());
            return "response-exceed-patient-limit-policy";
        }

        Policy policy = new Policy();

        policy.setId(policyService.makePolicyId(policyDTO.getPatient().getName(), policyDTO.getCompany().getName()));
        policy.setCompany(policyDTO.getCompany());
        policy.setPatient(policyDTO.getPatient());
        policy.setExpiryDate(policyService.formatDateFromForm(policyDTO.getExpiryDate()));
        policy.setTotalCoverage(policyDTO.getCompany().getTotalCoverage());
        policy.setTotalCovered(0);
        policy.setCreatedAt(new Date());
        policy.setUpdatedAt(new Date());
        policy.setStatus();

        Policy newPolicy = policyService.addPolicy(policy);

        if (newPolicy == null) {
            model.addAttribute("responseMessage",
                    String.format("Gagal menambahkan Policy"));
            return "response-patientpolicy";
        }

        model.addAttribute("responseMessage",
                String.format("Policy dengan ID %s untuk Patient %s dengan NIK %s berhasil ditambahkan", policy.getId(), policyDTO.getPatient().getName(), policyDTO.getPatient().getNik()));
        return "response-patientpolicy";
    }

    @GetMapping("/{idPolicy}/update")
    public String updatePolicyForm(@PathVariable("idPolicy") String idPolicy, Model model) {
        UpdatePolicyRequestDTO policyDTO = new UpdatePolicyRequestDTO();
        Policy policy = policyService.getPolicyById(idPolicy);

        policyDTO.setId(policy.getId());
        policyDTO.setPatient(policy.getPatient());
        policyDTO.setCompany(policy.getCompany());
        policyDTO.setExpiryDate(policyService.formatDateToForm(policy.getExpiryDate()));

        System.out.println(policyDTO.getCompany().getListCoverage());
        model.addAttribute("policyDTO", policyDTO);
        model.addAttribute("policy", policy);

        return "form-update-policy";
    }

    @PostMapping("/update")
    public String updatePolicy(UpdatePolicyRequestDTO policyDTO, Model model) {
        Policy policy = policyService.getPolicyById(policyDTO.getId());
        Company company = companyService.getCompanyById(policyDTO.getCompany().getId());

        policy.setId(policyDTO.getId());
        policy.setCompany(policyDTO.getCompany());
        policy.setPatient(policyDTO.getPatient());
        policy.setExpiryDate(policyService.formatDateFromForm(policyDTO.getExpiryDate()));
        policy.setTotalCoverage(company.getTotalCoverage());
        policy.setTotalCovered(0);
        policy.setStatus();
        policy.setUpdatedAt(new Date());

        Policy newPolicy = policyService.updatePolicy(policy);

        if (newPolicy == null) {
            model.addAttribute("responseMessage",
                    String.format("Gagal mengupdate Policy"));
            return "response-patientpolicy";
        }

        model.addAttribute("responseMessage",
                String.format("Policy dengan ID %s untuk Patient %s dengan NIK %s berhasil diupdate", policy.getId(), policyDTO.getPatient().getName(), policyDTO.getPatient().getNik()));
        return "response-patientpolicy";
    }

    @GetMapping("/{id}/delete")
    public String deletePolicy(@PathVariable("id") String idPolicy, Model model) {
        Policy policy = policyService.getPolicyById(idPolicy);
        Patient patient = patientService.getPatientById(policy.getPatient().getId());

        if (policy.getStatus() != 0) {
            model.addAttribute("responseMessage",
                    String.format("Gagal mengcancel Policy dengan status %s karena Policy hanya dapat dicancel apabila berstatus Created", policy.getStatusToString()));
            return "response-patientpolicy";
        }

        patient.getListPolicy().remove(policy);
        Patient newPatient = patientService.updatePatient(patient);

        policy.setStatus(4);
        Policy newPolicy = policyService.updatePolicy(policy);

        if (newPolicy == null) {
            model.addAttribute("responseMessage",
                    String.format("Gagal mengcancel Policy"));
            return "response-patientpolicy";
        }

        if (newPatient == null) {
            model.addAttribute("responseMessage",
                    String.format("Gagal mengupdate Patient"));
            return "response-patientpolicy";
        }

        model.addAttribute("responseMessage",
                String.format("Policy dengan ID %s untuk Patient %s dengan NIK %s berhasil dicancel", policy.getId(), patient.getName(), patient.getNik()));
        return "response-patientpolicy";
    }

    @GetMapping("/stat")
    public String statistics(Model model) {
        return "view-policy-statistics";
    }
}