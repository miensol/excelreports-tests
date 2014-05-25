package excelreports.tests;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import excelreports.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

//here we're testing whether the report contains the RIGHT values in right columns and rows
//note however that we're not dealing with excel formatting here

public class ReportsFactoryTests {
    private SessionHelper sessionHelper;
    private HSSFWorkbook targetWorkbook;

    @Before
    public void setup() {
        New.setupTestDatabase();
        this.sessionHelper = New.sessionHelper();
        this.targetWorkbook = new HSSFWorkbook();
    }

    @Test
    public void emptyReportShouldHaveTitle() {
        PerformanceReportData data = buildReport(42);

        String title = data.getTitle();

        assertThat(title, containsString("42"));
    }

    @Test
    public void emptyReportShouldHaveHeader() {
        PerformanceReportData data = buildReport(42);

        ReportRow row = Lists.newArrayList(data.getRows()).get(0);

        ArrayList<ReportCell> cells = Lists.newArrayList(row.iterator());
        assertThat(cells.size(), greaterThan(0));
    }

    @Test
    public void reportShouldHaveAsManyDataRowsAsTransactionItems() {
        final int branchId = 42;

        Transaction t = New.transaction(branchId);
        t.addItem(New.product(), 1);
        t.addItem(New.product(), 2);
        sessionHelper.saveInTransaction(t);
        PerformanceReportData data = buildReport(branchId);
        ArrayList<ReportRow> rows = Lists.newArrayList(Iterables.skip(data.getRows(), 1));
        assertThat(rows.size(), is(2));
    }

    @Test
    public void reportShouldIncludeOnlyRowsBelongingToGivenBranchId() {
        final int branchId = 42;
        Transaction t = New.transaction(branchId);
        t.addItem(New.product(), 2);
        Transaction other = New.transaction(branchId + 1);
        other.addItem(New.product(), 2);
        sessionHelper.saveInTransaction(t, other);

        PerformanceReportData data = buildReport(branchId);
        ArrayList<ReportRow> rows = Lists.newArrayList(Iterables.skip(data.getRows(), 1));
        assertThat(rows.size(), is(1));
    }

    @Test
    public void reportShouldIncludeProductNames(){
        final int branchId = 42;
        Transaction t = New.transaction(branchId);
        t.addItem(New.productNamed("Gold"),1);
        t.addItem(New.productNamed("Silver"),1);
        sessionHelper.saveInTransaction(t);

        PerformanceReportData data = buildReport(42);
        List<ReportCell> firstColumn = getColumn(data, 0);
        assertThat(firstColumn.get(0).getStringValue(), containsString("Product Name"));
        assertThat(firstColumn.get(1).getStringValue(), containsString("Gold"));
        assertThat(firstColumn.get(2).getStringValue(), containsString("Silver"));
    }

    @Test
    public void reportShouldIncludeProductPrize(){
        final int branchId = 42;
        Transaction t = New.transaction(branchId);
        t.addItem(New.productThatCosts(3),1);
        t.addItem(New.productThatCosts(1.5),1);
        sessionHelper.saveInTransaction(t);

        PerformanceReportData data = buildReport(42);
        List<ReportCell> secondColumn = getColumn(data, 1);
        assertThat(secondColumn.get(0).getStringValue(), containsString("Product Prize"));
        assertThat(secondColumn.get(1).getDoubleValue(), is(3D));
        assertThat(secondColumn.get(2).getDoubleValue(), is(1.5D));
    }

    private List<ReportCell> getColumn(PerformanceReportData data, final int column) {
        ArrayList<ReportRow> rows = Lists.newArrayList(data.getRows());
        return Lists.transform(rows, new Function<ReportRow, ReportCell>() {
            @Override
            public ReportCell apply(ReportRow reportCells) {
                return Lists.newArrayList(reportCells.iterator()).get(column);
            }
        });
    }

    private PerformanceReportData buildReport(final int branchId) {
        return sessionHelper.withinTransaction(new SessionHelper.Function<PerformanceReportData, Session>() {
            @Override
            public PerformanceReportData call(Session session) {
                return new ReportsFactory(session).performance(branchId);
            }
        });
    }
}
