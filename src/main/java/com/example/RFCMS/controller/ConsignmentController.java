package com.example.RFCMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.RFCMS.models.Consignment;
import com.example.RFCMS.repository.ConsignmentRepository;

@RestController
@RequestMapping("/api/consignments")
public class ConsignmentController {

    @Autowired
    private ConsignmentRepository repository;

    @PostMapping("/add")
    public Consignment addConsignment(@RequestBody Consignment c) {
        return repository.save(c);
    }
}
