package com.ivy.commondemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ivy.commondemo.R;
import com.ivy.commondemo.domain.MoviesBean;

import java.util.List;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description:
 */

public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private List<MoviesBean.SubjectsBean> mDataList;

    public MovieAdapter(Context context, List<MoviesBean.SubjectsBean> dataList) {
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public MoviesBean.SubjectsBean getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View conView, ViewGroup group) {
        ViewHolder holder;
        View view;
        if (conView == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.item_movie, null);
            holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.tvDirectors = (TextView) view.findViewById(R.id.tv_directors);
            holder.tvCasts = (TextView) view.findViewById(R.id.tv_casts);
            holder.tvYear = (TextView) view.findViewById(R.id.tv_year);
            holder.tvGenres = (TextView) view.findViewById(R.id.tv_genres);

            holder.ivCover = (ImageView) view.findViewById(R.id.iv_cover);
            holder.rbRating = (RatingBar) view.findViewById(R.id.rb_rating);

            view.setTag(holder);
        } else {
            view = conView;
            holder = (ViewHolder) conView.getTag();
        }

        MoviesBean.SubjectsBean subjects = getItem(i);
        holder.tvTitle.setText(subjects.getTitle());

        List<MoviesBean.SubjectsBean.DirectorsBean> directorsList = subjects.getDirectors();
        StringBuffer sbDirectors = new StringBuffer();
        for (int j = 0; j < directorsList.size(); j++) {
            sbDirectors.append(directorsList.get(j).getName()).append("/");
        }
        String directors = sbDirectors.substring(0, sbDirectors.length() - 1);
        holder.tvDirectors.setText(directors);

        List<MoviesBean.SubjectsBean.CastsBean> castsList = subjects.getCasts();
        StringBuffer sbCasts = new StringBuffer();
        for (int j = 0; j < castsList.size(); j++) {
            sbCasts.append(castsList.get(j).getName()).append("/");
        }
        String casts = sbCasts.substring(0, sbCasts.length() - 1);

        holder.tvCasts.setText(casts);

        holder.tvYear.setText(subjects.getYear());

        String smallUrl = subjects.getImages().getSmall();
        Glide.with(mContext).load(smallUrl).into(holder.ivCover);

        List<String> genres = subjects.getGenres();
        StringBuffer sbGenres = new StringBuffer();
        for (String gen : genres) {
            sbGenres.append(gen).append(" ");
        }

        holder.tvGenres.setText(sbGenres.toString());


        MoviesBean.SubjectsBean.RatingBean rating = subjects.getRating();
        int max = rating.getMax();
        double average = rating.getAverage();
        holder.rbRating.setMax(5);
        int rate = (int) (average / 2 + 0.5);
        if (rate > 5) rate = 5;
        holder.rbRating.setProgress(rate);


        return view;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvCasts;
        TextView tvDirectors;
        TextView tvYear;
        TextView tvGenres;
        ImageView ivCover;
        RatingBar rbRating;

    }
}
