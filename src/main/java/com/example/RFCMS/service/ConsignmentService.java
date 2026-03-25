package com.example.RFCMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

import com.example.RFCMS.models.Consignment;
import com.example.RFCMS.repository.ConsignmentRepository;
import com.example.RFCMS.service.RailwayDijkstra;


@Service
public class ConsignmentService {

    @Autowired
    private FreightService freightService;

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    private RailwayDijkstra railwayDijkstra;
    
    public Consignment bookConsignment(Consignment c){

        
        double totalDistance = railwayDijkstra.run(c.getSource(),c.getDestination());
        c.setDistance(totalDistance);

        double FreightCharge = freightService.calculateFreight(c);
        c.setFreightCharge(FreightCharge);
        c.setStatus("BOOKED");
        c.setBookingTimestamp(LocalTime.now().toString()); 

        return consignmentRepository.save(c);

    }

    
}
