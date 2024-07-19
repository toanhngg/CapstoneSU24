package fpt.CapstoneSU24.controller;

import fpt.CapstoneSU24.dto.payload.CreateCategoryRequest;
import fpt.CapstoneSU24.dto.payload.EditCategoryRequest;
import fpt.CapstoneSU24.dto.payload.FilterSearchForCategoryRequest;
import fpt.CapstoneSU24.dto.payload.IdRequest;
import fpt.CapstoneSU24.service.CategoryService;
import fpt.CapstoneSU24.service.ELKService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/elk")

public class ELKController {
    @Autowired
    ELKService elkService;
    @GetMapping("/getNumberVisitsDiagram")
    public ResponseEntity<?> findAll() throws IOException {
        return elkService.getNumberVisitsDiagram();
    }
    @GetMapping("/getNumberVisitsAllTime")
    public ResponseEntity<?> getNumberVisitsAllTime() throws IOException {
        return elkService.getNumberVisitsAllTime();
    }
}
