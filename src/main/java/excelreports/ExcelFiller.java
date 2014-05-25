package excelreports;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
* Created by piotr on 24/05/14.
*/
public class ExcelFiller {
    private Workbook workbook;

    public ExcelFiller(Workbook workbook) {
        this.workbook = workbook;
    }

    public void addSheet(PerformanceReportData data) {
        Sheet sheet = workbook.createSheet(data.getTitle());
        for (ReportRow reportRow : data.getRows()) {
            Row row = sheet.createRow(reportRow.getIndex());
            for (ReportCell value : reportRow) {
                Cell cell = row.createCell(value.getIndex());
                value.styleCell(cell);
                value.fillCell(cell);
            }
        }
    }


}
