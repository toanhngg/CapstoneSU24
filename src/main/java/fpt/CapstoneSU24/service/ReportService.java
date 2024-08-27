package fpt.CapstoneSU24.service;

import fpt.CapstoneSU24.dto.DataMailDTO;
import fpt.CapstoneSU24.dto.ReportDTO.CreateReportRequest;
import fpt.CapstoneSU24.dto.ReportDTO.ReplyReportRequest;
import fpt.CapstoneSU24.dto.ReportDTO.RequestListReport;
import fpt.CapstoneSU24.dto.ReportDTO.ReportListDTO;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.repository.PartyRepository;
import fpt.CapstoneSU24.repository.ReportRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.util.Const;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final EpochDate epochDate;
    private final ClientService clientService;
    @Autowired
    public ReportService(ReportRepository reportRepository,EpochDate epochDate, ClientService clientService){
        this.epochDate = epochDate;
        this.reportRepository = reportRepository;
        this.clientService = clientService;
    }
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private EmailService emailService;



    public ResponseEntity<?> getListReports(RequestListReport requestFilterTable) {
        Sort.Direction direction = requestFilterTable.getAsc() ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, requestFilterTable.getOrderBy());
        Pageable pageable = PageRequest.of(requestFilterTable.getPage(), requestFilterTable.getSize(), sort);

        Page<Report> reports = reportRepository.findReports(
                requestFilterTable.getCode(),
                requestFilterTable.getTitle(),
                requestFilterTable.getReportTo(),
                requestFilterTable.getType(),
                requestFilterTable.getDateFrom(),
                requestFilterTable.getDateTo(),
                requestFilterTable.getStatus(),
                requestFilterTable.getEmailReport(),
                requestFilterTable.getProductId(),
                pageable
        );

        Page<ReportListDTO> reportListDTOs = reports.map(this::convertToDTO);

        return ResponseEntity.ok(reportListDTOs);
    }

    private ReportListDTO convertToDTO(Report report) {
        ReportListDTO dto = new ReportListDTO();
        dto.setId(report.getReportId());
        dto.setCreateOn(report.getCreateOn());
        dto.setUpdateOn(report.getUpdateOn());
        dto.setCode(report.getCode());
        dto.setTitle(report.getTitle());
        dto.setType(report.getType());
        dto.setStatus(report.getStatus());
        dto.setPriority(report.getPriority());
        dto.setCreateBy(report.getCreateBy().getEmail());

        ReportListDTO.ReportTo reportToDto = new ReportListDTO.ReportTo();
        reportToDto.setName(report.getReportTo().getFirstName());
        dto.setReportTo(reportToDto);

        dto.setComponent(report.getComponent());
        dto.setCauseDetail(report.getCauseDetail());
        dto.setResponseDetail(report.getResponseDetail());
        dto.setImageReports(report.getImageReports().stream()
                .map(imageReport -> {
                    ReportListDTO.ImageReport imageDto = new ReportListDTO.ImageReport();
                    imageDto.setId(imageReport.getId());
                    imageDto.setUrl(cloudinaryService.getImageUrl(imageReport.getImagePath()));
                    return imageDto;
                }).collect(Collectors.toList()));
        dto.setItemId(report.getItemId().getProductRecognition());
        dto.setProductName(report.getItemId().getProduct().getProductName());
        return dto;
    }


    public ResponseEntity<?> createReport(CreateReportRequest request) throws MessagingException {

        int check = clientService.checkOTP(request.getCreateBy(), request.getOtp(), request.getProductCode());
        if(check != 3 && check != 8 )
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Không thể tìm thấy OTP hoặc OTP không hợp lệ");
        }
        Report report = new Report();
//        Party party = partyRepository.findByPartyEmail(request.getCreateBy());
//        if (party == null) {

        Party    party = new Party();
            party.setEmail(request.getCreateBy());
            party.setPhoneNumber("99999");
            party.setDescription("Party chi tao report");
            partyRepository.save(party);
//        }
        Item item = itemRepository.findByProductRecognition(request.getProductCode());
        User user = item.getProduct().getManufacturer();
        report.setReportTo(user);
        report.setCreateBy(partyRepository.findOneByPartyId(party.getPartyId()));
        report.setItemId(item);
        report.setCreateOn(System.currentTimeMillis());
        report.setUpdateOn(System.currentTimeMillis());
        report.setTitle(request.getTitle());
        report.setType(request.getType());
        report.setComponent(request.getComponent());
        report.setCauseDetail(getComponentLabel(request.getComponent()) + ": \n" +request.getCauseDetail());
        report.setStatus(0);
        report.setPriority(request.getPriority());

        String reportCode = item.getProductRecognition() + "-" + reportRepository.countItem(item.getItemId());
        report.setCode(reportCode);

        Report savedReport = reportRepository.save(report); // Lưu báo cáo trước để có ID hợp lệ

        List<ImageReport> imageReports = request.getImageReports().stream()
                .map(imageData -> {
                    try {
                        String filePath = cloudinaryService.uploadImageAndGetPublicId(
                                cloudinaryService.convertBase64ToImgFile(imageData), "");
                        ImageReport imageReport = new ImageReport();
                        imageReport.setImagePath(filePath);
                        imageReport.setReport(savedReport); // Gán báo cáo đã lưu trữ
                        return imageReport;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        savedReport.setImageReports(imageReports);


        reportRepository.save(savedReport);

        String subjectMail = "[" + reportCode + "] " + request.getTitle();
        String templateMail = Const.TEMPLATE_FILE_NAME.ANNOUCE_NEW_ISSUE;

        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo(user.getEmail());
        dataMail.setSubject(subjectMail);

        Map<String, Object> props = new HashMap<>();
        props.put("recipientName", user.getFirstName());
        props.put("senderName", report.getCreateBy().getEmail());
        props.put("title", request.getTitle());
        props.put("component", getComponentLabel(request.getComponent()));
        props.put("priority", getPriorityLabel(request.getPriority()));
        props.put("causeDetail", request.getCauseDetail());
        props.put("imageReports", imageReports.stream()
                .map(imageReport -> cloudinaryService.getImageUrl(imageReport.getImagePath()))
                .collect(Collectors.toList()));
        props.put("reportLink", Const.ClientServer.DeployServer + "manufacturer/reportManager/" + report.getReportId());
        dataMail.setProps(props);

        emailService.sendHtmlMail(dataMail, templateMail);

        return ResponseEntity.ok(savedReport);
    }


    private String getPriorityLabel(int priority) {
        switch (priority) {
            case 0:
                return "Low";
            case 1:
                return "Medium";
            case 2:
                return "High";
            case 3:
                return "Very High";
            default:
                return "Unknown";
        }
    }

    private String getComponentLabel(int component) {
        switch (component) {
            case 1:
                return "Sản phẩm không đúng mô tả (sai màu, kích thước, v.v.)";
            case 2:
                return "Sản phẩm bị hỏng/móp méo hoặc thiếu phụ kiện";
            case 3:
                return "Không hoạt động/lỗi kỹ thuật/quá hạn sử dụng";
            case 4:
                return "Sản phẩm giao chậm";
            case 5:
                return "Không có tem bảo hành hoặc cần thông tin bảo hành";
            case 6:
                return "Đổi/trả sản phẩm hoặc cần hướng dẫn sử dụng";
            case 7:
                return "Tư vấn sản phẩm tương tự";
            case 8:
                return "Khác...";
            default:
                return "Unknown";
        }
    }

    public ResponseEntity<String> replyReport(ReplyReportRequest request) throws MessagingException {
        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> new RuntimeException("Report not found"));

        Party user = report.getCreateBy();

        report.setStatus(1);
        report.setResponseDetail(request.getResponseDetail());
        reportRepository.save(report);


        String subjectMail = "Phản hồi về vấn đề [" + report.getCode() + "] " + report.getTitle();
        String templateMail = Const.TEMPLATE_FILE_NAME.REPLY_ISSUE;

        DataMailDTO dataMail = new DataMailDTO();
        dataMail.setTo(user.getEmail());
        dataMail.setSubject(subjectMail);

        Map<String, Object> props = new HashMap<>();
        props.put("userName", user.getEmail());
        props.put("title", report.getTitle());
        props.put("responseDetail", request.getResponseDetail());
        dataMail.setProps(props);


        emailService.sendHtmlMail(dataMail, templateMail);

        return ResponseEntity.ok("Reply sent successfully");
    }





}
