/**
 * A RadixTree
 * <p>
 * A RadixTree is a trie that compresses letters that have only one child. Its advantage over tries is that it saves space.
 *
 * @author Nick
 * @author Dorian
 * @author Jason
 */
public class RadixTree {
    RadixNode root;

    /**
     * Constructor
     */
    public RadixTree() {
        root = new RadixNode();
    }

    /**
     * Main method to test the RadixTree
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {
        // ****************************************
        // Test 1: Inserting, using all 5 insert cases
        // ****************************************
        var rt = new RadixTree();
        rt.insert("apple"); // Case 1
        rt.insert("apple"); // Case 2
        rt.insert("apples"); // Case 3
        rt.insert("app"); // Case 4
        rt.insert("apes"); // Case 5

        System.out.println("""
                ****************************************
                Test 1: Inserting, using all 5 insert cases
                ****************************************
                Expected:
                apes
                app
                apple
                apples
                                
                Got:""");

        rt.print();

        // ****************************************
        // Test 2: Searching
        // ****************************************
        System.out.println("""
                ****************************************
                Test 2: Searching
                ****************************************
                Expected:
                true
                true
                false
                false
                true
                                
                Got:""");
        System.out.println(rt.search("apple"));
        System.out.println(rt.search("apes"));
        System.out.println(rt.search("ape"));
        System.out.println(rt.search("apply"));
        System.out.println(rt.search("app"));

    }

    /**
     * Wrapper method for the recursive {@link #insertRec}
     *
     * @param str the string to insert
     */
    public void insert(String str) {
        insertRec(str, root);
    }

    /**
     * Recursively insert the inputted word into the RadixTree
     *
     * @param str     the string to insert
     * @param current the node to insert the string into
     */
    public void insertRec(String str, RadixNode current) {
        if (str == null || str.length() == 0) return;
        var node = current.children[str.charAt(0) - 97];
        // 5 cases:
        if (node == null) { // 1. Node doesn't exist
            node = new RadixNode();
            node.isWord = true;
            node.text = str;
        } else if (node.text.equals(str)) { // 2. Node is the same as the input
            node.isWord = true;
        } else if (str.startsWith(node.text)) { // 3. Node is a prefix
            insertRec(str.substring(node.text.length()), node);
        } else if (node.text.startsWith(str)) { // 4. Node is longer than the inserting string
            var newChild = new RadixNode();
            newChild.text = node.text.substring(str.length());
            newChild.isWord = node.isWord;
            newChild.children = node.children;
            node.isWord = true;
            node.text = str;
            node.children = new RadixNode[26];
            node.children[newChild.text.charAt(0) - 97] = newChild;
        } else { // 5. Node starts the same as the inserting string, but they have different endings
            var newChild = new RadixNode();
            // Figure out the index of the first character that they differ
            int i = 0;
            while (node.text.charAt(i) == str.charAt(i)) i++;
            newChild.text = node.text.substring(i);
            newChild.isWord = node.isWord;
            newChild.children = node.children;
            node.isWord = false;
            node.text = node.text.substring(0, i);
            node.children = new RadixNode[26];
            node.children[newChild.text.charAt(0) - 97] = newChild;
            insertRec(str.substring(i), node);
        }
        current.children[str.charAt(0) - 97] = node;
    }


    /**
     * Wrapper method for the recursive {@link #removeRec}
     *
     * @param str the string to remove
     */
    public void remove(String str) {
        removeRec(str, root);
    }

    /**
     * Recursively remove the inputted word from the RadixTree
     *
     * @param str     the string to remove
     * @param current the node to remove the string from
     */
    public void removeRec(String str, RadixNode current) {
    }

    /**
     * Wrapper method for the recursive {@link #searchRec}
     */
    public boolean search(String str) {
        return searchRec(str, root);
    }

    /**
     * Recursively search the RadixTree for the inputted word
     *
     * @param str     the string to search for
     * @param current the node to search from
     */
    public boolean searchRec(String str, RadixNode current) {
        if (str == null || str.length() == 0) return false;
        var node = current.children[str.charAt(0) - 97];
        if (node == null) return false;
        if (node.text.equals(str)) return node.isWord;
        if (str.startsWith(node.text)) return searchRec(str.substring(node.text.length()), node);
        return false;
    }


    /**
     * Wrapper method for the recursive {@link #printRec}
     */
    public void print() {
        if (root != null)
            printRec(root, "");
    }

    /**
     * Recursively print the RadixTree
     *
     * @param current the node to print
     * @param str     the string to print
     */
    public void printRec(RadixNode current, String str) {
        if (current.isWord)
            System.out.println(str);
        for (RadixNode node : current.children) {
            if (node != null)
                printRec(node, str + node.text);
        }
    }

}


/**
 * A class representing a node in the RadixTree
 */
class RadixNode {
    RadixNode[] children;
    String text;
    boolean isWord;

    public RadixNode() {
        children = new RadixNode[26];
        text = "";
        isWord = false;
    }
}
