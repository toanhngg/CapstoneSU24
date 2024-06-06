package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.payload.FilterByTimeStampRequest;
import fpt.CapstoneSU24.payload.FilterSearchRequest;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import fpt.CapstoneSU24.service.ExportExcelService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    ExportExcelService exportExcelService;
    /*
     * type is sort type: "desc" or "asc"
     * default data startDate and endDate equal 0 (need insert 2 data)
     * */
    @PostMapping("/search")
    public ResponseEntity searchItem(@Valid @RequestBody FilterSearchRequest req) {
        try {
            Page<Item> items = null;
            Pageable pageable = req.getType().equals("desc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt")) :
                    req.getType().equals("asc") ? PageRequest.of(req.getPageNumber(), req.getPageSize(), Sort.by(Sort.Direction.ASC, "createdAt")) :
                            PageRequest.of(req.getPageNumber(), req.getPageSize());
//        Page<Item> items = jsonReq.getString("type") == null? itemRepository.findAll(pageable) : jsonReq.getString("type").equals("desc") ? itemRepository.sortItemsByCreatedAtDesc(pageable) :  itemRepository.sortItemsByCreatedAtAsc(pageable);
            if (req.getStartDate() != 0 && req.getEndDate() != 0) {
                items = itemRepository.findByCreatedAtBetween(req.getStartDate(), req.getEndDate(), pageable);

            } else {
                items = itemRepository.findAllByCurrentOwnerContaining(req.getName(), pageable);
            }
            return ResponseEntity.status(200).body(items);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error when fetching data");
        }

    }

    @PostMapping("/exportListItem")
    public ResponseEntity<byte[]>  exportListItem(@Valid @RequestBody FilterByTimeStampRequest req) throws IOException {
        JSONObject jsonReq = new JSONObject(req);
        List<Item> items = itemRepository.findByCreatedAtBetween(req.getStartDate(), req.getEndDate());
        byte[] excelBytes = exportExcelService.exportItemToExcel(items);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "exported_data.xlsx");
        return new ResponseEntity<>(excelBytes, headers, 200);
    }
}
