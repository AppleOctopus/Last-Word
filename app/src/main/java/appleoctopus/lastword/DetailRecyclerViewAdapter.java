package appleoctopus.lastword;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import appleoctopus.lastword.models.Video;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by allenwang on 2017/3/23.
 */

public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.ThumbNailViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<Video> mVideos;

    public DetailRecyclerViewAdapter(Context context) {
        mVideos = new ArrayList<>();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateData(ArrayList<Video> video) {
        mVideos = video;
        notifyDataSetChanged();
    }

    @Override
    public ThumbNailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbNailViewHolder(mLayoutInflater.inflate(R.layout.item_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(ThumbNailViewHolder holder, final int position) {
        Drawable d = mContext.getResources().
                getDrawable(R.drawable.com_facebook_profile_picture_blank_portrait);
        holder.mCircleImageView.setImageDrawable(d);
        holder.mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Video video = mVideos.get(position);
                String uriString = video.getLocalVideoUri();
                if (uriString != null) {
                    Uri uri = Uri.parse(uriString);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public static class ThumbNailViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        CircleImageView mCircleImageView;

        ThumbNailViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);
            mCircleImageView = (CircleImageView) view.findViewById(R.id.image_view);
        }
    }
}