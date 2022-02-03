package chapter07.lifecycle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
public class FailingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    FailureStatus failureStatus = null;
    String value;

    enum FailureStatus {
        NOFAILURE, PREPERSIST, POSTPERSIST, POSTLOAD
    }

    @PrePersist
    void prePersist() {
        if (failureStatus.equals(FailureStatus.PREPERSIST)) {
            throw new RuntimeException("prepersist failure");
        }
    }

    @PostPersist
    void postPersist() {
        if (failureStatus.equals(FailureStatus.POSTPERSIST)) {
            throw new RuntimeException("postpersist failure");
        }
    }

    @PostLoad
    void postLoad() {
        if (failureStatus.equals(FailureStatus.POSTLOAD)) {
            throw new RuntimeException("postload failure");
        }
    }
}
