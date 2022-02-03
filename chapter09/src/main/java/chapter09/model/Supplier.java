package chapter09.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name = "supplier.findAll", query = "from Supplier s"),
        @NamedQuery(name = "supplier.findByName", query = "from Supplier s where s.name=:name"),
        @NamedQuery(name = "supplier.averagePrice", query = "select p.supplier.id, avg(p.price) from Product p GROUP BY p.supplier.id")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "supplier.findAverage", query = "SELECT p.supplier_id, avg(p.price) FROM Product p GROUP BY p.supplier_id")
})

@Data
@NoArgsConstructor
@Entity
public class Supplier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(unique = true)
    @NotNull String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "supplier", targetEntity = Product.class)
    List<Product> products = new ArrayList<>();

    public Supplier(String name) {
        this.name = name;
    }
}
