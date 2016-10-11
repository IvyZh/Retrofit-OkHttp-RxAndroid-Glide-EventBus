package com.ivy.commondemo.activities;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ivy.commondemo.R;
import com.ivy.commondemo.adapter.MovieAdapter;
import com.ivy.commondemo.base.BaseActivity;
import com.ivy.commondemo.domain.MoviesBean;
import com.ivy.commondemo.net.Retrofit2Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {


    @BindView(R.id.bt_top250)
    Button mBtTop250;
    @BindView(R.id.bt_theaters)
    Button mBtTheaters;
    @BindView(R.id.lv_movies)
    ListView mLvMovies;

    private MovieAdapter adapter;
    private List<MoviesBean.SubjectsBean> mDataList;

    private Observer mObserver;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);

        //git

        mObserver = new Observer<MoviesBean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MoviesBean bean) {
                List<MoviesBean.SubjectsBean> subjects = bean.getSubjects();
                setData(subjects);
            }

        };
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }


    @OnClick({R.id.bt_top250, R.id.bt_theaters})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_top250:
                Call<MoviesBean> call = Retrofit2Utils.getServiceApi().getTop250Movies(0, 20);
                call.enqueue(new Callback<MoviesBean>() {
                    @Override
                    public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
                        if (response.isSuccessful()) {
                            setData(response.body().getSubjects());
                        }

                    }

                    @Override
                    public void onFailure(Call<MoviesBean> call, Throwable t) {

                    }
                });
                break;
            case R.id.bt_theaters:
                // 返回对象
//                getObj();

                // 返回String
//                getString();

                // 使用RxAndroid
                getRxAndroid();

                break;
        }
    }

    private void getRxAndroid() {
        Observable<MoviesBean> observable = Retrofit2Utils.getServiceApi().getTheatersMoviesObservable("上海");


        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }

    private void getString() {
        Call<String> call3 = Retrofit2Utils.getServiceApi().getTheatersMoviesString("上海");
        call3.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v("success", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("fail", t.getMessage());
            }
        });
    }

    private void getObj() {
        Call<MoviesBean> call2 = Retrofit2Utils.getServiceApi().getTheatersMovies("上海");
        call2.enqueue(new Callback<MoviesBean>() {
            @Override
            public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
                if (response.isSuccessful()) {
                    setData(response.body().getSubjects());
                }

            }

            @Override
            public void onFailure(Call<MoviesBean> call, Throwable t) {

            }
        });
    }


    private void setData(List<MoviesBean.SubjectsBean> subjects) {

//        Log.v("way", response);
        if (adapter == null) {
            mDataList = subjects;
            adapter = new MovieAdapter(MainActivity.this, mDataList);
            mLvMovies.setAdapter(adapter);
        } else {
            mDataList.clear();
            mDataList.addAll(subjects);
            adapter.notifyDataSetChanged();
        }

    }
}
