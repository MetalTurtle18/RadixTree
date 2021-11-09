/**
 * A class to implement a radix tree
 * Names: Dorian, Jason, Nick
 */
public class Trie {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void compress() {
        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (root.children[i] != null) count++;
        }
        if (count == 1) {
            //Compress
        }
        
    }

    // Inserts a String into the Trie
    public void add(String str) {
        addRec(str, root);
    }

    private void addRec(String str, TrieNode current) {
        if (str == null || str.length() == 0) return;
        var node = current.children[str.charAt(0) - 97];
        if (node == null) node = new TrieNode();
        node.isWord = str.length() == 1 || node.isWord; // The || is to leave it a word if it already is a word
        current.children[str.charAt(0) - 97] = node;
        addRec(str.substring(1), node);
    }

    // "Deletes" a String from the Trie
    // In actuality, we will just unmark the String as a word
    public void remove(String str) {
        removeRec(str, root);
    }

    private void removeRec(String str, TrieNode current) {
        var child = current.children[str.charAt(0) - 97];
        if (child == null) return;
        if (str.length() == 1) child.isWord = false;
        else removeRec(str.substring(1), child);
    }

    // Prints out the Trie
    // Code is provided
    public void print() {
        printRec(this.root, "");
    }

    // Recursive code that actually prints out the Trie
    // Code is provided
    public void printRec(TrieNode current, String str) {
        if (current == null)
            return;
        if (current.isWord)
            System.out.println(str);
        for (char c = 'a'; c <= 'z'; c++)
            printRec(current.children[c - 97], str + c);
    }

    public static class TrieNode {
        TrieNode[] children;
        String multiNode;
        boolean isWord;
        boolean isCompressed;

        public TrieNode() {
            children = new TrieNode[26];
            isWord = false;
            isCompressed = false;
            multiNode = "";
        }
    }


}