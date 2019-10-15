package com.whatismips.mipside.com.whatismips.executor;

import java.util.HashMap;

public class Executor {

    HashMap<String, String> registers = new HashMap<>();
    HashMap<String, Integer> labels = new HashMap<>();

    public Executor(String rawMips) {

            // store each line of rawMips into arraylist of strings called memory
                // when label is encountered: store it into hashmap called 'Labels'. Key = label. value = index of label in memory
            // initialize hashmap for registers. key = register name. Value = integer (decimal)
            // initialize PC (int) to index of .text line

    }

    public void executeNext(){
        // execute instruction that the PC is pointing to
    }
}
