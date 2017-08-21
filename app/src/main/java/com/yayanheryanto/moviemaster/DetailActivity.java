package com.yayanheryanto.moviemaster;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yayanheryanto.moviemaster.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView detailGambar, backdrophImage;
    private TextView detailJudul, detailTahun, detailRate, overview;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindingData();
        setCollaptingToolbar();
        getFromIntent();
    }

    private void bindingData() {
        detailGambar = (ImageView) findViewById(R.id.expandedImage);
        detailJudul = (TextView) findViewById(R.id.detailJudul);
        backdrophImage = (ImageView) findViewById(R.id.backdrophImage);
        detailTahun = (TextView) findViewById(R.id.detailTahun);
        detailRate = (TextView) findViewById(R.id.detailRate);
        overview = (TextView) findViewById(R.id.overview);
    }

    private void getFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        movie = (Movie) bundle.getParcelable("MovieData");
        detailJudul.setText(movie.getTitle());
        detailTahun.setText(movie.getReleaseDate());
        detailRate.setText(String.valueOf(movie.getVoteAverage()));
        overview.setText(movie.getOverview());

        Glide.with(this)
                .load(movie.getPosterPath())
                .into(detailGambar);

        Glide.with(this)
                .load(movie.getBackdropPath())
                .into(backdrophImage);
    }

    private void setCollaptingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                collapsingToolbar.setTitle(movie.getTitle());
                collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
                collapsingToolbar.setExpandedTitleColor(Color.WHITE);
            }
        });
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

}
