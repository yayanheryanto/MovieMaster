package com.yayanheryanto.moviemaster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yayanheryanto.moviemaster.adapter.VideoAdapter;
import com.yayanheryanto.moviemaster.api.APIClient;
import com.yayanheryanto.moviemaster.api.APIInterface;
import com.yayanheryanto.moviemaster.database.DBHelper;
import com.yayanheryanto.moviemaster.model.Movie;
import com.yayanheryanto.moviemaster.model.Video;
import com.yayanheryanto.moviemaster.model.VideoResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.moviemaster.config.EndPoint.API_KEY;
import static com.yayanheryanto.moviemaster.config.EndPoint.BASE_IMAGE;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView detailGambar, backdrophImage;
    private TextView detailJudul, detailTahun, detailRate, overview;
    private Movie movie;
    private APIInterface apiInterface;
    private RecyclerView recyclerview;
    private FloatingActionButton fab;
    private CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = toolbar.getNavigationIcon();
        drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorTextIcon), PorterDuff.Mode.SRC_ATOP);

        bindingData();
        setCollaptingToolbar();
        getFromIntent();
        setFab();
        getVideo();

    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper db = new DBHelper(getApplicationContext());
                db.addFavoriteMovie(movie);
                final Snackbar snackbar = Snackbar.make(layout,"Movie Sudah Ditambahkan ke Favorite", Snackbar.LENGTH_LONG)
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
                Log.d("Movie", "Favorit");
            }
        });
    }

    private void getVideo() {
        Log.d("ID", String.valueOf(movie.getId()));
        apiInterface = APIClient.getApiClient().create(APIInterface.class);
        Call<VideoResponse> call = apiInterface.getVieoTrailer(movie.getId(), API_KEY);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                List<Video> video = response.body().getResults();
                Log.d("Video", String.valueOf(video.size()));
                VideoAdapter adapter = new VideoAdapter(getApplicationContext(), video);
                recyclerview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    private void bindingData() {
        detailGambar = (ImageView) findViewById(R.id.expandedImage);
        detailJudul = (TextView) findViewById(R.id.detailJudul);
        backdrophImage = (ImageView) findViewById(R.id.backdrophImage);
        detailTahun = (TextView) findViewById(R.id.detailTahun);
        detailRate = (TextView) findViewById(R.id.detailRate);
        overview = (TextView) findViewById(R.id.overview);
        fab = (FloatingActionButton) findViewById(R.id.fab_favorite);
        recyclerview = (RecyclerView) findViewById(R.id.listVideo);
        layout = (CoordinatorLayout) findViewById(R.id.layout);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setHasFixedSize(true);
    }

    private void getFromIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        movie = (Movie) bundle.getParcelable("MovieData");
        detailJudul.setText(movie.getTitle());
        detailTahun.setText(converDate(movie.getReleaseDate()));
        detailRate.setText(String.valueOf(movie.getVoteAverage()));
        overview.setText(movie.getOverview());

        String image = BASE_IMAGE + movie.getPosterPath();
        Glide.with(this)
                .load(image)
                .error(R.drawable.error)
                .into(detailGambar);

        if (movie.getBackdropPath()==null){
            String backdroph = BASE_IMAGE + movie.getPosterPath();
            Glide.with(this)
                    .load(backdroph)
                    .error(R.drawable.error)
                    .into(backdrophImage);
        }else {
            String backdroph = BASE_IMAGE + movie.getBackdropPath();
            Glide.with(this)
                    .load(backdroph)
                    .error(R.drawable.error)
                    .into(backdrophImage);
        }
        Log.d("Posterpath", movie.getPosterPath() + " Backdroph " + movie.getBackdropPath());
    }

    private String converDate(String date){
        Date dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(dateFormat);
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
