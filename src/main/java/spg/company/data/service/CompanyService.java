package spg.company.data.service;


import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import spg.company.data.entity.Company;
import spg.company.data.entity.Employee;
import spg.company.data.repository.CompanyRepository;
import spg.company.data.repository.EmployeeRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;



// TODO: add adoc infos:
// * Jedes DB-Statement muss innerhalb einer Transaktion laufen
// * Ein Spring Service ist per default transactional.
// * D.h. wenn eine Methode aufgerufen wird, beginnt eine Transaktion
//        beim Verlassen der Methode endet die Transaktion



@Slf4j
@Service
//@Transactional
public class CompanyService {

    private static final String P = "%";

    // injected stuff
    private final CompanyRepository companyRepo;
    private final EmployeeRepository employeeRepo;



    // -- CONSTRUCTOR --------------------------------------------------------------------------------------------------

    public CompanyService(CompanyRepository companyRepo, EmployeeRepository employeeRepo) {
        this.companyRepo = companyRepo;
        this.employeeRepo = employeeRepo;
    }


    // -- COMPANY ------------------------------------------------------------------------------------------------------

    public void update(Company company) {
        companyRepo.save(company);
    }

    public Optional<Company> findCompanyById(Long companyId) {
        if (companyId == null)
            return Optional.empty();
        return companyRepo.findById( companyId );
    }

    public Stream<Company> findAllCompanies(Query<Company, String> query) {
        return companyRepo.findAll( toPageable(query) ).stream();
    }

    public int hire(Long companyId, Collection<Employee> employees) {
        int counter = 0;


        // TODO: getReferenceById vs findById( ) -> findById Löst ein zusätzliches Select aus, um die Company aus der DB
        //          zu laden. Das ist aber nicht notwendig, da wir die companyId bereits kennen! Dieses Problem können
        //          wir mit getReferenceById lösen, das KEIN zusätzliches SELECT auslöst.
        //          Im Gegensatz zur findById lockt die getReferenceById die Daten nicht.
        //          Ist die companyId jedoch nicht vorhanden, löst das eine ConstraintViolationException beim flush aus.
        Company company = companyRepo.getReferenceById( companyId );

        for (Employee employee : employees) {
            try {
                employee.setCompany( company );
                employeeRepo.save(employee);
                counter++;
            } catch (Exception e) {
                log.error("Error hire {}, {}", employee, e.getMessage(), e);
            }
        }
        return counter;
    }


    // -- EMPLOYEE -----------------------------------------------------------------------------------------------------

    public Optional<Employee> findEmployeeById(Long eid) {
        return employeeRepo.findById( eid );
    }

    public void update(Employee employee) {
        employeeRepo.save(employee);
    }

    public void fire(Employee employee) {
        employee.setCompany(null);
        employeeRepo.save(employee);
    }

    public void delete(Employee employee) {
        if (employee == null)
            return;

        employeeRepo.delete(employee);
    }

    public Stream<Employee> findUnemployedEmployees(Query<Employee, Void> query) {
        return employeeRepo.findUnemployed( toPageable( query ) ).stream();
    }

    public Stream<Employee> findEmployeesByCompanyId(Long companyId, Query<Employee, Void> query) {
        return employeeRepo.findByCompanyId(
                companyId,
                PageRequest.of(
                        query.getPage(),
                        query.getPageSize(),
                        VaadinSpringDataHelpers.toSpringDataSort(query)
                )).stream();
    }

    public Stream<Employee> findUnemployedEmployees(String keyword, Query<Employee, Void> query) {
        return employeeRepo.searchUnemployed("%" + keyword.toLowerCase() + "%",
                toPageable( query )
        ).stream();
    }

    public int countUnemployedEmployees() {
        return employeeRepo.countUnemployed();
    }


    // -- SEARCH EXAMPLES ----------------------------------------------------------------------------------------------

    // Example 1
    public Stream<Employee> searchSpringRepoMethod(String keyword, Query<Employee, Void> query) {
        keyword = P + keyword + P;
        List<Employee> data = employeeRepo
                .findAllByCompanyIsNullAndFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(
                        keyword, keyword, toPageable( query )
                );
        return data.stream();
    }

    // Example 2
    public Stream<Employee> searchJpqlQuery(String keyword, Query<Employee, Void> query) {
        keyword = P + keyword + P;
        List<Employee> data = employeeRepo.searchUnemployedWithOr( keyword, toPageable(query));
        return data.stream();
    }

    // Example 3
    public Stream<Employee> searchJpqlConcatQuery(String keyword, Query<Employee, Void> query) {
        keyword = P + keyword + P;
        return employeeRepo.searchUnemployed( keyword, toPageable(query) ).stream();
    }



    public void test(Long id) {


    }


    // -- HELPER -------------------------------------------------------------------------------------------------------


    // Beispiel konkrete Implementierungen
/*
    // Ausschließlich für Employee|Void möglich - Code muss erweitert werden, wenn Company verwendet werden soll
    public Pageable toPageable(Query<Employee, Void> query) {
        return PageRequest.of(
                query.getPage(),
                query.getPageSize(),
                VaadinSpringDataHelpers.toSpringDataSort(query)
        );
    }

    // Ausschließlich für Company|String möglich - Code muss erweitert werden, wenn Company|Void verwendet werden soll
    public Pageable toPageable(Query<Company, String> query) {
        return PageRequest.of(
                query.getPage(),
                query.getPageSize(),
                VaadinSpringDataHelpers.toSpringDataSort(query)
        );
    }
*/

    // Generic way - Für ALLE Typen möglich ohne Code erweitern zu müssen
    public <T, F> Pageable toPageable(Query<T, F> query) {
        return PageRequest.of(
                query.getPage(),
                query.getPageSize(),
                VaadinSpringDataHelpers.toSpringDataSort(query)
        );
    }

}
