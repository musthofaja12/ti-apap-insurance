package apap.ti.insurance2206082354.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Policy")
public class Policy {
    @Id
    private String id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_company", referencedColumnName = "id")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_patient", referencedColumnName = "id")
    private Patient patient;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;

    @NotNull
    @Column(name = "expiry_date", columnDefinition = "DATE", nullable = false)
    private Date expiryDate;

    @NotNull
    @Column(name = "total_coverage", nullable = false)
    private long totalCoverage;

    @NotNull
    @Column(name = "total_covered", nullable = false)
    private long totalCovered;

    public String getTotalCoverageAmountToString() {
        long coverageAmount = this.totalCoverage;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(coverageAmount);
    }

    public String getTotalCoveredAmountToString() {
        long coverageAmount = this.totalCovered;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(coverageAmount);
    }

    public void setStatus() {
        LocalDate expiryDate = this.expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        if (this.status == 4) {
            this.status = 4;
        }
        else if (expiryDate.isEqual(today) || expiryDate.isBefore(today)) {
            this.status = 3;
        }
        else if (getTotalCovered() == getTotalCoverage()) {
            this.status = 2;
        }
        else if (getTotalCovered() > 0l) {
            this.status = 1;
        }
        else if (getTotalCovered() == 0l) {
            this.status = 0;
        }
    }

    public String getStatusToString() {
        return switch (this.status) {
            case 0 -> "Created";
            case 1 -> "Partially Claimed";
            case 2 -> "Fully Claimed";
            case 3 -> "Expired";
            case 4 -> "Cancelled";
            default -> "";
        };
    }
}