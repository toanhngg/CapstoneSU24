package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Item;
import fpt.CapstoneSU24.model.Member;
import fpt.CapstoneSU24.repository.ItemRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {
    @Autowired
    ItemRepository itemRepository;
    @GetMapping("/getAllItem")
    public ResponseEntity test() {
        List<Item> listItem = itemRepository.findAll();
        return ResponseEntity.ok(listItem);
    }
    @PostMapping("/getItemById")
    public ResponseEntity getItemById(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        Item listItem = itemRepository.findOneById(jsonObject.getInt("id"));
        return ResponseEntity.ok(listItem);
    }
}
