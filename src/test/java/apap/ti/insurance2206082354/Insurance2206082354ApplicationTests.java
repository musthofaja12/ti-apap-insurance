package apap.ti.insurance2206082354;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.PolicyDb;
import apap.ti.insurance2206082354.service.CoverageService;
import apap.ti.insurance2206082354.service.PolicyService;
import apap.ti.insurance2206082354.service.PolicyServiceImpl;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class Insurance2206082354ApplicationTests {

	@Autowired
	private PolicyService policyService;

	@Test
	void contextLoads() {
	}

	@Test
	void testReadAllPolicyFilter() {
		Random random = new Random();
		List<Policy> result = new ArrayList<>();
		Long from=0l;
		Long to=0l;
		int status=0;

		while (result.isEmpty()) {
			status = random.nextInt(4 - 0 + 1) + 0;
			from = random.nextLong();
			to = random.nextLong();

			while (to <= from) {
				to = random.nextLong();
			}

			result = policyService.getAllPolicyWithFilter(String.valueOf(status), String.valueOf(from), String.valueOf(to));
		}

		for (Policy policy : result) {
			assertEquals(policy.getStatus(), status, String.format("Expected only policies with status %s to be returned", status));
			assertTrue(policy.getTotalCoverage() >= from, String.format("Expected total coverage %s to be lesser than %s", policy.getTotalCoverage(), from));
			assertTrue(policy.getTotalCoverage() <= to, String.format("Expected total coverage %s to be greater than %s", policy.getTotalCoverage(), to));
		}
	}
}
