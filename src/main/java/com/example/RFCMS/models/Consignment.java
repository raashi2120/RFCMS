package com.example.RFCMS.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "consignments")
public class Consignment {

    @Id
    private String id;

    private String cargoDescription;
    private String cargoType;
    private float weight;
    private String source;
    private String destination;
    private String priority;

    private String status;

    // relationships (REFERENCE)
    private String userId;
    private String wagonId;

    // tracking + billing
    private String bookingTimestamp;
    private String deliveryTimestamp;
    private float delay;
    private float demurrageCharge;
}
