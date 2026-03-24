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
    private double FreightCharge; 

    private String status;

    // relationships (REFERENCE)
    private String userId;
    private String wagonId;

    // tracking + billing
    private String bookingTimestamp;
    private String deliveryTimestamp;
    private float delay;
    private float demurrageCharge;
    private double distance = 1400;
    
    // source to destination computation logic
    /*
    
    
    
    
    
    */

    public double getDistance(){
        return this.distance;
    }

    public float getWeight(){
        return this.weight;
    }

    public String getPrioty(){
        return this.priority;
    }

    public void setFreight(double FreightCharge){
        this.FreightCharge = FreightCharge; 
    }

    public void setStatus(String status){
        this.status = status; 
    }
}
