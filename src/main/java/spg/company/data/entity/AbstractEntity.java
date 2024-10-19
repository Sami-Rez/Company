package spg.company.data.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;

import java.io.Serializable;


@MappedSuperclass
public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractEntity other)) {
            return false;
        }

        if (getId() != null) {
            return getId().equals(other.getId());
        }
        return super.equals(other);
    }

    @Override
    public int compareTo(AbstractEntity o) {
        if(o == null)
            return 1;
        if(id == null)
            return -1;
        return Long.compare( id, o.getId() );
    }
}
