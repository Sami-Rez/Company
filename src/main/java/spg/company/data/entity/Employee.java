package spg.company.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;


@Entity
@Table(name = "EMPLOYEE")
public class Employee extends AbstractEntity {

    public static final String[] GRID_COLS = new String[]{"dateOfBirth", "role", "salary", "subcontractor",};

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @Email
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "phone")
    private String phone;

    @Past
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Column(name = "salary")
    private Integer salary;

    @NotNull
    @Column(name = "subcontractor")
    private Boolean subcontractor;


    @Column(name = "image_url")
    private String imageUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_company")
    private Company company;



    // -- GETTER/SETTER ------------------------------------------------------------------------------------------------

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Boolean getSubcontractor() {
        return subcontractor;
    }

    public void setSubcontractor(Boolean subcontractor) {
        this.subcontractor = subcontractor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return lastName + " " + firstName;
    }


    // -- COMPARE TO ---------------------------------------------------------------------------------------------------

    @Override
    public int compareTo(AbstractEntity entity) {
        if(entity == null)
            return 1;

        if(!(entity instanceof Employee employee))
            return 1;

        return lastName.compareTo( employee.getLastName() );
    }


    // -- TO STRING ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        // WICHTIG!!!! Keine Entity-Referenzklassen mit einbeziehen, da dies zu einer LazyInitializationException führt!
        return super.toString() +
                ", " + firstName +
                ", " + lastName +
                ", " + email +
                ", " + role;

    }
}