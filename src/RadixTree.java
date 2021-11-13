import java.util.Arrays;
import java.util.Objects;

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
        // Test 1.1: Basic inserting
        // ****************************************
        var rt = new RadixTree();
        rt.insert("hello");
        rt.insert("apple");
        rt.insert("banana");
        rt.insert("cat");
        rt.insert("dog");
        rt.insert("elephant");
        rt.insert("fish");
        rt.insert("grape");
        rt.insert("pineapple");
        rt.insert("zebra");
        rt.insert("zoo");
        rt.insert("graph");

        System.out.println("""
                ****************************************
                Test 1.1: Basic inserting
                ****************************************
                Expected:
                apple
                banana
                cat
                dog
                elephant
                fish
                grape
                graph
                hello
                pineapple
                zebra
                zoo
                                
                Got:""");

        rt.print();

        // ****************************************
        // Test 1.2: Inserting, using all 5 insert cases
        // ****************************************
        rt = new RadixTree();
        rt.insert("apple"); // Case 1
        rt.insert("apple"); // Case 2
        rt.insert("apples"); // Case 3
        rt.insert("app"); // Case 4
        rt.insert("apes"); // Case 5
        rt.insert("ap"); // Case 2 again

        System.out.println("""
                ****************************************
                Test 1.2: Inserting, using all 5 insert cases
                ****************************************
                Expected:
                ap
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

        // ****************************************
        // Test 3.1: Removing
        // ****************************************
        rt.insert("banana");
        rt.insert("ban");
        rt.remove("java");
        rt.remove("apply");
        rt.remove("apples");
        rt.remove("apple");
        rt.remove("banana");
        rt.remove("a");

        System.out.println("""
                ****************************************
                Test 3: Removing
                ****************************************
                Expected:
                ap
                apes
                app
                ban
                                
                Got:""");

        rt.print();

        // ****************************************
        // Test 3.2: Removing a chain of single letters
        // ****************************************
        rt = new RadixTree();
        rt.insert("a");
        rt.insert("an");
        rt.insert("ant");
        rt.insert("ants");
        rt.remove("ant"); // Should merge the s into "ant" and make that a word

        System.out.println("""
                ****************************************
                Test 3.2: Removing a chain of single letters
                ****************************************
                Expected:
                a
                an
                ants
                
                Got:""");

        rt.print();
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
        if (node == null) { // 1. Node doesn't exist, so just make a new node
            node = new RadixNode();
            node.isWord = true;
            node.text = str;
        } else if (node.text.equals(str)) { // 2. The node already exists, so just mark it as a word
            node.isWord = true;
        } else if (str.startsWith(node.text)) { // 3. Node is the start of this word, so recursively insert the rest underneath
            insertRec(str.substring(node.text.length()), node);
        } else if (node.text.startsWith(str)) { // 4. The node is longer than the word, so split the node up
            // Make the second part of the node a new node
            var newChild = new RadixNode();
            newChild.text = node.text.substring(str.length());
            newChild.isWord = node.isWord;
            newChild.children = node.children;
            // Add the new node
            node.isWord = true;
            node.text = str;
            node.children = new RadixNode[26];
            node.children[newChild.text.charAt(0) - 97] = newChild;
        } else { // 5. Node starts the same as the inserting string, but they have different endings
            // Make the new node of existing node's text
            var newChild = new RadixNode();
            // Figure out the index of the first character that they differ
            int i = 0;
            while (node.text.charAt(i) == str.charAt(i)) i++;
            newChild.text = node.text.substring(i);
            newChild.isWord = node.isWord;
            newChild.children = node.children;
            // Add the differing part of the existing node
            node.isWord = false;
            node.text = node.text.substring(0, i);
            node.children = new RadixNode[26];
            node.children[newChild.text.charAt(0) - 97] = newChild;
            // Insert the differing part of the inserting string recursively
            insertRec(str.substring(i), node);
        }
        current.children[str.charAt(0) - 97] = node; // Make the manipulated node actually part of the tree
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
        if (str == null || str.length() == 0) return; // If there is no string, finish
        var node = current.children[str.charAt(0) - 97]; // Go to the first node corresponding to str input

        if (node == null) {
            return;
        } else if (node.text.equals(str)) {
            // Count the number of children
            var childrenCount = (int) Arrays.stream(node.children).filter(Objects::nonNull).count();
            if (childrenCount == 0) // If it doesn't have children, just remove it
                node = null;
            else if (childrenCount == 1) { // If it has one child, join the child's text with it to keep it compressed
                // Throws an error if it can't find the node, but that shouldn't happen
                var newChild = Arrays.stream(node.children).filter(Objects::nonNull).findFirst().get();
                node.text += newChild.text;
                node.children = newChild.children;
                node.isWord = newChild.isWord;
            } else { // If it has more than 1 children, just mark it as not a word
                node.isWord = false;
            }
        } else if (str.startsWith(node.text)) { // If the node is only the first part of the string, remove the rest recursively
            removeRec(str.substring(node.text.length()), node);
        } else { // Other cases don't need to do anything because they don't have the word
            return;
        }
        current.children[str.charAt(0) - 97] = node;
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
        if (str == null || str.length() == 0) return false; // If searching for nothing, return false
        var node = current.children[str.charAt(0) - 97];
        if (node == null) return false; // If the node is null, return false
        if (node.text.equals(str)) return node.isWord; // If the string is found, return whether it's actually marked as a word
        if (str.startsWith(node.text)) return searchRec(str.substring(node.text.length()), node); // If part of the word is found, search for the rest
        return false; // If nothing else, return false
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
        if (current.isWord) // If on a word, print it
            System.out.println(str);
        for (RadixNode node : current.children) { // For each non-null child, print it recursively
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
