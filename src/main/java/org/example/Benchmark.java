package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Benchmark {
    public static void main(String[] args) {
        final int MIN_SIZE = 100;
        final int MAX_SIZE = 10000;
        final int STEP = 200;
        final int SEARCH_OPERATIONS = 1000;
        final int RUNS_PER_SIZE = 5;

        try (PrintWriter writer = new PrintWriter(new FileWriter("benchmark_results.csv"))) {
            writer.println("Size;Insert(ns);Search(ns);Delete(ns)");

            for (int size = MIN_SIZE; size <= MAX_SIZE; size += STEP) {
                System.out.println("Testing size: " + size);

                double totalInsert = 0;
                double totalSearch = 0;
                double totalDelete = 0;

                for (int run = 0; run < RUNS_PER_SIZE; run++) {
                    AATree tree = new AATree();
                    Random rand = new Random();
                    List<Student> students = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        students.add(new Student(rand.nextInt(size * 10), "Student", rand.nextDouble() * 4.0));
                    }

                    long start = System.nanoTime();
                    for (Student s : students) tree.insert(s);
                    totalInsert += (System.nanoTime() - start);

                    start = System.nanoTime();
                    for (int i = 0; i < SEARCH_OPERATIONS; i++) {
                        tree.search(students.get(rand.nextInt(students.size())).getId());
                    }
                    totalSearch += (System.nanoTime() - start);

                    int deleteCount = size / 4;
                    start = System.nanoTime();
                    for (int i = 0; i < deleteCount; i++) {
                        tree.delete(students.get(i).getId());
                    }
                    totalDelete += (System.nanoTime() - start);
                }

                double avgInsert = totalInsert / (RUNS_PER_SIZE * 1_000_000.0);
                double avgSearch = totalSearch / (RUNS_PER_SIZE * SEARCH_OPERATIONS * 1_000_000.0);
                double avgDelete = totalDelete / (RUNS_PER_SIZE * 1_000_000.0);

                writer.printf("%d;%.6f;%.6f;%.6f\n", size, avgInsert, avgSearch, avgDelete);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}