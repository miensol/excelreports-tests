package excelreports;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class TransactionItem {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private double productPrize;

    @Column(nullable = false)
    private double quantity;

    @Column(nullable = false)
    private double totalPrize;

    @ManyToOne(optional = false)
    private Transaction transaction;

    protected TransactionItem(){}

    public TransactionItem(Transaction transaction, Product product, double quantity) {
        this.transaction = transaction;
        this.productName = product.getName();
        this.quantity = quantity;
        this.productPrize = product.getPrize();
        this.totalPrize = this.productPrize * this.quantity;
    }
}
