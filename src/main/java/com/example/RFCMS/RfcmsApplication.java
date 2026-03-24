package com.example.RFCMS;

import com.example.RFCMS.service.RailwayDijkstra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class RfcmsApplication implements CommandLineRunner {

    @Autowired
    private RailwayDijkstra dijkstra;

    public static void main(String[] args) {
        SpringApplication.run(RfcmsApplication.class, args);
    }

    @Override
    public void run(String... args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter source city: ");
        String start = sc.nextLine();

        System.out.print("Enter destination city: ");
        String end = sc.nextLine();

        dijkstra.run(start, end);
    }
}