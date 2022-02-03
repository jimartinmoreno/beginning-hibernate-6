package chapter09.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Software extends Product implements Serializable {

    @Column
    @NotNull
    String version;

    public Software(Supplier supplier,
                    String name,
                    String description,
                    Double price,
                    String version) {
        super(supplier, name, description, price);
        this.version = version;
    }
}