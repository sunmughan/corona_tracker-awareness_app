package com.quarantinealert.feature.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.quarantinealert.R;
import com.quarantinealert.model.YouTubeModel;
import com.quarantinealert.util.BaseViewHolder;

public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {


    public static final int VIEW_TYPE_NORMAL = 1;
    Context context;

    private ArrayList<YouTubeModel> mYoutubeVideos;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public YoutubeRecyclerAdapter(Context context, ArrayList<YouTubeModel> youtubeVideos) {
        this.context = context;
        mYoutubeVideos = youtubeVideos;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_youtube_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mYoutubeVideos != null && mYoutubeVideos.size() > 0) {
            return mYoutubeVideos.size();
        } else {
            return 1;
        }
    }

    public void setItems(ArrayList<YouTubeModel> youtubeVideos) {
        mYoutubeVideos = youtubeVideos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder {
        //@BindView(R.id.textViewTitle)
        TextView textWaveTitle;
        //@BindView(R.id.btnPlay)
        ImageView playButton;

        RelativeLayout share;
        //@BindView(R.id.imageViewItem)
        ImageView imageViewItems;
        //@BindView(R.id.youtube_view)
        YouTubePlayerView youTubePlayerView;

        public ViewHolder(View view) {
            super(view);
            //ButterKnife.bind(this, view);
            share = (RelativeLayout)view.findViewById(R.id.share);
            textWaveTitle = (TextView) view.findViewById(R.id.textViewTitle);
            playButton = (ImageView) view.findViewById(R.id.btnPlay);
            imageViewItems = (ImageView) view.findViewById(R.id.imageViewItem);
            youTubePlayerView = (YouTubePlayerView) view.findViewById(R.id.youtube_view);

        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            YouTubeModel mYoutubeVideo = mYoutubeVideos.get(position);
            ((Activity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = Math.round(width * .5625f);
            Log.e("yutbehgt", "onBind: "+height );
            if (mYoutubeVideo.getTitle() != null)
                textWaveTitle.setText(mYoutubeVideo.getTitle());

            if (mYoutubeVideo.getImageurl() != null) {
                Glide.with(itemView.getContext())
                        .load(mYoutubeVideo.getImageurl()).
                         apply(new RequestOptions().override(width - 20, 200))
                        .into(imageViewItems);

                //Glide.with(itemView.getContext()).load(mYoutubeVideo.getImageUrl()).into(imageViewItems);
            }
            imageViewItems.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(View.GONE);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String link = "https://www.youtube.com/watch?v="+mYoutubeVideo.getVideoId();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);


                }
            });
            playButton.setOnClickListener(view -> {
                Log.e("ytarray2",mYoutubeVideo.getId()+" "+mYoutubeVideo.getImageurl()+" "+mYoutubeVideo.getVideoId());
                imageViewItems.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.GONE);
                youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                    @Override
                    public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(mYoutubeVideo.getVideoId(), 0);
                    }
                });
            });

        }
    }
}

