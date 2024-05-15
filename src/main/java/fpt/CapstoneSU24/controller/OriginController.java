package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.model.Product;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Origin")
public class OriginController {
    @Autowired
    private OriginRepository originRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<Origin> originList = originRepository.findAll();
        return ResponseEntity.ok(originList);
    }
    @PostMapping("/findByIdProduct")
    public ResponseEntity findOriginByProduct(@RequestBody String req) {
        JSONObject jsonReq = new JSONObject(req);
        System.out.println(jsonReq.getInt("productId")+"hehe");
        List<Origin> originList = originRepository.findAllByProductId(jsonReq.getInt("productId"));
        return ResponseEntity.ok(originList);
    }
}
