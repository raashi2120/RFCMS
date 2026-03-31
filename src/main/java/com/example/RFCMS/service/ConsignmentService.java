package com.example.RFCMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Duration;
import java.util.Optional;

import com.example.RFCMS.models.Consignment;
import com.example.RFCMS.models.Invoice;
import com.example.RFCMS.repository.ConsignmentRepository;
import com.example.RFCMS.repository.InvoiceRepository;

@Service
public class ConsignmentService {

    @Autowired
    private FreightService freightService;

    @Autowired
    private ConsignmentRepository consignmentRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    // ---------------- BOOK ----------------
    public Consignment bookConsignment(Consignment c){

        double freightCharge = freightService.calculateFreight(c);
        c.setFreightCharge(freightCharge);
        c.setStatus("BOOKED");

        // ✅ FULL DATE + TIME
        c.setBookingTimestamp(LocalDateTime.now());

        // ✅ INIT AS NULL
        c.setActualArrivalTimestamp(null);
        c.setPickupTimestamp(null);

        return consignmentRepository.save(c);
    }

    // ---------------- PICKUP → INVOICE ----------------
    public String generateInvoiceOnPickup(String consignmentId) {

    Optional<Consignment> optional = consignmentRepository.findById(consignmentId);

    if (optional.isEmpty()) {
        return "Consignment not found!";
    }

    Consignment consignment = optional.get();

    // ✅ CHECK ARRIVED
    if (!"ARRIVED".equals(consignment.getStatus())) {
        return "Consignment has not arrived yet!";
    }

    if (consignment.getActualArrivalTimestamp() == null) {
        return "Arrival timestamp missing!";
    }

    // ✅ PICKUP TIME
    LocalDateTime pickupTime = LocalDateTime.now();
    consignment.setPickupTimestamp(pickupTime);

    // ✅ ARRIVAL TIME
    LocalDateTime arrivalTime = consignment.getActualArrivalTimestamp();

    // ✅ STORAGE TIME
    long hoursStored = java.time.Duration.between(arrivalTime, pickupTime).toHours();

    // ✅ DEMURRAGE
    int demurrage = calculateDemurrage(hoursStored);
    consignment.setDemurrageCharge(demurrage);

    // ✅ CHARGES
    double freight = consignment.getFreightCharge();
    double total = freight + demurrage;
    consignment.setFinalAmount(total);

    consignmentRepository.save(consignment);

    // ✅ INVOICE
    Invoice invoice = new Invoice();
    invoice.setConsignmentId(consignmentId);
    invoice.setSource(consignment.getSource());
    invoice.setDestination(consignment.getDestination());
    invoice.setCargoDescription(consignment.getCargoDescription());
    invoice.setFreightCharge(freight);
    invoice.setDemurrageCharge(demurrage);
    invoice.setTotalAmount(total);
    invoice.setGeneratedAt(pickupTime.toString());

    invoiceRepository.save(invoice);

    return formatInvoice(invoice);
}
    // ---------------- DEMURRAGE ----------------
    private int calculateDemurrage(long hoursStored) {

        if (hoursStored <= 24) return 0;

        int days = (int) (hoursStored / 24);  // floor division
        return days * 100;
    }

    // ---------------- INVOICE FORMAT ----------------
    private String formatInvoice(Invoice invoice) {

        return "\n===== FINAL INVOICE =====\n" +
                "Consignment ID: " + invoice.getConsignmentId() + "\n" +
                "Route: " + invoice.getSource() + " → " + invoice.getDestination() + "\n" +
                "Cargo: " + invoice.getCargoDescription() + "\n\n" +

                "Freight Charge: ₹" + invoice.getFreightCharge() + "\n" +
                "Demurrage Charge: ₹" + invoice.getDemurrageCharge() + "\n" +
                "----------------------------\n" +
                "TOTAL AMOUNT: ₹" + invoice.getTotalAmount() + "\n\n" +

                "Generated At: " + invoice.getGeneratedAt() + "\n" +
                "Status: ARRIVED\n" +
                "=========================\n";
    }
}