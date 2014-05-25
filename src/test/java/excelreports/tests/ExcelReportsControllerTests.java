package excelreports.tests;

import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import excelreports.ExcelReportsController;
import excelreports.SessionHelper;
import excelreports.Transaction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

//here we test whether we have connected all pieces together properly
//we only check whether there is no exception and we have produced a workbook
//with some sheet and some rows and cells
//note that we don't assert whether the file contains actual proper values

//to be more sure that we have actually produced a correct file we prepared a reference rendering (with our api)
//and checked it manually by opening and reviewing - it's saved to our test directory and checked in to source control
//we then compare the contents on a byte level - it's a very brittle test but gives you more confidence

public class ExcelReportsControllerTests {

    private ExcelReportsController reporter;
    private SessionHelper helper;

    @Before
    public void buildReportController(){
        New.setupTestDatabase();
        helper = New.sessionHelper();
        reporter = new ExcelReportsController(helper);
    }


    @Test
    public void createsEmptyExcelReport() throws IOException {
        InputStream file = reporter.performanceReport(3);
        assertNotNull(file);
        assertThat(file.available(), greaterThan(0));
    }

    @Test
    public void createsExcelReportWithGivenData() throws Exception {
        final int branchId = 3;
        helper.withinTransaction(new SessionHelper.Action<Session>() {
            @Override
            public void call(Session session) {
                Transaction transaction = New.transaction(branchId);
                transaction.addItem(New.product(), 1);
                session.save(transaction);
            }
        });
        InputStream stream = reporter.performanceReport(branchId);
        Workbook wb = WorkbookFactory.create(stream);
        Sheet sheet = wb.getSheetAt(0);
        assertNotNull("Must have at least one sheet", sheet);
        Row sheetRow = sheet.getRow(0);
        assertNotNull("Must have at least one row", sheetRow);
        assertNotNull("Must have at least one cell", sheetRow.getCell(0));
    }

    @Test
    public void createsExcelReportEqualToReferenceRendering() throws Exception {
        final int branchId = 3;
        helper.withinTransaction(new SessionHelper.Action<Session>() {
            @Override
            public void call(Session session) {
                Transaction transaction = New.transaction(branchId);
                transaction.addItem(New.product(), 1);
                transaction.addItem(New.product(), 2);
                transaction.addItem(New.product(), 3);
                session.save(transaction);
            }
        });
        InputStream reportSource = reporter.performanceReport(branchId);
//        IOUtils.copy(reportSource, new FileOutputStream("reference.xlsx"));
        ByteSource reportBytes = ByteSource.wrap(ByteStreams.toByteArray(reportSource));
        InputStream resourceAsStream = getClass().getResourceAsStream("/reference.xlsx");
        ByteSource referenceBytes = ByteSource.wrap(ByteStreams.toByteArray(resourceAsStream));

        assertThat(referenceBytes.contentEquals(reportBytes), is(true));
    }

}
