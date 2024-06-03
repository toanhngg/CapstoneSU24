package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.User;
import fpt.CapstoneSU24.repository.ItemRepository;
import fpt.CapstoneSU24.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    @Autowired
    private ItemRepository itemRepository;

    /*
     * type is sort type: "desc" or "asc"
     * default data startDate and endDate equal 0 (need insert 2 data)
     * */
    @PostMapping("/search")
    public ResponseEntity searchItem(@RequestBody String req) {
        JSONObject jsonReq = new JSONObject(req);
        int pageNumber = jsonReq.getInt("pageNumber");
        int pageSize = jsonReq.getInt("pageSize");
        long startDate = jsonReq.getLong("startDate");
        long endDate = jsonReq.getLong("endDate");
        Page<Item> items = null;
        Pageable pageable = jsonReq.getString("type").equals("desc") ? PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")) :
                jsonReq.getString("currentOwner").equals("asc") ? PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createdAt")) :
                        PageRequest.of(pageNumber, pageSize);
//        Page<Item> items = jsonReq.getString("type") == null? itemRepository.findAll(pageable) : jsonReq.getString("type").equals("desc") ? itemRepository.sortItemsByCreatedAtDesc(pageable) :  itemRepository.sortItemsByCreatedAtAsc(pageable);
        if (startDate != 0 && endDate != 0) {
             items = itemRepository.findByCreatedAtBetween(startDate, endDate, pageable);

        }else {
             items = jsonReq.getString("currentOwner").equals("") ? itemRepository.findAll(pageable) : itemRepository.findAllByCurrentOwnerContaining(jsonReq.getString("currentOwner"), pageable);
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}
