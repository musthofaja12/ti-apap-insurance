package apap.ti.insurance2206082354.service;

import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.PatientDb;
import apap.ti.insurance2206082354.repository.PolicyDb;
import org.hibernate.internal.util.ZonedDateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    private final PolicyDb policyDb;

    public PolicyServiceImpl(PolicyDb policyDb) {
        this.policyDb = policyDb;
    }

    @Override
    public List<Policy> getAllPolicy() {
        return policyDb.findAll();
    }

    @Override
    public Policy addPolicy(Policy policy) {
        return policyDb.save(policy);
    }

    @Override
    public List<Policy> getAllPolicyWithFilter(String status, String from, String to) {
        try {
            List<Policy> policyList = new ArrayList<Policy>();
            Long fromLong = 0l;
            Long toLong = 0l;

            from = from.trim();
            to = to.trim();

            if (from.equals("")) {
                fromLong = Long.valueOf(0);
            } else {
                fromLong = Long.valueOf(from);
            }

            if (to.equals("")) {
                toLong = Long.MAX_VALUE;
            } else {
                toLong = Long.valueOf(to);
            }
            if (status.equals("")) {
                policyList = policyDb.findByTotalCoverageBetween(fromLong, toLong);
            } else {
                policyList = policyDb.findByStatusAndTotalCoverageBetween(Integer.parseInt(status), fromLong, toLong);
            }

            return policyList;
        } catch (Exception e) {
            return new ArrayList<Policy>();
        }

    }

    @Override
    public Policy getPolicyById(String policyId) {
        return policyDb.findById(policyId).orElse(null);
    }

    @Override
    public String makePolicyId(String patientName, String companyName) {
        String firstLetterName = "";
        String secondLetterName = "";

        String[] nameSplit = patientName.split(" ");

        if (nameSplit.length > 1) {
            firstLetterName = nameSplit[0].substring(0, 1).toUpperCase();
            secondLetterName = nameSplit[1].substring(0, 1).toUpperCase();
        } else {
            firstLetterName = nameSplit[0].substring(0, 1).toUpperCase();
            secondLetterName = nameSplit[0].substring(1, 2).toUpperCase();
        }

        String companyLetterName = companyName.replace(" ", "").substring(0, 3).toUpperCase();
        String randomLastCode = String.format("%04d", (1 + getAllPolicy().size()));

        return "POL" + firstLetterName + secondLetterName + companyLetterName + randomLastCode;
    }

    @Override
    public Date formatDateFromForm(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String formatDateToForm(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(date);
    }

    @Override
    public Policy updatePolicy(Policy policy) {
        Policy policyToBeUpdated = policyDb.findById(policy.getId()).orElse(null);

        if (policyToBeUpdated == null) {
            return null;
        }

        policyToBeUpdated.setStatus(policy.getStatus());
        policyToBeUpdated.setExpiryDate(policy.getExpiryDate());
        policyToBeUpdated.setUpdatedAt(policy.getUpdatedAt());

        return policyDb.save(policyToBeUpdated);
    }
}