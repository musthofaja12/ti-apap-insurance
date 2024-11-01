package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.repository.CompanyDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDb companyDb;

    @Override
    public Company addCompany(Company company) {
        return companyDb.save(company);
    }

    @Override
    public List<Company> getAllCompany() {
        return companyDb.findAll();
    }

    @Override
    public Company getCompanyById(UUID id) {
        return companyDb.findById(id).orElse(null);
    }

    @Override
    public Company updateCompany(Company company) {
        Company companyToUpdate = getCompanyById(company.getId());

        if (companyToUpdate != null) {
            companyToUpdate.setName(company.getName());
            companyToUpdate.setAddress(company.getAddress());
            companyToUpdate.setEmail(company.getEmail());
            companyToUpdate.setContact(company.getContact());
            companyToUpdate.setListCoverage(company.getListCoverage());
            companyToUpdate.setUpdatedAt(new Date());

            return companyDb.save(companyToUpdate);
        }

        return null;
    }

    @Override
    public Company deleteCompany(UUID id) {
        Company companyToDelete = getCompanyById(id);
        if (companyToDelete != null && companyToDelete.getListPolicy().isEmpty()) {
            companyDb.delete(companyToDelete);
        }
        return null;
    }
}