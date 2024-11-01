package apap.ti.insurance2206082354.restservice;

import apap.ti.insurance2206082354.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206082354.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206082354.restdto.response.PolicyResponseDTO;
import apap.ti.insurance2206082354.restdto.response.PolicyStatisticsResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PolicyRestService {
    PolicyStatisticsResponseDTO getPolicyStatistics(String period, String year);
    PolicyResponseDTO getPolicyById(String policyId);
    PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO addPolicyRequestRestDTO);
}
