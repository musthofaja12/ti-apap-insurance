package apap.ti.insurance2206082354.restcontroller;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206082354.restdto.response.*;
import apap.ti.insurance2206082354.restservice.CompanyRestService;
import apap.ti.insurance2206082354.restservice.PolicyRestService;
import apap.ti.insurance2206082354.service.CompanyService;
import apap.ti.insurance2206082354.service.PatientService;
import apap.ti.insurance2206082354.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/policy")
public class PolicyRestController {

    @Autowired
    PolicyRestService policyRestService;

    @Autowired
    PatientService patientService;

    @Autowired
    CompanyService companyService;

    @PostMapping("/stat")
    public ResponseEntity<?> policyStat(@RequestParam("period") String period, @RequestParam("year") String year) {
        var baseResponseDTO = new BaseResponseDTO<PolicyStatisticsResponseDTO>();

        PolicyStatisticsResponseDTO policyStatisticsResponseDTO = policyRestService.getPolicyStatistics(period, year);

        if (policyStatisticsResponseDTO == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policyStatisticsResponseDTO);
        baseResponseDTO.setMessage(String.format("Policy statistics berhasil dibuat"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> policyDetail(@RequestParam("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policyResponseDTO = policyRestService.getPolicyById(id);

        if (policyResponseDTO == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policyResponseDTO);
        baseResponseDTO.setMessage(String.format("Policy dengan ID %s berhasil ditemukan", policyResponseDTO.getId()));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addPolicyForm(@RequestParam("nik") String nik, @Valid @RequestBody AddPolicyRequestRestDTO addPolicyRequestRestDTO,
                                           BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        Patient patientToAdd = patientService.getPatientByNIK(nik);
        Company companyToAdd = companyService.getCompanyById(addPolicyRequestRestDTO.getCompany());

        if (patientToAdd == null) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(String.format("Patient dengan NIK %s tidak dapat ditemukan", nik));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
        if (companyToAdd == null) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(String.format("Company dengan ID %s tidak dapat ditemukan", addPolicyRequestRestDTO.getCompany()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        addPolicyRequestRestDTO.setNik(nik);
        PolicyResponseDTO policyResponseDTO = policyRestService.addPolicy(addPolicyRequestRestDTO);

        if (policyResponseDTO == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy gagal ditambahkan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policyResponseDTO);
        baseResponseDTO.setMessage(String.format("Policy dengan ID %s berhasil ditambahkan", policyResponseDTO.getId()));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}