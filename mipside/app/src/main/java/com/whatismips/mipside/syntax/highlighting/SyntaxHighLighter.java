package com.whatismips.mipside.syntax.highlighting;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;

import java.util.Locale;

public class SyntaxHighLighter implements TextWatcher {
    private int startIndex;
    private int length;
    private Trie commandTrie;

    public SyntaxHighLighter(String[] commands) {
        buildCommandTrie(commands);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int currentCount) {
//        System.out.println(String.format(Locale.getDefault(),"%s %d %d %d", charSequence, start,count, currentCount));
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int priorCount, int currentCount) {
        System.out.println(String.format(Locale.getDefault(), "%s start: %d  priorCount: %d currentCount: %d", charSequence, start, priorCount, currentCount));
        if (currentCount > priorCount && (charSequence.charAt(start) == ' ' ||
                charSequence.charAt(start) == ','||
                charSequence.charAt(startIndex) == ','||
                charSequence.charAt(startIndex) == '\n' ||
                charSequence.charAt(start) == '\n'))
            startIndex = start;

        System.out.println(String.format(Locale.getDefault(), "startIndex: %d  length: %d", startIndex, length));
        if (currentCount < priorCount)
            startIndex = start;

        length = start + currentCount;


    }

    @Override
    public void afterTextChanged(Editable editable) {
//        System.out.println(String.format(Locale.getDefault(), "%s %d %d", editable.toString(), startIndex, length));
        String token = editable.toString().substring(startIndex, length);
        startIndex = adjustStart(token, startIndex);


        if (isValidCommand(token)) {
            editable.setSpan(
                    new ForegroundColorSpan(Color.BLUE),
                    startIndex,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else {
            if (token.startsWith("$")) {
                if (token.contains(",")){
                    int index = token.indexOf(',');
                    editable.setSpan(
                            new ForegroundColorSpan(Color.RED),
                            startIndex,
                            index,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );

                }
                else {
                    editable.setSpan(
                            new ForegroundColorSpan(Color.RED),
                            startIndex,
                            length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }

        }
    }

    private boolean isValidCommand(String token) {
        return commandTrie.search(token.trim().toLowerCase());
    }

    private int adjustStart(String token, int start) {
        if (token.startsWith(" "))
            return start + 1;

        return start;
    }

    private void buildCommandTrie(String[] commands) {
        commandTrie = new Trie();
        for (String command : commands)
            commandTrie.insert(command);
    }
}
