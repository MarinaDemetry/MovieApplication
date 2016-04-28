package com.example.marina.phase1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements MovieInterface {
        private boolean twoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FrameLayout detailLayout = (FrameLayout) findViewById(R.id.secondFrag);

        if ( detailLayout == null ){

            twoPane = false;
            //Log.d("Pane is", "one pane");
        }
        else {

            twoPane = true;
            //Log.d("Pane is", "two pane");
        }


            ;
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Setting.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void choosenMovie(Movie m) {
        if ( twoPane == true ) {

            Bundle b = new Bundle();
            b.putSerializable("MovieKey", m);

            DetailsFragment detailFragment = new DetailsFragment();
            detailFragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.secondFrag,detailFragment)
                    .commit();
        }
        else {

            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("MovieKey", m);
            startActivity(intent);

        }

    }
}
