package spg.company.data.repository;



import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import spg.company.data.entity.Employee;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {


//    List<Employee> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);
    List<Employee> findByCompanyId(Long companyId, Pageable pageable);
    
//    List<Employee> findEmployeesByCompanyId(Long companyId, Pageable pageable);
//    List<Employee> findEmployeesByCompany_Id(Long companyId, Pageable pageable);

    
    @Query(value = "select emp from Employee emp left join Company com on emp.company.id = com.id where emp.company is null")
    List<Employee> findUnemployed( Pageable pageable );


    @Query(value = "select count(emp.id) from Employee emp left join Company com on emp.company.id = com.id where emp.company is null")
    int countUnemployed();


    // -- FIND BY ------------------------------------------------------------------------------------------------------

    List<Employee> findByFirstName(String firstName, Pageable pageable);

    List<Employee> findByLastName(String firstName, Pageable pageable);

    List<Employee> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);


    // -- LIKE 3 different ways ----------------------------------------------------------------------------------------

    // spring repo
    List<Employee> findAllByCompanyIsNullAndFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(
            String firstName, String lastName, Pageable pageable);


    // jpql query or
    @Query("SELECT emp " +
            "FROM Employee emp " +
            "   LEFT JOIN Company com ON emp.company.id = com.id " +
            "WHERE  emp.company IS NULL " +
            "  AND ( lower(emp.lastName) LIKE lower(:keyword) OR lower(emp.firstName) LIKE lower(:keyword) )")
    List<Employee> searchUnemployedWithOr(String keyword, Pageable pageable);


    // jpql concat
    @Query("SELECT  emp " +
            "FROM   Employee emp " +
            "   LEFT JOIN   Company com ON emp.company.id = com.id " +
            "WHERE  emp.company IS NULL " +
            "   AND ( LOWER( CONCAT(emp.firstName, ' ', emp.lastName) ) LIKE :keyword )")
    List<Employee> searchUnemployed(String keyword, Pageable pageable);
}
