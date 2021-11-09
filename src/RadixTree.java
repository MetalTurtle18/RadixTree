/**
 * A RadixTree, in layman's terms, is a compressed Trie
 */
public class RadixTree {
    RadixNode root; //Instance Variables

    //Constructor
    public RadixTree() {
        root = new RadixNode();
    }

    /**
     * Functional Methods
     */

    //Wrapper method for the recursive addRec()
    public void add(String str) {
        addRec(str, root);
    }
    public void addRec(String str, RadixNode current) {
        if (str == null || str.length() == 0) return;
        var node = current.children[str.charAt(0) - 97];
        if (node == null) node = new RadixNode();
        node.isWord = str.length() == 1 || node.isWord; // The || is to leave it a word if it already is a word
        current.children[str.charAt(0) - 97] = node;
        addRec(str.substring(1), node);
    }

    //Wrapper method for the recursive removeRec()
    public void remove(String str) {
        removeRec(str, root);
    }

    /**
     * Recursively remove the inputted word from the RadixTree,
     * Splitting and compressing as need be
     */
    public void removeRec(String str, RadixNode current) {}

    public void printRec(RadixNode current, String str) {
        if (current == null)
            return;
        System.out.println(str);
        for (char c = 'a'; c <= 'z'; c++)
            printRec(current.children[c - 97], str + c);
    }

    //The main method is to be used for tests on the implementation of a RadixTree
    public static void main(String[] args) {
        RadixTree rt = new RadixTree();
        rt.add("please");
        rt.add("place");
        rt.add("pleasure");

        rt.printRec(rt.root, "");
    }

}


/**
 * A class for manipulating the nodes within the Radix Tree
 */
class RadixNode {
    RadixNode[] children;
    String text; // If empty string, represents a single char. Otherwise, represents a group of characters
    boolean isWord;

    public RadixNode() {
        children = new RadixNode[26];
        text = "";
        isWord = false;
    }
}
