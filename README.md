# Radix Tree

### What is a Radix Tree?

A Radix Tree is a trie that compresses letters that have only one child. Its advantage over tries is that it saves space.

If a letter has only one child, that child can be added to the parent so that it functions as a single block of letters. It will be split up if a word is inserted that uses only part of that block. 

### Sources:
* https://en.wikipedia.org/wiki/Radix_tree
  * Used to get background information about how radix trees work.
* https://iq.opengenus.org/radix-tree/
  * Used as a reference for writing the insert and search methods.
