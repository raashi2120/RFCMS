package com.example.RFCMS.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RailwayDijkstra {

    static class Edge {
        String dest;
        int weight;

        Edge(String d, int w) {
            dest = d;
            weight = w;
        }
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    // -----------------------------
    // BUILD GRAPH
    // -----------------------------
    public Map<String, List<Edge>> buildGraph() {
        Map<String, List<Edge>> graph = new HashMap<>();

        List<Document> edges = mongoTemplate.findAll(Document.class, "graph");

        for (Document doc : edges) {
            String src = doc.getString("source");
            String dest = doc.getString("destination");
            int dist = doc.getInteger("distance");

            graph.putIfAbsent(src, new ArrayList<>());
            graph.putIfAbsent(dest, new ArrayList<>()); // ✅ IMPORTANT

            graph.get(src).add(new Edge(dest, dist));
        }

        return graph;
    }

    // -----------------------------
    // DIJKSTRA
    // -----------------------------
    public Map<String, Integer> dijkstra(Map<String, List<Edge>> graph,
            String start,
            Map<String, String> parent) {

        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

        Map<String, Integer> dist = new HashMap<>();

        dist.put(start, 0);
        pq.add(new AbstractMap.SimpleEntry<>(start, 0));

        while (!pq.isEmpty()) {
            String curr = pq.poll().getKey();

            for (Edge edge : graph.getOrDefault(curr, new ArrayList<>())) {
                int newDist = dist.get(curr) + edge.weight;

                if (newDist < dist.getOrDefault(edge.dest, Integer.MAX_VALUE)) {
                    dist.put(edge.dest, newDist);
                    parent.put(edge.dest, curr);
                    pq.add(new AbstractMap.SimpleEntry<>(edge.dest, newDist));
                }
            }
        }

        return dist;
    }

    // -----------------------------
    // GET PATH
    // -----------------------------
    public List<String> getPath(Map<String, String> parent, String end) {
        List<String> path = new ArrayList<>();

        while (end != null) {
            path.add(end);
            end = parent.get(end);
        }

        Collections.reverse(path);
        return path;
    }

    // -----------------------------
    // MAIN LOGIC METHOD
    // -----------------------------
    public void run(String start, String end) {
        Map<String, List<Edge>> graph = buildGraph();

        // ✅ ADD THIS LINE HERE
        System.out.println("GRAPH: " + graph);

        Map<String, String> parent = new HashMap<>();
        Map<String, Integer> dist = dijkstra(graph, start, parent);

        if (!dist.containsKey(end) || dist.get(end) == Integer.MAX_VALUE) {
            System.out.println("No path found.");
        } else {
            List<String> path = getPath(parent, end);
            System.out.println("Shortest Distance: " + dist.get(end) + " km");
            System.out.println("Path: " + String.join(" -> ", path));
        }
    }
}