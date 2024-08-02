package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.CustomerCareRponseDTO;
import fpt.CapstoneSU24.dto.ListSupportDTOResponse;
import fpt.CapstoneSU24.dto.SubSupportDTOResponse;
import fpt.CapstoneSU24.dto.ViewProductDTOResponse;
import fpt.CapstoneSU24.model.*;
import fpt.CapstoneSU24.repository.ImageSupportSystemRepository;
import fpt.CapstoneSU24.repository.SupportSystemRepository;
import fpt.CapstoneSU24.service.CloudinaryService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class SupportSystemMapper {
    @Autowired
    private SupportSystemRepository supportSystemRepository;
    @Autowired
    private ImageSupportSystemRepository imageSupportSystemRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Mapping(source = "supportSystemId", target = "supportSystemId")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(source = "supporterName", target = "supporterName")
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    @Mapping(target = "subSupport", ignore = true)
    public abstract ListSupportDTOResponse supportSystemToListSupportDTOResponse(SupportSystem supportSystem);
    @AfterMapping
    protected void setAfter(SupportSystem supportSystem, @MappingTarget ListSupportDTOResponse listSupportDTOResponse) {
        listSupportDTOResponse.setEmail(supportSystem.getUser().getEmail());
        listSupportDTOResponse.setPhoneNumber(supportSystem.getUser().getPhone());

    }
    @AfterMapping
    protected void setSubSupport(SupportSystem supportSystem, @MappingTarget ListSupportDTOResponse listSupportDTOResponse) {
        List<SupportSystem> supportSystems = supportSystemRepository.findAllByReplyId(supportSystem.getSupportSystemId());
        List<SubSupportDTOResponse> subSupportDTOResponses = new ArrayList<>();

        List<String> initImages = imageSupportSystemRepository.findImagesBySupSystemIdAndType(supportSystem.getSupportSystemId(), 0).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());;
        List<String> initSupportImages = imageSupportSystemRepository.findImagesBySupSystemIdAndType(supportSystem.getSupportSystemId(), 1).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());;
        subSupportDTOResponses.add(new SubSupportDTOResponse(supportSystem.getSupportSystemId(), supportSystem.getContent(), initImages, supportSystem.getTimestamp(), supportSystem.getSupportContent(), initSupportImages, supportSystem.getSupportTimestamp()));

        if(!supportSystems.isEmpty()){
            for (SupportSystem i: supportSystems) {
                List<String> images = imageSupportSystemRepository.findImagesBySupSystemIdAndType(i.getSupportSystemId(), 0).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());;
                List<String> supportImages = imageSupportSystemRepository.findImagesBySupSystemIdAndType(i.getSupportSystemId(), 1).stream().map(filePath -> cloudinaryService.getImageUrl(filePath)).collect(Collectors.toList());;
                subSupportDTOResponses.add(new SubSupportDTOResponse(i.getSupportSystemId(), i.getContent(), images, i.getTimestamp(), i.getSupportContent(), supportImages, i.getSupportTimestamp()));
            }
        }
        listSupportDTOResponse.setSubSupport(subSupportDTOResponses);
    }
}