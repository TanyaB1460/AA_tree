package org.example;

import java.util.ArrayList;
import java.util.List;

public class AATree {
    private static class Node {
        Student student;
        int level;
        Node left, right;

        Node(Student student) {
            this.student = student;
            this.level = 1;
        }
    }

    private Node root;

    // Вставка студента
    public void insert(Student student) {
        root = insert(root, student);
    }

    private Node insert(Node node, Student student) {
        if (node == null) return new Node(student);

        if (student.getId() < node.student.getId()) {
            node.left = insert(node.left, student);
        } else if (student.getId() > node.student.getId()) {
            node.right = insert(node.right, student);
        } else {
            node.student = student; // Обновить данные, если ID совпадает
        }

        node = skew(node);
        node = split(node);
        return node;
    }

    // Удаление по ID
    public void delete(int id) {
        root = delete(root, id);
    }

    private Node delete(Node node, int id) {
        if (node == null) return null;

        if (id < node.student.getId()) {
            node.left = delete(node.left, id);
        } else if (id > node.student.getId()) {
            node.right = delete(node.right, id);
        } else {
            if (node.left == null && node.right == null) return null;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node successor = findMin(node.right);
            node.student = successor.student;
            node.right = delete(node.right, successor.student.getId());
        }

        node = decreaseLevel(node);
        node = skew(node);
        if (node.right != null) node.right = skew(node.right);
        node = split(node);
        if (node.right != null) node.right = split(node.right);
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Поиск по ID
    public Student search(int id) {
        Node node = root;
        while (node != null) {
            if (id < node.student.getId()) node = node.left;
            else if (id > node.student.getId()) node = node.right;
            else return node.student;
        }
        return null;
    }

    // Поиск студентов с GPA > minGpa (конкретная задача)
    public List<Student> findStudentsWithGpaGreaterThan(double minGpa) {
        List<Student> result = new ArrayList<>();
        inOrderTraversal(root, minGpa, result);
        return result;
    }

    private void inOrderTraversal(Node node, double minGpa, List<Student> result) {
        if (node == null) return;
        inOrderTraversal(node.left, minGpa, result);
        if (node.student.getGpa() > minGpa) result.add(node.student);
        inOrderTraversal(node.right, minGpa, result);
    }

    // Балансировка
    private Node skew(Node node) {
        if (node == null || node.left == null) return node;
        if (node.left.level == node.level) {
            Node left = node.left;
            node.left = left.right;
            left.right = node;
            return left;
        }
        return node;
    }

    private Node split(Node node) {
        if (node == null || node.right == null || node.right.right == null) return node;
        if (node.level == node.right.right.level) {
            Node right = node.right;
            node.right = right.left;
            right.left = node;
            right.level++;
            return right;
        }
        return node;
    }

    private Node decreaseLevel(Node node) {
        int leftLevel = node.left != null ? node.left.level : 0;
        int rightLevel = node.right != null ? node.right.level : 0;
        int correctLevel = Math.min(leftLevel, rightLevel) + 1;
        if (correctLevel < node.level) {
            node.level = correctLevel;
            if (node.right != null && node.right.level > correctLevel) {
                node.right.level = correctLevel;
            }
        }
        return node;
    }
}
