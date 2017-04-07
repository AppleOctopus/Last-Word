package appleoctopus.lastword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by allenwang on 2017/3/23.
 */

public class CatogoryRecyclerViewAdapter extends RecyclerView.Adapter<CatogoryRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;
    private TypedArray mBgIds;

    private Set<Integer> mIntSet;


    public CatogoryRecyclerViewAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mBgIds = context.getResources().obtainTypedArray(R.array.bg_ids);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    public void updateData(Set<Integer> intSet){
        this.mIntSet = intSet;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_catagory, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, final int position) {
        holder.mTextView.setText(mTitles[position]);
        holder.mBgImageView.setImageDrawable(mBgIds.getDrawable(position));
        holder.mBgImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVideoExist = mIntSet.contains(position);
                Intent i = new Intent();
                if (isVideoExist) {
                    i.setClass(mContext, CategoryDetailActivity.class);
                    i.putExtra(CategoryDetailActivity.CATEGORY_KEY, position);
                } else {
                    i.setClass(mContext, StoryActivity.class);
                    i.putExtra(CategoryDetailActivity.CATEGORY_KEY, position);
                }
                mContext.startActivity(i);
            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                //takeVideoIntent.putExtra(CATEGORY, position);
                ((Activity)mContext).startActivityForResult(
                        takeVideoIntent, position);
             }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mBgImageView;
        Button mButton;

        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);
            mBgImageView = (ImageView) view.findViewById(R.id.image_view);
            mButton = (Button) view.findViewById(R.id.button_record);
        }
    }
}