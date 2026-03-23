package com.example.RFCMS.models;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "wagons")
public class Wagon {

    @Id
    private String id;

    private float capacity;
    private float remainingCapacity;

    private String currentStation;
    private String status;

    // tracking support
    private double currentLatitude;
    private double currentLongitude;
}
