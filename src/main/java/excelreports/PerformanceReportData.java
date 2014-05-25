package excelreports;

import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;

public class PerformanceReportData {
    private ArrayList<ReportRow> rows;
    private String title;

    public PerformanceReportData(String title){
        this.rows = new ArrayList<>();
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void addHeader(String... headers) {
        ReportRow reportRow = newRow();
        rows.add(reportRow.addValues(headers).withBorder());
    }

    public void addRow(Object[] rowValues) {
        ReportRow row = newRow();
        rows.add(row.addValues(rowValues));
    }

    private ReportRow newRow() {
        ReportRow reportRow = new ReportRow();
        reportRow.setIndex(rows.size());
        return reportRow;
    }

    public Iterable<ReportRow> getRows() {
        return rows;
    }
}
