package apap.ti.insurance2206082354.restservice;

import apap.ti.insurance2206082354.restdto.response.CompanyResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CompanyRestService {
    List<CompanyResponseDTO> getAllCompany();

    CompanyResponseDTO getCompanyById(UUID companyId);
}
