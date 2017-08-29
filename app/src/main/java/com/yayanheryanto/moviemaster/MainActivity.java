package com.yayanheryanto.moviemaster;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.yayanheryanto.moviemaster.adapter.MovieAdapter;
import com.yayanheryanto.moviemaster.api.APIClient;
import com.yayanheryanto.moviemaster.api.APIInterface;
import com.yayanheryanto.moviemaster.model.Movie;
import com.yayanheryanto.moviemaster.model.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.moviemaster.config.EndPoint.API_KEY;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] kategori;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private APIInterface apiInterface;
    private Call<MovieResponse> call;
    private Spinner navigationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingData();
        setColorSwipe();
        setPostSwipe();
        swipeOnRefesh();
        setToolbar();
        setFab();
        setDrawer();
        setNavigationView();
        setSpinner();
        setRecyclerView();
        initRetrofit();
    }

    private void initRetrofit() {
        apiInterface = APIClient.getApiClient().create(APIInterface.class);
        if (call==null) {
            call = apiInterface.getPopularMovie(API_KEY);
            getMovie(call);
        }
    }

    private void bindingData() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setColorSwipe(){
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.darker_gray);
    }

    private void setPostSwipe() {
        swipeRefresh.setRefreshing(true);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                initRetrofit();
            }
        });
    }

    private void swipeOnRefesh() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                navigationSpinner.setSelection(0);
                call = null;
                initRetrofit();

            }
        });
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

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setNavigationView() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSpinner() {
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Kategori, R.layout.spinner_item);
        navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);

        kategori = getResources().getStringArray(R.array.Kategori);
        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (kategori[position]){
                    case "Genres" :

                        break;

                    case "Action" :
                        Call<MovieResponse> mcall = apiInterface.getGenreMovie(28, API_KEY);
                        getMovie(mcall);
                        break;

                    case "Adventure" :
                        Call<MovieResponse> mcall1 = apiInterface.getGenreMovie(12, API_KEY);
                        getMovie(mcall1);
                        break;

                    case "Comedy" :
                        Call<MovieResponse> mcall2 = apiInterface.getGenreMovie(35, API_KEY);
                        getMovie(mcall2);
                        break;

                    case "Fantasy" :
                        Call<MovieResponse> mcall3 = apiInterface.getGenreMovie(14, API_KEY);
                        getMovie(mcall3);
                        break;

                    case "Horror" :
                        Call<MovieResponse> mcall4 = apiInterface.getGenreMovie(27, API_KEY);
                        getMovie(mcall4);
                        break;

                    case "War" :
                        Call<MovieResponse> mcall5 = apiInterface.getGenreMovie(10752, API_KEY);
                        getMovie(mcall5);
                        break;


                    default:
                        Log.d("Error", "Terjadi Kesalahan");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRecyclerView() {
        layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu);

        final MenuItem cari = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) cari.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                swipeRefresh.setRefreshing(true);
                call = apiInterface.getSearch(API_KEY, query);
                getMovie(call);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void getMovie(Call<MovieResponse> mCall){
        swipeRefresh.setRefreshing(true);
        mCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                swipeRefresh.setRefreshing(false);
                List<Movie> movie = response.body().getResults();
                MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movie);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("Respone", String.valueOf(movie.size()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Log.d("Respone", t.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            Call<MovieResponse> mcall = apiInterface.getPopularMovie(API_KEY);
            getMovie(mcall);
            return true;
        }else if (id==R.id.action_top_rated){
            Call<MovieResponse> mcall = apiInterface.getTopRatedMovies(API_KEY);
            getMovie(mcall);
            return true;
        }else if (id==R.id.action_upcoming){
            Call<MovieResponse> mcall = apiInterface.getUpComingMovie(API_KEY);
            getMovie(mcall);
            return true;
        }else if (id==R.id.action_now_playing){
            Call<MovieResponse> mcall = apiInterface.getNowPlayingMovies(API_KEY);
            getMovie(mcall);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setCheckable(false);

        if (id == R.id.nav_favorit) {
            Intent intent = new Intent(MainActivity.this, FavoriteMovie.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setView(R.layout.about_dialog);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}