package com.example.marina.phase1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();

        if ( savedInstanceState == null ){

            DetailsFragment detailFragment = new DetailsFragment();
            detailFragment.setArguments(extras);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.secondFrag, detailFragment)
                    .commit();
        }

        }

}
