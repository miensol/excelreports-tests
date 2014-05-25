package excelreports;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="BranchTransaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private int branchId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transaction")
    private List<TransactionItem> transactionItems;

    protected Transaction(){
    }

    public Transaction(int branchId) {
        this.branchId = branchId;
        this.transactionItems = new ArrayList<TransactionItem>();
    }

    public int getBranchId() {
        return branchId;
    }

    public void addItem(Product product, double quantity){
        TransactionItem item = new TransactionItem(this,product,quantity);
        transactionItems.add(item);
    }

}
