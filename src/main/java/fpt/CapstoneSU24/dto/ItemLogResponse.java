package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.*;

import java.util.List;

public class ItemLogResponse {
    private final List<ItemLogDTO> itemLogDTOs;
   // private ItemDTO item;

    public ItemLogResponse(/*ItemDTO item,*/ List<ItemLogDTO> itemLogDTOs) {
        //this.item = item;
        this.itemLogDTOs = itemLogDTOs;
    }

    public List<ItemLogDTO> getItemLogDTOs() {
        return itemLogDTOs;
    }

//    public ItemDTO getItem() {
//        return item;
//    }
//
//    public void setItem(ItemDTO item) {
//        this.item = item;
//    }
}