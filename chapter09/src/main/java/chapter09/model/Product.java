
package chapter09.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Inheritance(strategy = InheritanceType.JOINED) A strategy in which fields that are specific to a subclass are mapped
 * to a separate table than the fields that are common to the parent class, and a join is performed to instantiate the
 * subclass.
 */

@NamedQueries({
        @NamedQuery(name = "product.searchByPhrase", query = "from Product p where p.name like :text or p.description like :text"),
        @NamedQuery(name = "product.findProductAndSupplier", query = "from Product p, Supplier s where p.supplier=s")
})

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"supplier"})
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    Supplier supplier;
    @Column
    @NotNull
    String name;
    @Column
    @NotNull
    String description;
    @Column
    @NotNull
    Double price;

     public Product(Supplier supplier,
                   String name,
                   String description,
                   Double price) {
        this.supplier = supplier;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
