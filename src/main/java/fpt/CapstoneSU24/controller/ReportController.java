package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.ReportDTO.CreateReportRequest;
import fpt.CapstoneSU24.dto.ReportDTO.ReplyReportRequest;
import fpt.CapstoneSU24.dto.ReportDTO.RequestListReport;
import fpt.CapstoneSU24.dto.ReportDTO.ReportListDTO;
import fpt.CapstoneSU24.service.ReportService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/getListReports")
    public ResponseEntity<?> getListReports(@RequestBody RequestListReport requestFilter) {
        return reportService.getListReports(requestFilter);
    }

    @PostMapping("/createReport")
    public ResponseEntity<?> createReport(@RequestBody CreateReportRequest request) throws MessagingException {
        return reportService.createReport(request);
    }

    @PostMapping("/replyReport")
    public ResponseEntity<?> replyReport(@RequestBody ReplyReportRequest request) throws MessagingException {
        return reportService.replyReport(request);
    }




}
