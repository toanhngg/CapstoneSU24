package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.B02.B02_GetListReport;
import fpt.CapstoneSU24.dto.ReportDetailDto;
import fpt.CapstoneSU24.model.ImageReport;
import fpt.CapstoneSU24.model.Report;
import fpt.CapstoneSU24.repository.ReportRepository;
import fpt.CapstoneSU24.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private EpochDate epochDate;


    public Page<B02_GetListReport> getListReports(String code,
                                                  String title,
                                                  Integer reportBy,
                                                  Integer type,
                                                  long dateFrom,
                                                  long dateTo,
                                                  Integer status,
                                                  String orderBy,
                                                  Boolean isAsc,
                                                  int page,
                                                  int size) {
        try {
            // Sort theo filter
            Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);

            // Tạo request
            Pageable pageable = PageRequest.of(page, size, sort);


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

        Map<Integer, String> componentMap = DataUtils.getComponentMapping();
        int componentCode = report.getComponent();
        String componentName = componentMap.getOrDefault(componentCode, "Other");
        listReport.setCode("[ " + componentName + " ]" + report.getCode());

        listReport.setTitle(report.getTitle());
        return listReport;
    }

    public ReportDetailDto getDetailReport(int reportId)
    {
        ReportDetailDto reportDetailDto = new ReportDetailDto();
               Report report = reportRepository.getOneByreportId(reportId);
        Map<String, String> imageReport = new HashMap<>();

        for (ImageReport imageReportItem : report.getImageReports()) {
            imageReport.put(String.valueOf(imageReportItem.getId()), imageReportItem.getImageName());
        }
        //mapping component
        Map<Integer, String> componentMap = DataUtils.getComponentMapping();
        int componentCode = report.getComponent();
        String componentName = componentMap.getOrDefault(componentCode, "Other");

        reportDetailDto.setReportId(report.getReportId());
        reportDetailDto.setCode(report.getCode());
        reportDetailDto.setComponent(componentName);
        reportDetailDto.setCreateBy(report.getCreateBy());
        String getReportDate = epochDate.dateTimeToString(epochDate.epochToDate(report.getCreateOn()), "dd-MM-yyyy");
        reportDetailDto.setCreateOn(getReportDate);
        reportDetailDto.setPriority(report.getPriority());
        reportDetailDto.setStatus(report.getStatus());
        reportDetailDto.setTitle(report.getTitle());
        reportDetailDto.setType(report.getType());
        String getReportUpdateOn = epochDate.dateTimeToString(epochDate.epochToDate(report.getUpdateOn()), "dd-MM-yyyy");
        reportDetailDto.setUpdateOn(getReportUpdateOn);
        reportDetailDto.setReportTo(report.getReportTo().getEmail());
        reportDetailDto.setReportImage(imageReport);
        return reportDetailDto;
    }
}
