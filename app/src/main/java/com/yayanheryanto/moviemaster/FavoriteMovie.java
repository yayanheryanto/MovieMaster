package com.yayanheryanto.moviemaster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yayanheryanto.moviemaster.adapter.MovieAdapter;
import com.yayanheryanto.moviemaster.database.DBHelper;
import com.yayanheryanto.moviemaster.model.Movie;

import java.util.List;

public class FavoriteMovie extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MovieAdapter adapter;
    private List<Movie> movies;
    private CoordinatorLayout layout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Favorite Movie");
        bindingData();
        setFab();
        initDatabases();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindingData() {
        recyclerView = (RecyclerView) findViewById(R.id.rvFavorite);
        layout = (CoordinatorLayout) findViewById(R.id.relativeLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null)
                layoutManager.scrollToPositionWithOffset(0,0);
            }
        });
    }

    private void setRecyclerView() {
        layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void initDatabases() {
        DBHelper db = new DBHelper(this);
        movies = db.getAllMovie();
        if (movies.size()==0){
            Log.d("Favorit ", String.valueOf(movies.size()));
            final Snackbar snackbar = Snackbar.make(layout,"Favorite Movie Masih Kosong", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            snackbar.setActionTextColor(Color.YELLOW);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }else {
            setRecyclerView();
        }
    }

}
