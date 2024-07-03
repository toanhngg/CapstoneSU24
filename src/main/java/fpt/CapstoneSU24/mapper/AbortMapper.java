package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.AbortDTO;
import fpt.CapstoneSU24.model.ItemLog;
import org.mapstruct.Mapper;

    @Mapper(componentModel = "spring")
    public interface AbortMapper {
        ItemLog abortDTOToItemLog(AbortDTO abortDTO);
    }

