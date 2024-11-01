package apap.ti.insurance2206082354.request;

import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.service.CoverageService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCompanyRequestDTO {

    @NotBlank (message = "Nama tidak boleh kosong.")
    private String name;

    @NotBlank (message = "Email tidak boleh kosong.")
    private String email;

    @NotBlank (message = "Contact tidak boleh kosong.")
    private String contact;

    @NotBlank (message = "Address tidak boleh kosong.")
    private String address;

    @NotNull(message = "Coverage tidak boleh kosong.")
    private List<Coverage> listCoverage;
}
