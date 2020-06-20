import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
        String fileName = "pride-and-prejudice.txt";
        System.out.println(fileName);

        ArrayList<String> words = new ArrayList<>();


        if (FileOperation.readFile(fileName, words)) {


            Collections.sort(words);

            // Test BST
            long t1 = System.nanoTime();
            BST<String, Integer> bst = new BST<>();
            words.forEach(
                    (e) -> {
                        if (bst.contains(e)) {
                            bst.set(e, bst.get(e) + 1);
                        } else {
                            bst.add(e, 1);
                        }
                    }
            );
            long t2 = System.nanoTime();
            double t = (t2 - t1) / 1000000000.0;
            System.out.println("BST: " + t + " s.");

            // Test AVLTree
            t1 = System.nanoTime();
            AVLTree<String, Integer> avlTree = new AVLTree<>();
            words.forEach(
                    (e) -> {
                        if (avlTree.contains(e)) {
                            avlTree.set(e, avlTree.get(e) + 1);
                        } else {
                            avlTree.add(e, 1);
                        }
                    }
            );
            t2 = System.nanoTime();
            t = (t2 - t1) / 1000000000.0;
            System.out.println("AVLTree: " + t + " s.");
            System.out.println("AVLTree depth: " + avlTree.getDepth() + ".");

            // Test rbTree
            t1 = System.nanoTime();
            RBTree<String, Integer> rbTree = new RBTree<>();
            words.forEach(
                    (e) -> {
                        if (rbTree.contains(e)) {
                            rbTree.set(e, rbTree.get(e) + 1);
                        } else {
                            rbTree.add(e, 1);
                        }
                    }
            );
            t2 = System.nanoTime();
            t = (t2 - t1) / 1000000000.0;
            System.out.println("RBTree: " + t + " s.");
            System.out.println("RBTree depth: " + rbTree.getDepth() + ".");
        }
    }
}
