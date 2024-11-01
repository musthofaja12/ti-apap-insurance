package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestDTO {

    @NotBlank (message = "Expiry Date Policy tidak boleh kosong.")
    private String expiryDate;

    @NotNull (message = "Company tidak boleh kosong.")
    private Company company;

    @NotNull (message = "Patient tidak boleh kosong.")
    private Patient patient;
}
