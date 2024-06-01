package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.B02.B02_GetListReport;
import fpt.CapstoneSU24.dto.B02.B02_RequestFilterTable;
import fpt.CapstoneSU24.dto.B03.B03_GetDataGridDTO;
import fpt.CapstoneSU24.dto.B03.B03_RequestDTO;
import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.model.Role;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.ReportRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.EmailService;
import fpt.CapstoneSU24.service.ReportService;
import fpt.CapstoneSU24.util.Const;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportService reportService;


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
}
