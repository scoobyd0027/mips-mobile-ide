package com.whatismips.mipside;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.whatismips.mipside.syntax.highlighting.SyntaxHighLighter;

public class MainActivity extends AppCompatActivity {
    EditText editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editor = findViewById(R.id.editText);

        String[] commands = getResources().getStringArray(R.array.mips_commands);
        editor.addTextChangedListener(new SyntaxHighLighter(commands, editor));
    }
}
