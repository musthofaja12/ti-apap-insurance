package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Policy;

import java.util.Date;
import java.util.List;

public interface PolicyService {
    Policy addPolicy(Policy policy);
    List<Policy> getAllPolicy();
    List<Policy> getAllPolicyWithFilter(String status, String from, String to);
    Policy getPolicyById(String policyId);
    String makePolicyId(String patientName, String companyName);
    Date formatDateFromForm(String date);
    String formatDateToForm(Date date);
    Policy updatePolicy(Policy policy);
} 

    
