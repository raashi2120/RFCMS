package com.example.RFCMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.RFCMS.models.Consignment;
import com.example.RFCMS.repository.ConsignmentRepository;
import com.example.RFCMS.service.ConsignmentService;


@RestController
@RequestMapping("/api/consignments")
public class ConsignmentController {

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    private ConsignmentService consignmentService;

    @PostMapping("/add")
    public Consignment addConsignment(@RequestBody Consignment c) {
        return consignmentRepository.save(c);
    }

    @PostMapping("/book")
    public Consignment book(@RequestBody Consignment c) {   
        return consignmentService.bookConsignment(c);
    }
}
