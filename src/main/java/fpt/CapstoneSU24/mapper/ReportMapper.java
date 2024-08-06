package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.ReportDTO.ReportListDTO;
import fpt.CapstoneSU24.model.Report;

import java.util.stream.Collectors;

public class ReportMapper {
    public ReportListDTO convertToDTO(Report report) {
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
                    imageDto.setUrl(imageReport.getImagePath());
                    return imageDto;
                }).collect(Collectors.toList()));
        dto.setItemId(report.getItemId().getProductRecognition());
        dto.setProductName(report.getItemId().getProduct().getProductName());
        return dto;
    }
}
