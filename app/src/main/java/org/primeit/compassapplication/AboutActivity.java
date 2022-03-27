package org.primeit.compassapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.primeit.compassapplication.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {


    ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}