package appleoctopus.lastword;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by allenwang on 2017/3/23.
 */

public class CatogoryRecyclerViewAdapter extends RecyclerView.Adapter<CatogoryRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;

    public CatogoryRecyclerViewAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.layout_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(mContext, DetailActivity.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        NormalTextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);
            mImageView = (ImageView) view.findViewById(R.id.image_view);


        }
    }
}