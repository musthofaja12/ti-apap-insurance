package apap.ti.insurance2206082354.restdto.request;

import apap.ti.insurance2206082354.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206082354.restdto.response.PatientResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestRestDTO {

    @NotNull (message = "Company tidak boleh kosong")
    private UUID company;

    private String nik;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Expiry date tidak boleh kosong")
    private Date expiryDate;
}