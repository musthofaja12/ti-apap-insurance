package apap.ti.insurance2206082354.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Company")
@SQLDelete(sql = "UPDATE company SET deleted_at = NOW() WHERE id=?")
@SQLRestriction("deleted_at IS NULL")
public class Company {
    @Id
    private UUID id = UUID.randomUUID();

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
    @Column(name = "contact", nullable = false)
    private String contact;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    private List<Policy> listPolicy;

    @ManyToMany
    @JoinTable(name = "coverage_company", joinColumns = @JoinColumn(name = "id_company"),
            inverseJoinColumns = @JoinColumn(name = "id_coverage"))
    List<Coverage> listCoverage;

    @Column(name = "deleted_at")
    private Date deletedAt;

    public String getAllCoverage() {
        String result = " ";
        for (int i = 0; i < listCoverage.size() - 1; i++) {
            result += listCoverage.get(i).getName() + ", ";
        }
        result += listCoverage.get(listCoverage.size() - 1).getName();
        return result;
    }

    public String getTotalCoverageToString() {
        int totalCoverage = 0;
        for (Coverage coverage : listCoverage) {
            totalCoverage += coverage.getCoverageAmount();
        }
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(totalCoverage);

    }

    public Long getTotalCoverage() {
        Long totalCoverage = 0l;
        for (Coverage coverage : listCoverage) {
            totalCoverage += coverage.getCoverageAmount();
        }
        return totalCoverage;
    }
}