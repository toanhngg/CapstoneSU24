package fpt.CapstoneSU24.mapper;

import fpt.CapstoneSU24.dto.AbortDTO;
import fpt.CapstoneSU24.dto.ItemLogDTO;
import fpt.CapstoneSU24.dto.UserVerifyDTOResponse;
import fpt.CapstoneSU24.model.ItemLog;
import fpt.CapstoneSU24.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper
public interface ItemLogMapper {
    ItemLogMapper INSTANCE = Mappers.getMapper(ItemLogMapper.class);

 //   AbortDTO itemLogToAbortDTO(ItemLog itemLog);

}
