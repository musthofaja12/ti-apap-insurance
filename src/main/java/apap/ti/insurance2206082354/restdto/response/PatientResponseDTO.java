package apap.ti.insurance2206082354.restdto.response;

import apap.ti.insurance2206082354.model.Patient;
import apap.ti.insurance2206082354.model.Policy;
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
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
    private UUID id;

    private String nik;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;

    private String name;

    private int gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date birthDate;

    private String email;

    private int pClass;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<PolicyResponseDTO> listPolicy;
}
