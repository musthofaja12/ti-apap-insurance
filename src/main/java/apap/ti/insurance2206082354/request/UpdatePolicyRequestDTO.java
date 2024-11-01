package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePolicyRequestDTO extends AddPolicyRequestDTO {
    @NotNull(message = "ID tidak boleh kosong.")
    private String id;
}
