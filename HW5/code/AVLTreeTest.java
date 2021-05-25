import java.util.Random;
import java.util.Scanner;

public class AVLTreeTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.next());

        AVLTree<Integer, Integer> avlTree = new AVLTree<>();

        while(n-- > 0) {
            Random rand = new Random();
            int randn = rand.nextInt(100000);
            avlTree.insert(randn, randn);

            System.out.println(avlTree);
        }
    }
}
