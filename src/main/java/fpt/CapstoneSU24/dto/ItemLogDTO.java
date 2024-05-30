package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.*;
import lombok.Data;

@Data
public class ItemLogDTO {

        private Location location;
        private Origin origin;
        private Item item;
        private Party party;
        private ItemLog itemLog;
    }
