package apap.ti.insurance2206082354.restdto.response;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyResponseDTO {
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CompanyResponseDTO company;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PatientResponseDTO patient;

    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date expiryDate;

    private long totalCoverage;

    private long totalCovered;
}
