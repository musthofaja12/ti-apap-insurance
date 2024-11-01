package apap.ti.insurance2206082354.restdto.response;

import apap.ti.insurance2206082354.model.Company;
import apap.ti.insurance2206082354.model.Coverage;
import apap.ti.insurance2206082354.model.Policy;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoverageResponseDTO {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;

    private String name;

    private int coverageAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> listCompany;
}
