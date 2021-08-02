package com.card.carder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton btn_start, btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_start = findViewById(R.id.imageButton4);
        btn_exit = findViewById(R.id.imageButton5);
        btn_start.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CarderActivity.class));
        });
        btn_exit.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you really want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (arg0, arg1) -> System.exit(0)).create().show();
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("Do you really want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> System.exit(0)).create().show();
    }
}