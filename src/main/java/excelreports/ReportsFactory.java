package excelreports;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.List;

public class ReportsFactory {
    private Session session;

    public ReportsFactory(Session session) {
        this.session = session;
    }

    public PerformanceReportData performance(int branchId) {
        SQLQuery sqlQuery = session.createSQLQuery("select ti.productName, ti.productPrize, ti.quantity, ti.totalPrize from TransactionItem ti, BranchTransaction bt where bt.branchId = :branchId and bt.id = ti.transaction_id");
        sqlQuery.setParameter("branchId", branchId);
        PerformanceReportData reportData = new PerformanceReportData("Performance of branch " + branchId);
        reportData.addHeader("Product Name", "Product Prize", "Quantity", "Total Prize");
        for(Object row: sqlQuery.list()){
            Object[]rowValues = (Object[])row;
            reportData.addRow(rowValues);
        }
        return reportData;
    }
}
