package com.yayanheryanto.moviemaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yayanheryanto.moviemaster.DetailActivity;
import com.yayanheryanto.moviemaster.R;
import com.yayanheryanto.moviemaster.model.Movie;

import java.util.List;

/**
 * Created by Yayan Heryanto on 8/19/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieModels;

    public MovieAdapter(Context context, List<Movie> movieModels) {
        this.context = context;
        this.movieModels = movieModels;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_movie, null, false);
        MovieViewHolder adapter = new MovieViewHolder(view);

        return adapter;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        final Movie movie = movieModels.get(position);
        holder.judulMovie.setText(movie.getTitle());
        Glide.with(context)
                .load(movie.getPosterPath())
                .into(holder.gambarMovie);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("MovieData",movie);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView gambarMovie;
        private TextView judulMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            this.mView = itemView;
            gambarMovie = (ImageView) itemView.findViewById(R.id.gambarMovie);
            judulMovie = (TextView) itemView.findViewById(R.id.judulMovie);
        }
    }
}
