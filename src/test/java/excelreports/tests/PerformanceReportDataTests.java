package excelreports.tests;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import excelreports.PerformanceReportData;
import excelreports.ReportCell;
import excelreports.ReportRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

//here we can test whether our logic for adding rows and headers is correct
// the one test that is written here is actually one that I DON'T like since it's going into too much details
public class PerformanceReportDataTests {

    private PerformanceReportData data;
    private Cell cell;
    private CellStyle cellStyle;

    @Before
    public void setup(){
        data = new PerformanceReportData("a title");
        cell = new HSSFWorkbook().createSheet().createRow(0).createCell(0);
        cellStyle = cell.getCellStyle();
    }

    @Test
    public void headersShouldHaveBorder(){
        data.addHeader("One", "Two", "Three");

        ReportRow first = Iterables.getFirst(data.getRows(), null);
        List<ReportCell> reportCells = Lists.newArrayList(first.iterator());
        ReportCell reportCell = reportCells.get(0);
        reportCell.styleCell(cell);

        assertThat(cellStyle.getBorderBottom(), is(CellStyle.BORDER_MEDIUM));
        assertThat(cellStyle.getBorderLeft(), is(CellStyle.BORDER_MEDIUM));
        assertThat(cellStyle.getBorderRight(), is(CellStyle.BORDER_MEDIUM));
        assertThat(cellStyle.getBorderTop(), is(CellStyle.BORDER_MEDIUM));
    }
}
