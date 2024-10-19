package spg.company.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import spg.company.data.entity.Company;


public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
}