package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.B02.B02_GetListReport;
import fpt.CapstoneSU24.model.Report;
import fpt.CapstoneSU24.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Page<B02_GetListReport> getListReports(String code,
                                                  String title,
                                                  int reportBy,
                                                  int type,
                                                  long dateFrom,
                                                  long dateTo,
                                                  int status,
                                                  String orderBy,
                                                  Boolean isAsc,
                                                  int page,
                                                  int size) {
        try {
            // Sort theo filter
            Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);

            // Tạo request
            Pageable pageable = PageRequest.of(page, size, sort);

            // Get data
            Page<Report> reportsPage = reportRepository.findReports(code, title, reportBy, type, dateFrom, dateTo, status, pageable);

            // Mapping
            List<B02_GetListReport> listReports = reportsPage.getContent().stream()
                    .map(this::transformToB02_GetListReport)
                    .collect(Collectors.toList());

            // trả về kiểu page
            return new PageImpl<>(listReports, pageable, reportsPage.getTotalElements());

        } catch (Exception ex) {
            System.err.println("Error in report: " + ex.getMessage());
            ex.printStackTrace();
        }
        return Page.empty(PageRequest.of(page, size, Sort.by(orderBy)));
    }

    private B02_GetListReport transformToB02_GetListReport(Report report) {
        B02_GetListReport listReport = new B02_GetListReport();
        listReport.setReportId(report.getReportId());
        listReport.setStatus(report.getStatus());
        //code here
        return listReport;
    }
}
