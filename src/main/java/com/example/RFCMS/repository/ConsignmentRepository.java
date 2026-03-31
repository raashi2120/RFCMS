package com.example.RFCMS.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.RFCMS.models.Consignment;

public interface ConsignmentRepository extends MongoRepository<Consignment, String> {
}