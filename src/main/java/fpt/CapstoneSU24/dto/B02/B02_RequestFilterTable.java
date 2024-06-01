package fpt.CapstoneSU24.dto.B02;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class B02_RequestFilterTable {
    private String code;
    private String title;
    private Integer reportBy;
    private Integer type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;

    private Integer status;
    private String orderBy = "reportId";
    private Boolean isAsc = true;
    private int page = 0;
    private int size = 10;

    public B02_RequestFilterTable() {
    }

    public B02_RequestFilterTable(String code, String title, int reportBy, int type, LocalDate dateFrom, LocalDate dateTo, int status, String orderBy, Boolean isAsc, int page, int size) {
        this.code = code;
        this.title = title;
        this.reportBy = reportBy;
        this.type = type;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.status = status;
        this.orderBy = orderBy;
        this.isAsc = isAsc;
        this.page = page;
        this.size = size;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReportBy() {
        return reportBy;
    }

    public void setReportBy(int reportBy) {
        this.reportBy = reportBy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getAsc() {
        return isAsc;
    }

    public void setAsc(Boolean asc) {
        isAsc = asc;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
