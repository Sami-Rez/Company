package spg.company.data.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "COMPANY")
public class Company extends AbstractEntity {

    @NotNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @NotEmpty
    @Column(name = "address")
    private String address;


    @OneToMany( mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();


    public String getName() {
        return name;
    }

    public void setName(String name)  {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // -- ADD Employees --------------------------------------

    public void addEmployee(Employee employee) {
        employees.add( employee );
        employee.setCompany(this);
    }

    public void removeEmployee(Employee employee) {
        employees.remove(employee);
        employee.setCompany(null);
    }


    public Set<Employee> getEmployees() {
        return new HashSet<>( employees );
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }


    // -- COMPARE TO ---------------------------------------------------------------------------------------------------

    @Override
    public int compareTo(AbstractEntity entity) {
        if(entity == null)
            return 1;

        if(!(entity instanceof Company company))
            return 1;

        return name.compareTo( company.getName() );
    }


    // -- EQUALS  HASH-CODE in super -----------------------------------------------------------------------------------


    // -- TO-STRING ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);

        // NEVER DO THIS!!!!! - LazyInitializationException!!! -> DB-Session could be closed
//        for (Employee employee : employees) {
//            sb.append(employee).append("\n");
//        }

        return sb.toString();
    }
}