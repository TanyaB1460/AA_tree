package org.example;

import java.util.*;
import java.io.*;

public class Benchmark {
    public static void main(String[] args) {
        Random rand = new Random();

        int minSize = 100;
        int maxSize = 10000;
        int step = (maxSize - minSize) / 49;

        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            writer.println("Size;Insert(ms);Search(ms);Delete(ms)");

            for (int s = 0; s < 50; s++) {
                int size = minSize + step * s;
                System.out.println("\n=== Набор из " + size + " элементов ===");
                List<Integer> data = new ArrayList<>();
                for (int i = 0; i < size; i++) data.add(rand.nextInt(size * 10));

                AATree tree = new AATree();

                long insertStart = System.nanoTime();
                for (int value : data) tree.insert(value);
                long insertEnd = System.nanoTime();
                double insertTime = (insertEnd - insertStart) / 1_000_000.0;
                System.out.println("Вставка: " + insertTime + " мс");

                long searchStart = System.nanoTime();
                for (int i = 0; i < 100; i++) {
                    int val = data.get(rand.nextInt(data.size()));
                    tree.contains(val);
                }
                long searchEnd = System.nanoTime();
                double searchTime = (searchEnd - searchStart) / 1_000_000.0;
                System.out.println("Поиск: " + searchTime + " мс");

                Collections.shuffle(data);
                long deleteStart = System.nanoTime();
                for (int i = 0; i < size / 4; i++) tree.delete(data.get(i));
                long deleteEnd = System.nanoTime();
                double deleteTime = (deleteEnd - deleteStart) / 1_000_000.0;
                System.out.println("Удаление: " + deleteTime + " мс");

                writer.printf("%d;%.3f;%.3f;%.3f\n", size, insertTime, searchTime, deleteTime);
            }

            System.out.println("\nРезультаты сохранены в файл results.csv");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
