package com.whatismips.mipside.syntax.highlighting;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.Locale;

public class SyntaxHighLighter implements TextWatcher {
    private int startIndex, cursor;
    //    private int length;
    private Trie commandTrie;
    private EditText attached;

    public SyntaxHighLighter(String[] commands, EditText attached) {
        this.attached = attached;
        buildCommandTrie(commands);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int currentCount) {
        System.out.println("beforeTextChanged");
        System.out.println(String.format(Locale.getDefault(),"%s %d %d %d", charSequence, start,count, currentCount));
        System.out.println(String.format(Locale.getDefault(), "cursor position: %d", attached.getSelectionStart()));

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int priorCount, int currentCount) {
        System.out.println("onTextChanged");
        System.out.println(String.format(Locale.getDefault(), "%s start: %d  priorCount: %d currentCount: %d", charSequence, start, priorCount, currentCount));

        cursor = attached.getSelectionStart();
        System.out.println(String.format(Locale.getDefault(), "cursor position: %d", cursor));
//        length = start + currentCount;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        System.out.println("afterTextChanged");
//        System.out.println(String.format(Locale.getDefault(), "%s %d %d", editable.toString(), cursor, length));

        String text = editable.toString();
        startIndex = positionStartIndex(text);
        moveCursor(text);

        String token = text.substring(startIndex, cursor);
        startIndex = adjustStart(token, startIndex);
        token = token.trim();
        System.out.println(String.format(Locale.getDefault(), "startIndex: %d  cursor: %d", startIndex, cursor));
        System.out.println(String.format(Locale.getDefault(), "%s\n\n", token));

        if (isValidCommand(token)) {
            editable.setSpan(
                    new ForegroundColorSpan(Color.BLUE),
                    startIndex,
                    cursor,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else if (token.matches("\\d*\\((\\s*\\$[asgtv][0-9p]\\s*)\\)") || token.matches("\\$[astv]\\d,?")) {
            int index = text.substring(0, cursor).lastIndexOf("$");
            editable.setSpan(
                    new ForegroundColorSpan(Color.RED),
                    index,
                    index + 3,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else if (token.startsWith(".")) {
            if (isValidCommand(token.substring(1))) {
                editable.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#FF77AA")),
                        startIndex,
                        cursor,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        } else {
            editable.setSpan(
                    new ForegroundColorSpan(Color.BLACK),
                    startIndex,
                    cursor,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        }
        highlightComments(editable);
    }

    private void highlightComments(Editable editable) {
        String text = editable.toString();
        for(int i = 0; i < text.length(); i++){
            if (text.charAt(i) == '#'){
                int newlinePos = getNextNewlineCharacterPosition(text, i+1);
                editable.setSpan(
                        new ForegroundColorSpan(Color.parseColor("#00DB25")),
                        i,
                        newlinePos,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

    }

    private int getNextNewlineCharacterPosition(String text, int start) {
        int pos = start;
        while (pos < text.length() && text.charAt(pos) != '\n') pos++;

        return pos;
    }


    private boolean isValidCommand(String token) {
        return commandTrie.search(token.trim().toLowerCase());
    }

    private int adjustStart(String token, int start) {
        if (token.startsWith(" "))
            return start + 1;

        return start;
    }

    private int positionStartIndex(String text) {
        int spaceIndex = text.substring(0, cursor).lastIndexOf(' ');
        int newLineIndex = text.substring(0, cursor).lastIndexOf('\n');

        if (newLineIndex > spaceIndex)
            return newLineIndex;

        if (spaceIndex > 0)
            return spaceIndex;

        return 0;
    }

    private void moveCursor(String text) {
        while (cursor < text.length() && text.charAt(cursor) != ' ' && text.charAt(cursor) != '\n')
            cursor++;

    }

    private void buildCommandTrie(String[] commands) {
        commandTrie = new Trie();
        for (String command : commands)
            commandTrie.insert(command);
    }
}
