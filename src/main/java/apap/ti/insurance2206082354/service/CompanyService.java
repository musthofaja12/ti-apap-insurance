package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyService {
    Company addCompany(Company company);
    List<Company> getAllCompany();
    Company getCompanyById(UUID companyId);
    Company updateCompany(Company company);
    Company deleteCompany(UUID companyId);
} 

    
