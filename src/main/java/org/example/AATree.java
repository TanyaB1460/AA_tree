package org.example;

public class AATree {
    private static class Node {
        int value, level;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.level = 1;
            this.left = this.right = null;
        }
    }

    private Node root = null;

    public void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node node, int value) {
        if (node == null) return new Node(value);

        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }

        node = skew(node);
        node = split(node);
        return node;
    }

    public void delete(int value) {
        root = delete(root, value);
    }

    private Node delete(Node node, int value) {
        if (node == null) return null;

        if (value < node.value) {
            node.left = delete(node.left, value);
        } else if (value > node.value) {
            node.right = delete(node.right, value);
        } else {
            if (node.left == null && node.right == null) return null;
            else if (node.left == null) node = node.right;
            else if (node.right == null) node = node.left;
            else {
                Node successor = node.right;
                while (successor.left != null) successor = successor.left;
                node.value = successor.value;
                node.right = delete(node.right, successor.value);
            }
        }

        node = decreaseLevel(node);
        node = skew(node);
        if (node.right != null) {
            node.right = skew(node.right);
            if (node.right.right != null) node.right.right = skew(node.right.right);
        }
        node = split(node);
        if (node.right != null) node.right = split(node.right);
        return node;
    }

    public boolean contains(int value) {
        Node current = root;
        while (current != null) {
            if (value < current.value) current = current.left;
            else if (value > current.value) current = current.right;
            else return true;
        }
        return false;
    }

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
        int shouldBe = Math.min(getLevel(node.left), getLevel(node.right)) + 1;
        if (shouldBe < node.level) {
            node.level = shouldBe;
            if (node.right != null && node.right.level > shouldBe) node.right.level = shouldBe;
        }
        return node;
    }

    private int getLevel(Node node) {
        return node == null ? 0 : node.level;
    }
}
