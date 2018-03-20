package base.datatable;


import com.jfinal.plugin.activerecord.Record;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BOGONm on 16/8/11.
 */
public class DataTablePage implements Serializable {
    private List<Record> data;
    private int pageSize;
    private int pageIndex;
    private int totalPage;
    private int recordsTotal;
    private int recordsFiltered;
    private int draw;
    private List<Object> data1;

    public List<Object> getData1() {
        return data1;
    }

    public void setData1(List<Object> data1) {
        this.data1 = data1;
    }

    public List<Record> getData() {
        return data;
    }

    public void setData(List<Record> data) {
        this.data = data;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }
    public DataTablePage() {
        this.data = null;
        this.draw = 0;
        this.recordsFiltered = 0;
        this.recordsTotal = 0;
    }
    public DataTablePage(List<Record> data, int draw, int recordsTotal, int recordsFiltered,Object a) {
        this.data = data;
        this.draw = draw;
        this.recordsFiltered = recordsFiltered;
        this.recordsTotal = recordsTotal;
    }
    public DataTablePage(List<Object> data, int draw, int recordsTotal, int recordsFiltered) {
        this.data1 = data;
        this.draw = draw;
        this.recordsFiltered = recordsFiltered;
        this.recordsTotal = recordsTotal;
    }
}
