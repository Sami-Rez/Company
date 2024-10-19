package spg.company.data;


import com.vaadin.flow.data.validator.BeanValidator;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import spg.company.data.entity.Employee;
import spg.company.data.entity.Role;
import spg.company.data.repository.EmployeeRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository repository;

    @Test
    void shouldSaveUser() {
        Employee employee = new Employee();
        employee.setLastName("Testmann");
        employee.setFirstName("Max");
        employee.setEmail("test@spengergasse.at");
        employee.setDateOfBirth(LocalDate.of(1970, 8, 10 ));
        employee.setRole(Role.ANALYST);
        employee.setSalary(2394);
        employee.setPhone("+436504508790");
        employee.setSubcontractor(false);

        Employee savedEmployee = repository.save( employee );


//        User savedUser = userRepository.save(user);
        assertThat( savedEmployee ).usingRecursiveComparison().ignoringFields("id").isEqualTo( employee );
        assertThat( savedEmployee.getId() ).isNotNull();
    }

}