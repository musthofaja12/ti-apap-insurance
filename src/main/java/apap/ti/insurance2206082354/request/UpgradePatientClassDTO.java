package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpgradePatientClassDTO {

    @NotBlank (message = "ID tidak boleh kosong.")
    private UUID id;

    @NotBlank (message = "NIK tidak boleh kosong.")
    private String nik;

    @NotNull (message = "Insurance Class harus jelas")
    private String insuranceClass;
}
