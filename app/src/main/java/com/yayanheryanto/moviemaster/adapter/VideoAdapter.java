package com.yayanheryanto.moviemaster.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yayanheryanto.moviemaster.R;
import com.yayanheryanto.moviemaster.model.Video;

import java.util.List;

/**
 * Created by Yayan Heryanto on 8/21/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoVH> {

    private Context context;
    private List<Video> mVideo;

    public VideoAdapter(Context context, List<Video> video) {
        this.context = context;
        this.mVideo = video;
    }

    @Override
    public VideoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_item, parent, false);
        VideoVH videoVH = new VideoVH(view);
        return videoVH;
    }

    @Override
    public void onBindViewHolder(VideoVH holder, int position) {
        final Video video = mVideo.get(position);
        holder.textView.setText(video.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                try {
                    intent =new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(video.getYoutubUrl());
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(video.getYoutubUrl());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideo.size();
    }

    public class VideoVH extends RecyclerView.ViewHolder{

        private TextView textView;
        private View mView;

        public VideoVH(View itemView) {
            super(itemView);

            this.mView = itemView;
            textView = (TextView) itemView.findViewById(R.id.txtJudulVideo);
        }
    }
}
