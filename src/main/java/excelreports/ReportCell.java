package excelreports;

import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;

public class ReportCell {
    private final int index;
    private final ArrayList<Stylus> styles;
    Object value;
    public ReportCell(Object value, int index) {
        this.value = value;
        this.index = index;
        this.styles = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public void fillCell(Cell cell) {
        if(value instanceof Double){
            cell.setCellValue(getDoubleValue());
        } else {
            cell.setCellValue(getStringValue());
        }
    }

    public void styleCell(Cell cell) {
        for(Stylus stylus: this.styles){
            stylus.style(cell.getCellStyle());
        }
    }

    public ReportCell withBorder() {
        this.styles.add(Stylus.mediumBorder);
        return this;
    }

    public String getStringValue() {
        return value != null ? value.toString(): null;
    }

    public double getDoubleValue() {
        return ((Number)value).doubleValue();
    }
}
