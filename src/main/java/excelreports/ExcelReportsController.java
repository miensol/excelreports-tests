package excelreports;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;

import java.io.*;

public class ExcelReportsController {

    private SessionHelper sessionHelper;

    public ExcelReportsController(SessionHelper sessionHelper) {
        this.sessionHelper = sessionHelper;
    }

    public InputStream performanceReport(final int branchId) {
        try {
            Workbook workbook = new HSSFWorkbook();
            PerformanceReportData data = sessionHelper.withinTransaction(
                    new SessionHelper.Function<PerformanceReportData, Session>() {
                        @Override
                        public PerformanceReportData call(Session session) {

                            ReportsFactory factory = new ReportsFactory(session);
                            return factory.performance(branchId);
                        }
                    }
            );
            new ExcelFiller(workbook).addSheet(data);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            workbook.write(stream);
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Could not create report", e);
        }
    }
}
