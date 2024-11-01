package apap.ti.insurance2206082354;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
import apap.ti.insurance2206082354.repository.CoverageDb;
import apap.ti.insurance2206082354.repository.PolicyDb;
import apap.ti.insurance2206082354.service.CompanyService;
import apap.ti.insurance2206082354.service.CoverageService;
import apap.ti.insurance2206082354.service.PatientService;
import apap.ti.insurance2206082354.service.PolicyService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.capitalize;

@SpringBootApplication
public class Insurance2206082354Application {

	public static void main(String[] args) {
		SpringApplication.run(Insurance2206082354Application.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(CompanyService companyService, CoverageService coverageService, PatientService patientService, PolicyService policyService, CoverageDb coverageDb, PolicyDb policyDb) {
		return args -> {
			var faker = new Faker(new Locale("in-ID"));
			Random rand = new Random();

			var company = new Company();

			Faker faker_bule = new Faker();
			List<String> suffixes = List.of("Insurance", "Assurance", "Life", "Health", "Risk Management", "Secure");
			List<String> formats = List.of(
					"%s %s",
					"%s %s Corp",
					"%s %s Solutions",
					"%s & %s",
					"%s %s Services",
					"%s Group",
					"%s %s Ltd."
			);
			String companyName = String.format(
					formats.get(rand.nextInt(formats.size())),
					faker_bule.company().name(),
					suffixes.get(rand.nextInt(suffixes.size()))
			);

			String companyNameAbbreviation = companyName.substring(0, 3).toUpperCase();

			company.setName(companyName);

			var contactFirstName = faker.name().firstName();
			var contactLastName = faker.commerce().productName();
			String phoneNumber = faker.phoneNumber().phoneNumber();
			String formattedPhoneNumber = phoneNumber.replaceFirst("0", "+62");

			company.setContact(formattedPhoneNumber);

			String email = contactFirstName + "." + contactLastName + "@gmail.com";
			company.setEmail(email.replace(" ", "_"));
			company.setAddress(faker.address().fullAddress());
			company.setCreatedAt(new Date());
			company.setUpdatedAt(new Date());
			company.setListPolicy(new ArrayList<Policy>());

			company.setListCoverage(new ArrayList<Coverage>());
			int listCompanyCoverageMaxSize = rand.nextInt(12 - 1 + 1) + 1;
			long totalCoverage = 0;
			List<Coverage> listCoverage = coverageService.getAllCoverage();
			for (int i = 0; i < listCoverage.size(); i++) {
				if (company.getListCoverage().size() < listCompanyCoverageMaxSize) {
					int randCoverageIndex = rand.nextInt((listCoverage.size() - 1) - 0 + 1) + 0;

					if(!company.getListCoverage().contains(listCoverage.get(randCoverageIndex))) {
						company.getListCoverage().add(listCoverage.get(randCoverageIndex));
					}

					totalCoverage += listCoverage.get(randCoverageIndex).getCoverageAmount();
				}
			}

			Company newCompany = companyService.addCompany(company);

			var policy = new Policy();

			String patientFirstName = faker.name().firstName();
			String patientLastName = faker.name().lastName();
			String patientName = patientFirstName + " " + patientLastName;
			String patientFirstAbbreviation = String.valueOf(patientFirstName.charAt(0)).toUpperCase();
			String patientLastAbbreviation = String.valueOf(patientLastName.charAt(0)).toUpperCase();

			String randomLastCode = String.format("%04d", (1 + policyService.getAllPolicy().size()));

			String policyId = "POL" + patientFirstAbbreviation + patientLastAbbreviation + companyNameAbbreviation + randomLastCode;

			policy.setId(policyId);

			policy.setCompany(newCompany);

			policy.setExpiryDate(faker.date().future(100, TimeUnit.DAYS));
			policy.setTotalCoverage(totalCoverage);
			policy.setTotalCovered(0);
			policy.setCreatedAt(new Date());
			policy.setUpdatedAt(new Date());

			var patient = new Patient();

			String nik = "";
			List<String> listNik = new ArrayList<String>();
			boolean isNikUnique= false;

			for (Patient patientExisted: patientService.getAllPatient()) {
				listNik.add(patientExisted.getNik());
			}
			while(isNikUnique == false) {
				String kodeWilayah = String.format("%06d", rand.nextInt(999999));
				String tanggal = String.format("%02d", rand.nextInt(31) + 1);
				String bulan = String.format("%02d", rand.nextInt(12) + 1);
				String tahun = String.format("%02d", rand.nextInt(100));
				String nomorUrut = String.format("%04d", rand.nextInt(10000)); //
				String beforeChecksum = kodeWilayah + tanggal + bulan + tahun + nomorUrut;

				int sum = 0;
				for (int i = 0; i < beforeChecksum.length(); i++) {
					int digit = Character.getNumericValue(beforeChecksum.charAt(i));
					sum += digit * (i + 1);
				}
				sum %= 10;
				nik = beforeChecksum + sum;

				if (!listNik.contains(nik)) {
					isNikUnique = true;
				}
			}

			patient.setNik(nik);
			patient.setName(patientName);
			patient.setGender(rand.nextInt(1 - 0 + 1) + 0);
			patient.setBirthDate(faker.date().birthday());
			patient.setEmail("hilangharapan@rocketmail.com");
			patient.setPClass(rand.nextInt(3 - 1 + 1) + 1);
			patient.setCreatedAt(new Date());
			patient.setUpdatedAt(new Date());

			patient.setListPolicy(new ArrayList<Policy>());
//			patient.getListPolicy().add(policy);

			Patient newPatient = patientService.addPatient(patient);

			policy.setPatient(newPatient);

			policy.setStatus();
			policyService.addPolicy(policy);
		};
	}
}
