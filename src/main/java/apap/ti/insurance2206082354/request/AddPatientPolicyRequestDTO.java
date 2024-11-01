package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPatientPolicyRequestDTO {

    @NotBlank (message = "Nama tidak boleh kosong.")
    private String name;

    @NotBlank (message = "NIK tidak boleh kosong.")
    private String nik;

    @NotBlank (message = "Email tidak boleh kosong.")
    private String email;

    @NotBlank (message = "Gender tidak boleh binggung")
    private String gender;

    @NotBlank (message = "Tanggal Lahir tidak boleh kosong.")
    private String birthDate;

    @NotBlank (message = "Insurance Class harus jelas")
    private String insuranceClass;

    @NotBlank (message = "Expiry Date Policy tidak boleh kosong.")
    private String expiryDate;

    @NotNull (message = "Company tidak boleh kosong.")
    private Company company;
}
