package apap.ti.insurance2206082354.controller;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.CompanyDb;
import apap.ti.insurance2206082354.repository.PatientDb;
import apap.ti.insurance2206082354.repository.PolicyDb;
import apap.ti.insurance2206082354.request.AddCompanyRequestDTO;
import apap.ti.insurance2206082354.request.UpdateCompanyRequestDTO;
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

import java.util.*;

@Controller
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @Autowired
    PolicyService policyService;

    @Autowired
    PatientService patientService;

    @Autowired
    CoverageService coverageService;

    @GetMapping("/viewall")
    private String listCompany(Model model) {

        model.addAttribute("listCompany", companyService.getAllCompany());
        return "viewall-company";
    }

    @GetMapping("/{idCompany}")
    private String viewCompany(@PathVariable("idCompany") UUID idCompany, Model model) {
        Company company = companyService.getCompanyById(idCompany);
        model.addAttribute("company", company);

        return "view-company";
    }

    @GetMapping("/add")
    public String addCompanyForm(Model model) {
        var companyDTO = new AddCompanyRequestDTO();

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-add-company";
    }

    @PostMapping(value="/add", params={"deleteRow"})
    public String deleteRowCoverageCompany(@ModelAttribute AddCompanyRequestDTO companyDTO, @RequestParam("deleteRow") int row, Model model) {
        companyDTO.getListCoverage().remove(row);

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-add-company";
    }

    @PostMapping(value="/add", params={"addRow"})
    public String addRowDeveloperProyek(@ModelAttribute AddCompanyRequestDTO companyDTO, Model model) {
        if(companyDTO.getListCoverage() == null || companyDTO.getListCoverage().isEmpty()) {
            companyDTO.setListCoverage(new ArrayList<>());
        }

        companyDTO.getListCoverage().add(new Coverage());

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-add-company";
    }

    @PostMapping("/add")
    public String addCompany(@Valid @ModelAttribute AddCompanyRequestDTO companyDTO, BindingResult result, Model model) {
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

                return "response-company";
            }
        }

        var company = new Company();

        company.setName(companyDTO.getName());
        company.setEmail(companyDTO.getEmail());
        company.setContact(companyDTO.getContact());
        company.setAddress(companyDTO.getAddress());
        company.setListCoverage(companyDTO.getListCoverage());
        company.setListPolicy(new ArrayList<Policy>());
        company.setCreatedAt(new Date());
        company.setUpdatedAt(new Date());

        companyService.addCompany(company);

        model.addAttribute("responseMessage",
                String.format("Company %s dengan ID %s berhasil ditambahkan.", company.getName(), company.getId()));

        return "response-company";
    }

    @GetMapping("/{id}/update")
    public String updateCompanyForm(@PathVariable("id") UUID id, Model model) {
        Company company = companyService.getCompanyById(id);
        var companyDTO = new UpdateCompanyRequestDTO();

        companyDTO.setId(id);
        companyDTO.setName(company.getName());
        companyDTO.setEmail(company.getEmail());
        companyDTO.setContact(company.getContact());
        companyDTO.setAddress(company.getAddress());
        companyDTO.setListCoverage(company.getListCoverage());

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listPolicy", company.getListPolicy());
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-update-company";
    }

    @PostMapping(value="/update", params={"deleteRow"})
    public String deleteRowCoverageCompanyUpdate(@ModelAttribute UpdateCompanyRequestDTO companyDTO, @RequestParam("deleteRow") int row, Model model) {
        companyDTO.getListCoverage().remove(row);
        Company company = companyService.getCompanyById(companyDTO.getId());

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listPolicy", company.getListPolicy());
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-update-company";
    }

    @PostMapping(value="/update", params={"addRow"})
    public String addRowDeveloperProyekUpdate(@ModelAttribute UpdateCompanyRequestDTO companyDTO, Model model) {
        if(companyDTO.getListCoverage() == null || companyDTO.getListCoverage().isEmpty()) {
            companyDTO.setListCoverage(new ArrayList<>());
        }
        Company company = companyService.getCompanyById(companyDTO.getId());

        companyDTO.getListCoverage().add(new Coverage());

        model.addAttribute("companyDTO", companyDTO);
        model.addAttribute("listPolicy", company.getListPolicy());
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());

        return "form-update-company";
    }

    @PostMapping("/update")
    public String updateCompany(@Valid @ModelAttribute UpdateCompanyRequestDTO companyDTO, BindingResult result, Model model) {
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

                return "response-company";
            }
        }

        var company = new Company();

        company.setId(companyDTO.getId());
        company.setName(companyDTO.getName());
        company.setEmail(companyDTO.getEmail());
        company.setContact(companyDTO.getContact());
        company.setAddress(companyDTO.getAddress());
        company.setListCoverage(companyDTO.getListCoverage());

        if (companyService.updateCompany(company) != null) {
            model.addAttribute("responseMessage",
                    String.format("Company %s dengan ID %s berhasil diperbarui.", company.getName(), company.getId()));
        } else {
            model.addAttribute("responseMessage",
                    String.format("Company %s dengan ID %s gagal diperbarui.", company.getName(), company.getId()));
        }

        return "response-company";
    }

    @GetMapping("/{id}/delete")
    public String deleteCompany(@PathVariable("id") UUID id, Model model) {
        Company company = companyService.deleteCompany(id);

        if (company != null) {
            model.addAttribute("responseMessage",
                    String.format("Company %s dengan ID %s berhasil dihapus.", company.getName(), company.getId()));
        } else {
            model.addAttribute("responseMessage",
                    String.format("Company gagal dihapus."));
        }

        return "response-company";
    }
}