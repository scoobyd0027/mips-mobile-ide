package com.whatismips.mipside.syntax.highlighting;


import java.util.HashMap;
import java.util.Map;


public class Trie {
    class TrieNode {
        HashMap<Character, TrieNode> children;
        boolean isLeaf;

        private TrieNode() {
            children = new HashMap<>();
        }


    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }


    public void insert(String word) {
        HashMap<Character, TrieNode> children = root.children;

        for (int i = 0; i < word.length(); i++) {
            char charAt = word.charAt(i);

            TrieNode trieNode;
            if (children.containsKey(charAt)) {
                trieNode = children.get(charAt);

            } else {
                trieNode = new TrieNode();
                children.put(charAt, trieNode);
            }

            children = trieNode.children;

            //set leaf node
            if (i == word.length() - 1)
                trieNode.isLeaf = true;
        }
    }


    public boolean search(String word) {
        TrieNode trieNode = searchNode(word);

        return trieNode != null && trieNode.isLeaf;
    }


    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }

    public TrieNode searchNode(String token) {
        Map<Character, TrieNode> children = root.children;
        TrieNode trieNode = null;

        for (int i = 0; i < token.length(); i++) {
            char c = token.charAt(i);
            if (children.containsKey(c)) {
                trieNode = children.get(c);
                children = trieNode.children;
            } else {
                return null;
            }
        }

        return trieNode;
    }
}
