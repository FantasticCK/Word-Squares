package com.CK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String[] words = {"abat", "baba", "atan", "atal"};
        System.out.println(new Solution().wordSquares(words));
    }
}

class Solution2 {
    List<List<String>> res = new ArrayList<>();

    public List<List<String>> wordSquares(String[] words) {
        int size = words.length;
        if (size == 0)
            return res;

        int len = words[0].length();

        Trie trie = new Trie();
        for (String s : words) {
            trie.insert(s);
        }
        List<String> dfsList = new ArrayList<>();
        dfs(trie, words, dfsList, len, 0);
        return res;
    }

    private void dfs(Trie trie, String[] words, List<String> dfsList, int len, int r) {
        if (r >= len) {
            boolean valid = true;
            for (int i = 0; i < len; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < len; j++) {
                    sb.append(dfsList.get(j).charAt(i));
                }
                if (!trie.search(sb.toString()) || !sb.toString().equals(dfsList.get(i))) {
                    valid = false;
                    break;
                }
            }
            if (valid == true)
                res.add(new ArrayList<>(dfsList));
            return;
        }

        for (int i = 0; i < words.length; i++) {
            dfsList.add(words[i]);
            dfs(trie, words, dfsList, len, r + 1);
            dfsList.remove(dfsList.size() - 1);
        }
    }

    class Trie {
        Node root;

        Trie() {
            root = new Node('#');
        }

        private void insert(String s) {
            int index = 0, sLen = s.length();
            Node curr = root;
            while (index < sLen) {
                char c = s.charAt(index);
                if (curr.children[c - 'a'] == null) {
                    curr.children[c - 'a'] = new Node(c);
                }
                curr = curr.children[c - 'a'];
                index++;
            }
            curr.isWord = true;
        }

        private boolean search(String s) {
            int index = 0, sLen = s.length();
            Node curr = root;
            while (index < sLen) {
                char c = s.charAt(index);
                if (curr.children[c - 'a'] == null) {
                    return false;
                }
                curr = curr.children[c - 'a'];
                index++;
            }
            return curr.isWord;
        }
    }

    class Node {
        char val;
        Node[] children;
        boolean isWord;

        Node(char _val) {
            val = _val;
            children = new Node[26];
            isWord = false;
        }
    }
}

// DFS + Trie
class Solution {
    class Node {
        Node[] kids = new Node[26];
        String val = null;
    }

    public Node root = new Node();

    private void addToTrie(String s, Node root) {
        for (int i = 0; i < s.length(); i++) {
            int ind = s.charAt(i) - 'a';
            if (root.kids[ind] == null) {
                root.kids[ind] = new Node();
            }
            root = root.kids[ind];
        }
        root.val = s;
    }

    private void findAllSquares(int row, int col, Node[] rows, List<List<String>> res) {
        if (row == rows.length) {
            List<String> temp = new ArrayList<>(rows.length);
            for (int i = 0; i < rows.length; i++) temp.add(rows[i].val);
            res.add(temp);
        } else if (col < rows.length) {
            Node currow = rows[row];
            Node curcol = rows[col];
            for (int i = 0; i < 26; i++) {
                if (currow.kids[i] != null && curcol.kids[i] != null) {
                    rows[row] = currow.kids[i];
                    rows[col] = curcol.kids[i];
                    findAllSquares(row, col + 1, rows, res);
                }
            }
            rows[row] = currow;
            rows[col] = curcol;
        } else {
            findAllSquares(row + 1, row + 1, rows, res);
        }
    }

    public List<List<String>> wordSquares(String[] words) {
        List<List<String>> res = new ArrayList<>();
        if (words == null || words.length == 0 || words[0] == null || words[0].length() == 0) return res;
        for (String s : words) {
            addToTrie(s, root);
        }
        Node[] rows = new Node[words[0].length()];
        Arrays.fill(rows, root);
        findAllSquares(0, 0, rows, res);
        return res;
    }
}