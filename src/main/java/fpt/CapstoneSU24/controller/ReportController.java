package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.B02.B02_GetListReport;
import fpt.CapstoneSU24.dto.B02.B02_RequestFilterTable;
import fpt.CapstoneSU24.dto.ReportDetailDto;
import fpt.CapstoneSU24.repository.ReportRepository;
import fpt.CapstoneSU24.service.ReportService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@RequestMapping("/api/report")
public class ReportController {

   // private final ReportRepository reportRepository;
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportRepository reportRepository, ReportService reportService) {
       // this.reportRepository = reportRepository;
        this.reportService = reportService;
    }

    @PostMapping("/getListReports")
    public ResponseEntity<Page<B02_GetListReport>> getListReports(@RequestBody B02_RequestFilterTable requestFilter) {

        long dateFromEpoch = requestFilter.getDateFrom() != null ? requestFilter.getDateFrom().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() : 0;
        long dateToEpoch = requestFilter.getDateTo() != null ? requestFilter.getDateTo().atStartOfDay(ZoneId.systemDefault()).toEpochSecond() : 0;

        Integer  reportBy = null;
        Integer type = null;
        Integer status = null;

        if (requestFilter.getReportBy() > -1) {
            reportBy = -1;
        }
        if (requestFilter.getType() > -1) {
            type = -1;
        }
        if (requestFilter.getStatus()  > -1) {
            status = -1;
        }

        Page<B02_GetListReport> b02GetListReports = reportService.getListReports(
                requestFilter.getCode(),
                requestFilter.getTitle(),
                reportBy,
                type,
                dateFromEpoch,
                dateToEpoch,
                status,
                requestFilter.getOrderBy(),
                requestFilter.getAsc(),
                requestFilter.getPage(),
                requestFilter.getSize());
        return  ResponseEntity.ok(b02GetListReports);

    }


    @PostMapping("/getReportById")
    public ResponseEntity<?> getDetailReport(@RequestBody String req)
    {
        ReportDetailDto reportDetail = new ReportDetailDto();
        JSONObject jsonObject = new JSONObject(req);
        int reportId = jsonObject.has("reportId") ? jsonObject.getInt("reportId") : -1;
        reportDetail = reportService.getDetailReport(reportId);
        return ResponseEntity.ok(reportDetail);
    }


}
