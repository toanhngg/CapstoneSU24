package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.repository.OriginRepository;
import fpt.CapstoneSU24.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/origin")
public class OriginController {

    private final OriginRepository originRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OriginController(OriginRepository originRepository, ProductRepository productRepository) {
        this.originRepository = originRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        List<Origin> originList = originRepository.findAll();
        return ResponseEntity.ok(originList);
    }
//    @PostMapping("/findByIdProduct")
//    public ResponseEntity findOriginByProduct(@RequestBody String req) {
//        JSONObject jsonReq = new JSONObject(req);
//        List<Origin> originList = originRepository.findAllByProductId(jsonReq.getInt("productId"));
//        return ResponseEntity.ok(originList);
//    }
}
