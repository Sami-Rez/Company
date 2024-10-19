package spg.company.data;

import lombok.extern.slf4j.Slf4j;
import org.junit. jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import spg.company.data.entity.Company;
import spg.company.data.service.CompanyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@TestConfiguration
@ActiveProfiles(profiles = "test")
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;


    @DisplayName("Update company successfull")
    @Sql(scripts = {"/data-test.sql"})
    @Test
    void test() {

        Optional<Company> optional = companyService.findCompanyById(1L);
        Company company = optional.get();
        company.setName("GEÄNDERT");

        companyService.update( company );
        Company updatedCompany = companyService.findCompanyById(1L).get();
        assertEquals( "GEÄNDERT", updatedCompany.getName() );

    }
}