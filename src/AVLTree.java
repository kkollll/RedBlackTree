import java.util.ArrayList;

public class AVLTree<K extends Comparable<K>, V> {

    private class Node {
        public K key;
        public V value;
        public Node left, right;
        public int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = null;
            right = null;
            height = 1;
        }
    }

    private Node root;
    private int size;

    public AVLTree() {
        root = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isBST() {

        ArrayList<K> keys = new ArrayList<>();
        inOrder(root, keys);
        for (int i = 1; i < keys.size(); i++) {
            if (keys.get(i - 1).compareTo(keys.get(i)) > 0) {
                return false;
            }
        }
        return true;
    }

    private void inOrder(Node node, ArrayList<K> keys) {
        if (node == null) {
            return;
        }
        inOrder(node.left, keys);
        keys.add(node.key);
        inOrder(node.right, keys);
    }

    public boolean isBanlanced() {
        return isBanlanced(root);
    }

    private boolean isBanlanced(Node node) {
        if (node == null) {
            return true;
        }
        int banlanceFactor = getBalanceFactor(node);
        if (Math.abs(banlanceFactor) > 1) {
            return false;
        }
        return isBanlanced(node.left) && isBanlanced(node.right);
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    private int getBalanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node t3 = x.right;
        y.left = t3;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.right = y;
        return x;
    }

    private Node leftRotate(Node y) {
        Node x = y.right;
        Node t3 = x.left;
        y.right = t3;
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        y.height = Math.max(getHeight(y.left), getHeight(y.right)) + 1;
        x.left = y;
        return x;
    }

    // 向二分搜索树中添加新的元素(key, value)
    public void add(K key, V value) {
        root = add(root, key, value);
    }

    public int getDepth() {
        return getDepth(root, 0);
    }
    private int getDepth(Node node, int depth) {
        if (node == null) {
            return depth;
        }
        int rightDepth = getDepth(node.right,depth + 1);
        int leftDepth  = getDepth(node.left, depth + 1);
        return rightDepth > leftDepth ? rightDepth : leftDepth;
    }
    // 向以node为根的二分搜索树中插入元素(key, value)，递归算法
    // 返回插入新节点后二分搜索树的根
    private Node add(Node node, K key, V value) {

        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = add(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = add(node.right, key, value);
        } else // key.compareTo(node.key) == 0
        {
            node.value = value;
        }


        return getNode(node);
    }

    // 维护平衡树
    private Node getNode(Node node) {
        if (node == null) {
            return null;
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        int balanceFactor = getBalanceFactor(node);
//        if (Math.abs(balanceFactor) > 1) {
//            System.out.println("unbalanced: " + balanceFactor);
//        }

//   t1 < z < t2 < x < t3 < y < t4
//                mid
//        y                             x
//       / \                          /   \
//      x   t4          右旋         z     y
//     / \           --------->     / \   / \
//    z  t3                        t1 t2 t3  t4
//   / \
//  t1 t2
//
//
//   t1 < x < t2 < z < t3 < y < t4
//                mid
//        y                             z
//       / \                          /   \
//      x   t4         左右旋         x     y
//     / \           --------->     / \   / \
//    t1  z                        t1 t2 t3  t4
//       / \
//      t2  t3
//
//
        // 平衡维护
        if (balanceFactor > 1 && getBalanceFactor(node.left) >= 0) {
            node = rightRotate(node);
        } else if (balanceFactor < -1 && getBalanceFactor(node.right) <= 0) {
            node = leftRotate(node);
        } else if (balanceFactor > 1 && getBalanceFactor(node.left) < 0) {
            node.left = leftRotate(node.left);
            node = rightRotate(node);
        } else if (balanceFactor < -1 && getBalanceFactor(node.right) > 0) {
            node.right = rightRotate(node.right);
            node = leftRotate(node);
        }
        return node;
    }

    // 返回以node为根节点的二分搜索树中，key所在的节点
    private Node getNode(Node node, K key) {

        if (node == null) {
            return null;
        }

        if (key.equals(node.key)) {
            return node;
        } else if (key.compareTo(node.key) < 0) {
            return getNode(node.left, key);
        } else // if(key.compareTo(node.key) > 0)
        {
            return getNode(node.right, key);
        }
    }

    public boolean contains(K key) {
        return getNode(root, key) != null;
    }

    public V get(K key) {

        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    public void set(K key, V newValue) {
        Node node = getNode(root, key);
        if (node == null) {
            throw new IllegalArgumentException(key + " doesn't exist!");
        }

        node.value = newValue;
    }

    // 返回以node为根的二分搜索树的最小值所在的节点
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        }
        return minimum(node.left);
    }

    // 从二分搜索树中删除键为key的节点
    public V remove(K key) {

        Node node = getNode(root, key);
        if (node != null) {
            root = remove(root, key);
            return node.value;
        }
        return null;
    }

    private Node remove(Node node, K key) {

        if (node == null) {
            return null;
        }

        Node retNode;
        if (key.compareTo(node.key) < 0) {
            node.left = remove(node.left, key);
            retNode = node;
        } else if (key.compareTo(node.key) > 0) {
            node.right = remove(node.right, key);
            retNode = node;
        } else {   // key.compareTo(node.key) == 0

            // 待删除节点左子树为空的情况
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                size--;
                retNode = rightNode;
            }
            // 待删除节点右子树为空的情况
            else if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                size--;
                retNode = leftNode;
            } else {

                // 待删除节点左右子树均不为空的情况

                // 找到比待删除节点大的最小节点, 即待删除节点右子树的最小节点
                // 用这个节点顶替待删除节点的位置
                Node successor = minimum(node.right);
                successor.right = remove(node.right, successor.key);
                successor.left = node.left;

                node.left = node.right = null;

                retNode = successor;
            }
        }
        return getNode(retNode);
    }

    public static void main(String[] args) {

        System.out.println("Pride and Prejudice");

        ArrayList<String> words = new ArrayList<>();
        if (FileOperation.readFile("pride-and-prejudice.txt", words)) {
            System.out.println("Total words: " + words.size());

            AVLTree<String, Integer> avlTree = new AVLTree<>();
            for (String word : words) {
                if (avlTree.contains(word)) {
                    avlTree.set(word, avlTree.get(word) + 1);
                } else {
                    avlTree.add(word, 1);
                }
            }

            System.out.println("Total different words: " + avlTree.getSize());
            System.out.println("Frequency of PRIDE: " + avlTree.get("pride"));
            System.out.println("Frequency of PREJUDICE: " + avlTree.get("prejudice"));

            System.out.println("is BST: " + avlTree.isBST());
            System.out.println("is Balanced: " + avlTree.isBanlanced());
            System.out.println(avlTree.getDepth());

            words.forEach(
                    (e) -> {
                        avlTree.remove(e);
                        if (!avlTree.isBST() || !avlTree.isBanlanced()) {
                            throw new RuntimeException("Error");
                        }
                    }
            );
        }
//
//        System.out.println();
//        AVLTree<Integer, Integer> tree = new AVLTree<>();
//        tree.add(8, null);
//        tree.add(4, null);
//        tree.add(9, null);
//        tree.add(2, null);
//        tree.add(6, null);
//        tree.add(10, null);
//        tree.add(1, null);
//        tree.add(3, null);
//        tree.add(7, null);
//        tree.remove(8);
//
//        if(!tree.isBanlanced()) {
//            throw new RuntimeException("remove not Balanced");
//        }
    }
}
