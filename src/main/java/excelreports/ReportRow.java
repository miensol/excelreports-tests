package excelreports;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReportRow implements Iterable<ReportCell> {
    List<ReportCell> values;
    private int index;
    private short rowCellStyle;

    ReportRow() {
        this.values = new ArrayList<>();
    }

    public ReportRow addValues(Object[] values) {
        for(Object value: values){
            this.values.add(new ReportCell(value, this.values.size()));
        }
        return this;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Iterator<ReportCell> iterator() {
        return values.iterator();
    }


    public ReportRow withBorder() {
        for(ReportCell value: values){
            value.withBorder();
        }
        return this;
    }
}
