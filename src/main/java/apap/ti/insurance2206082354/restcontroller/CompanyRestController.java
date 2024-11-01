package apap.ti.insurance2206082354.restcontroller;

import apap.ti.insurance2206082354.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206082354.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206082354.restdto.response.CoverageResponseDTO;
import apap.ti.insurance2206082354.restservice.CompanyRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

    @Autowired
    CompanyRestService companyRestService;

    @GetMapping("/{idCompany}/coverages")
    public ResponseEntity<?> companyCoveragesById(@PathVariable UUID idCompany) {
        var baseResponseDTO = new BaseResponseDTO<List<CoverageResponseDTO>>();

        CompanyResponseDTO companyResponseDTO = companyRestService.getCompanyById(idCompany);

        if (companyResponseDTO == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data company tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        List<CoverageResponseDTO> listCoverage = companyResponseDTO.getListCoverage();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCoverage);
        baseResponseDTO.setMessage(String.format("List Coverage dari Company berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}