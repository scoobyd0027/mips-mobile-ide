package com.whatismips.mipside.syntax.highlighting;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import androidx.core.content.ContextCompat;

import com.whatismips.mipside.R;

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
        System.out.println(String.format(Locale.getDefault(), "%s %d %d %d", charSequence, start, priorCount, currentCount));
        startIndex = start;
        length = start + currentCount;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        System.out.println(String.format(Locale.getDefault(), "%s %d %d", editable.toString(), startIndex, length));
        String token = editable.toString().substring(startIndex, length);
        startIndex = adjustStart(token, startIndex);

        if (isValidToken(token)) {
            editable.setSpan(
                    new ForegroundColorSpan(Color.BLUE),
                    startIndex,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

        } else {
            editable.setSpan(
                    new ForegroundColorSpan(Color.RED),
                    startIndex,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
    }

    private boolean isValidToken(String token) {
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
