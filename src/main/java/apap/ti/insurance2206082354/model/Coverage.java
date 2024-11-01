package apap.ti.insurance2206082354.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Coverage")
public class Coverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "coverage_amount", nullable = false)
    private int coverageAmount;

    @ManyToMany(mappedBy = "listCoverage", fetch = FetchType.LAZY)
    @SQLRestriction("deleted_at IS NULL")
    private List<Company> listCompany;

    public String getCoverageAmountToString() {
        int coverageAmount = 0;

        coverageAmount += this.coverageAmount;
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(coverageAmount);
    }
}