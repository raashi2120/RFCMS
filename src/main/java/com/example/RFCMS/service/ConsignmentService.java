package com.example.RFCMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import com.example.RFCMS.models.Consignment;
import com.example.RFCMS.repository.ConsignmentRepository;

@Service
public class ConsignmentService {

    @Autowired
    private FreightService freightService;

    @Autowired
    private ConsignmentRepository consignmentRepository;
    
    public Consignment bookConsignment(Consignment c){

        

        double FreightCharge = freightService.calculateFreight(c);
        c.setFreightCharge(FreightCharge);
        c.setStatus("BOOKED");
        c.setBookingTimestamp(LocalTime.now().toString()); 

        return consignmentRepository.save(c);

    }

    
}
