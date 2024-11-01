package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.repository.CompanyDb;
import apap.ti.insurance2206082354.repository.CoverageDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoverageServiceImpl implements CoverageService {

    @Autowired
    private CoverageDb coverageDb;

    @Override
    public List<Coverage> getAllCoverage() {
        return coverageDb.findAll();
    }
}