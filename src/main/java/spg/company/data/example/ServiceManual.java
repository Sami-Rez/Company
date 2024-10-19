package spg.company.data.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import spg.company.data.entity.Company;

@Service
public class ServiceManual {


    @PersistenceContext
    private EntityManager em;

    public void test() {
        em.persist( new Company());
    }

}
