package excelreports;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double prize;

    protected Product(){}
    public Product(String name, double prize){
        this.name = name;
        this.prize =prize;
    }

    public String getName() {
        return name;
    }

    public double getPrize() {
        return prize;
    }
}
