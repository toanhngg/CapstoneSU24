package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.OrgNameUserDTO;
import fpt.CapstoneSU24.model.Origin;
import fpt.CapstoneSU24.service.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.util.Elements;
import java.util.List;

@RestController
@RequestMapping("/api/origin")
public class OriginController {

    private final OriginService originService;

    @Autowired
    public OriginController(OriginService originService) {
        this.originService = originService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return originService.findAll();

    }
//    @GetMapping("/top5OrgNames")
//    public ResponseEntity<List<OrgNameUserDTO>> getTop5OrgNames() {
//        List<OrgNameUserDTO> topOrgNames = originService.getTop5OrgNames();
//        return ResponseEntity.ok(topOrgNames);
//    }
//    @PostMapping("/findByIdProduct")
//    public ResponseEntity findOriginByProduct(@RequestBody String req) {
//        JSONObject jsonReq = new JSONObject(req);
//        List<Origin> originList = originRepository.findAllByProductId(jsonReq.getInt("productId"));
//        return ResponseEntity.ok(originList);
//    }
}
