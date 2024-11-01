package apap.ti.insurance2206082354.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.metamodel.model.domain.AbstractIdentifiableType;
import org.hibernate.validator.constraints.UniqueElements;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="nik", nullable = false)
    private String nik;

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
    @Column(name = "gender", nullable = false)
    private int gender;

    @NotNull
    @Column(name = "birth_date", columnDefinition = "DATE", nullable = false)
    private Date birthDate;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "p_class", nullable = false)
    private int pClass;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Policy> listPolicy;

    public String calculateAge() {
        if (birthDate != null) {
            LocalDate birthDateLocal = birthDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Period period = Period.between(birthDateLocal, LocalDate.now());
            return period.getYears() + " years, " + period.getMonths() + " months, " + period.getDays() + " days";
        }
        return "N/A";
    }

    public Long getAvailableLimit() {
        Long insuranceLimit = 0l;
        if (pClass == 1) {
            insuranceLimit = 100000000l;
        }
        else if (pClass == 2) {
            insuranceLimit = 50000000l;
        }
        else if (pClass == 3) {
            insuranceLimit = 25000000l;
        }

        Long limitUsed = 0l;
        for (Policy policy : listPolicy) {
            if (policy.getStatus() != 4) {
                limitUsed += policy.getTotalCoverage();
            }
        }

        return insuranceLimit-limitUsed;
    }

    public String getAvailableLimitToString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(getAvailableLimit());
    }

    public List<Company> getAllCompanyRegistered() {
        List<Company> companyRegistered = new ArrayList<Company>();
        for (Policy policy : listPolicy) {
            companyRegistered.add(policy.getCompany());
        }
        return companyRegistered;
    }
}