package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Coverage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCompanyRequestDTO extends AddCompanyRequestDTO {

    @NotNull (message = "ID tidak boleh kosong")
    private UUID id;
}
