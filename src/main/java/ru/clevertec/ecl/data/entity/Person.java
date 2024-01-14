package ru.clevertec.ecl.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import ru.clevertec.ecl.data.converter.SexConverter;

@Getter
@Setter
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "sex_id", nullable = false)
    @Convert(converter = SexConverter.class)
    private Sex sex;

    @Column(name = "passport_series", nullable = false)
    private String passportSeries;

    @Column(name = "passport_number", nullable = false, columnDefinition = "INTEGER CHECK (passport_number >= 100000 AND passport_number <= 999999)")
    private Integer passportNumber;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "house_id", nullable = false)
    private House residentsPlace;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "persons_houses", joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "house_id"))
    private Set<House> ownership;

    @Generated(GenerationTime.INSERT)
    @Column(name = "create_date", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP (3) WITHOUT TIME ZONE DEFAULT (now() at time zone 'utc')")
    private Instant createDate;

    @Generated(GenerationTime.INSERT)
    @Column(name = "update_date", insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP (3) WITHOUT TIME ZONE DEFAULT (now() at time zone 'utc')")
    private Instant updateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Person other = (Person) o;
        return id != null && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", sex=" + sex +
                ", passportSeries='" + passportSeries + '\'' +
                ", passportNumber=" + passportNumber +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}
