package com.whatismips.mipside.syntax.highlighting;


public class Trie {
    private class TrieNode {
        private TrieNode[] trieNodes;
        private boolean isEnd;

        TrieNode() {
            this.trieNodes = new TrieNode[26];
        }

    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode trieNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';

            if (trieNode.trieNodes[index] == null) {
                TrieNode temp = new TrieNode();
                trieNode.trieNodes[index] = temp;
                trieNode = temp;

            } else {
                trieNode = trieNode.trieNodes[index];
            }
        }
        trieNode.isEnd = true;
    }

    public boolean search(String word) {
        TrieNode trieNode = searchNode(word);
        if (trieNode == null) {
            return false;

        } else {
            return trieNode.isEnd;
        }

    }


    public boolean startsWith(String prefix) {
        TrieNode trieNode = searchNode(prefix);
        return trieNode != null;
    }

    private TrieNode searchNode(String word) {
        TrieNode trieNode = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';

            if (index >= 0 && index < 26 && trieNode.trieNodes[index] != null) {
                trieNode = trieNode.trieNodes[index];
            } else {
                return null;
            }
        }

        if (trieNode == root)
            return null;

        return trieNode;
    }
}
