package apap.ti.insurance2206082354.controller;

import apap.ti.insurance2206082354.repository.CompanyDb;
import apap.ti.insurance2206082354.repository.PatientDb;
import apap.ti.insurance2206082354.repository.PolicyDb;
import apap.ti.insurance2206082354.service.CompanyService;
import apap.ti.insurance2206082354.service.PatientService;
import apap.ti.insurance2206082354.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {
    @Autowired
    CompanyService companyService;

    @Autowired
    PolicyService policyService;

    @Autowired
    PatientService patientService;

    @GetMapping("/")
    private String beranda(Model model) {

        model.addAttribute("policyTotal", policyService.getAllPolicy().size());
        model.addAttribute("companyTotal", companyService.getAllCompany().size());
        model.addAttribute("patientTotal", patientService.getAllPatient().size());
        return "beranda";
    }

}