# Retrofit-OkHttp-RxAndroid-Glide-EventBus
Retrofit-OkHttp-RxAndroid-Glide-EventBus 通用Demo

> 可以把它作为一个应用程序的通用框架


## 步骤

> 以下


* 1. 引入OkHttp
>
	compile 'com.squareup.okhttp3:okhttp:3.0.1' // okhttp3
	compile 'com.squareup.okhttp3:logging-interceptor:3.0.1' // okhttp3的拦截器



* 2. 实例化OkHttp3，OkHttp3Utils

> code,没有设置Cookie的管理器和网络拦截器，根据实际情况设置，包括token可以写在拦截器中

	public class OkHttp3Utils {
	    private static OkHttpClient mOkHttpClient = null;
	    private static int cacheSize = 10 << 20; // 10 MiB
	    private static Cache cache = new Cache(MyApplication.getContext().getCacheDir(), cacheSize);
	
	    private OkHttp3Utils() {//私有化构造器
	
	    }
	
	    public static OkHttpClient getOkHttpClient() {
	        if (mOkHttpClient == null) {
	            synchronized (OkHttpClient.class) {
	                if (mOkHttpClient == null) {
	
	                    // 日志拦截器
	                    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
	                    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
	
	                    mOkHttpClient = new OkHttpClient.Builder()
	                            //设置一个自动管理cookies的管理器
	                            //.cookieJar(new CookiesManager())
	                            .cache(cache)
	                            //添加日志拦截器
	                            //.addInterceptor(logInterceptor)
	                            //添加网络连接器,让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数
	                            //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getContext()))
	                            .retryOnConnectionFailure(true)//方法为设置出现错误进行重新连接。
	                            .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
	                            .readTimeout(20, TimeUnit.SECONDS)
	                            .writeTimeout(20, TimeUnit.SECONDS)
	                            .build();
	                }
	            }
	        }
	        return mOkHttpClient;
	    }
	}


* 3.接入Retrofit2

>

    compile 'com.squareup.retrofit2:retrofit:2.0.0' // retrofit2
    compile 'com.squareup.retrofit2:converter-gson:2.0.0' //gson转换


* 4. 实例化Retrofit2，Retrofit2Utils

> Code

	public abstract class Retrofit2Utils {
	    private static Retrofit mRetrofit = null;
	
	    private static String BASE_URL = "https://api.douban.com";
	
	    private static OkHttpClient mOkHttpClient;
	
	    protected static Retrofit getRetrofit() {
	
	        if (mRetrofit == null) {
	            synchronized (Retrofit2Utils.class) {
	                if (mOkHttpClient == null) {
	                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
	                }
	                if (mRetrofit == null) {
	                    mRetrofit = new Retrofit.Builder()
	                            .baseUrl(BASE_URL)
	                            .addConverterFactory(StringConverterFactory.create())
	                            .addConverterFactory(GsonConverterFactory.create())
	                            .client(mOkHttpClient)
	                            .build();
	                }
	            }
	        }
	
	        return mRetrofit;
	
	    }
	}

> 注意这里用到了 `StringConverterFactory` 转换类
> Code

	public final class StringConverterFactory extends Converter.Factory {
	
	    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");
	
	    @Override
	    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
	        if (String.class.equals(type)) {
	            return new Converter<ResponseBody, String>() {
	                @Override
	                public String convert(ResponseBody value) throws IOException {
	                    return value.string();
	                }
	            };
	        }
	        return null;
	    }
	
	    @Override
	    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations,
	                                                          Annotation[] methodAnnotations, Retrofit retrofit) {
	        if (String.class.equals(type)) {
	            return new Converter<String, RequestBody>() {
	                @Override
	                public RequestBody convert(String value) throws IOException {
	                    return RequestBody.create(MEDIA_TYPE, value);
	                }
	            };
	        }
	        return null;
	    }
	
	
	    public static Converter.Factory create() {
	        return new StringConverterFactory();
	    }
	}


* 4.1 用Retrofit2来进行网络请求

> ApiCode

	public interface DouBanServiceApi {
	
	
	    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//
	    //                                                                            //
	    //                                                                            //
	    //                             以下是Book请求的示例                             //
	    //                                                                            //
	    //                                                                            //
	    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//
	
	
	    //获取图书信息
	    @GET("/v2/book/{id}")
	    Call<Book> getBookInfo(@Path("id") int id);
	
	    //根据isbn获取图书信息
	    @GET("/v2/book/isbn/{name}")
	    Call<Book> getBookInfo(@Path("name") String id);
	
	    //搜索图书
	    @GET("/v2/book/search")
	    Call<String> getBookInfo(@Query("q") String q, @Query("tag") String tag, @Query("start") int start, @Query("count") int count);
	
		// ....
	}


> 调用

    Retrofit2Utils.getServiceApi().getBookInfo(34232).enqueue(new Callback<Book>() {
        @Override
        public void onResponse(Call<Book> call, Response<Book> response) {
            
        }

        @Override
        public void onFailure(Call<Book> call, Throwable t) {

        }
    });

* 5. 接入Glide

>
	compile 'com.github.bumptech.glide:glide:3.7.0'// glide
	compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar' //glide 集成okhttp3

> 注意，这个我暂时不知道Glide使用的加载器是不是OkHttpClient.之前在配置OkHttp2和Glide的时候是在MyApplication里面写的，待考究。

* 6.接入RxAndroid和RxJava

>
    // RxAndroid
    compile 'io.reactivex:rxandroid:1.2.1'
    // Because RxAndroid releases are few and far between, it is recommended you also
    // explicitly depend on RxJava's latest version for bug fixes and new features.
    compile 'io.reactivex:rxjava:1.1.6'

	compile 'com.squareup.retrofit2:adapter-rxjava:2.0.0'// 这个一定要加上

与此同时：

    //通用的
    private static Retrofit getRetrofit() {

        if (mRetrofit == null) {
            synchronized (Retrofit2Utils.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
                }
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(StringConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .build();
                }
            }
        }
        return mRetrofit;
    }


* 7.RxAndroid的简单实现

> 核心Code

	observer = new Observer<MoviesBean>() {

        @Override
        public void onCompleted() {
            Log.v("way","complete");
        }

        @Override
        public void onError(Throwable e) {
            Log.v("way","onError"+e.getLocalizedMessage());
        }

        @Override
        public void onNext(MoviesBean o) {
            tvResult.setText(o.toString());
        }
    };


    public void load(View v) {

	    NetWork.getService().getTop250(20, 0)
	            .subscribeOn(Schedulers.io())
	            .observeOn(AndroidSchedulers.mainThread())
	            .subscribe(observer);

	}


* 8.接入ButterKnife

> 配置参考[简书文章](http://www.jianshu.com/p/bf9018c1a7f6)


	1.Project的build.gradle文件中增加classpath
	
		buildscript {
		    repositories {
		        jcenter()
		    }
		    dependencies {
		        classpath 'com.android.tools.build:gradle:2.1.0'
		        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
		    }
		}
		allprojects {
		    repositories {
		        jcenter()
		    }
		}
		task clean(type: Delete) {
		    delete rootProject.buildDir
		}

	2.在Module的build.gradle文件中增加plugin
	
		apply plugin: 'com.neenbedankt.android-apt'

	3.在Dependencies中增加下面两句
	
		compile 'com.jakewharton:butterknife:8.0.1'
		apt 'com.jakewharton:butterknife-compiler:8.0.1'


* 9.BaseActivity

> Code

	public abstract class BaseActivity extends AppCompatActivity {
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        AppManager.getAppManager().addActivity(this);// 添加Activity到堆栈
	        initView();//需要设置setContentView
	
	        ButterKnife.bind(this);//绑定ButterKnife
	
	        initListener();//监听
	
	        loadData();//加载数据
	    }
	
	    /**
	     * 加载数据的网络操作
	     */
	    protected abstract void loadData();
	
	
	    /**
	     * setContentView和findViewById操作(可以只用ButterKnife快捷操作)
	     */
	    protected abstract void initView();
	
	    /**
	     * 给控件添加点击监听事件(一般也可以使用ButterKnife)
	     */
	    protected abstract void initListener();
	
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        // 结束Activity&从堆栈中移除
	        AppManager.getAppManager().finishActivity(this);
	    }
	}

* 10.接入EventBus

	> compile 'org.greenrobot:eventbus:3.0.0'

> 使用可以[参看](https://github.com/IvyZh/SimpleEventBus/blob/master/SimpleEventBusDemo/app/src/main/java/com/ivy/simpleeventbusdemo/MainActivity.java)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void basic(String msg) {
        tvResult.setText(msg);
    }

    public void method1(View v) {
        EventBus.getDefault().post(((Button) v).getText().toString() + new Random().nextInt(100));
    }

> 也可以定义一个消息类

	如何使用
	(1)首先需要定义一个消息类，该类可以不继承任何基类也不需要实现任何接口。如：
	
	public class MessageEvent {
	    ......
	}
	(2)在需要订阅事件的地方注册事件
	
	EventBus.getDefault().register(this);
	(3)产生事件，即发送消息
	
	EventBus.getDefault().post(messageEvent);
	(4)处理消息
	
	@Subscribe(threadMode = ThreadMode.PostThread)
	public void XXX(MessageEvent messageEvent) {
	    ...
	}



* 11.MyApplication

> Code


	public class MyApplication extends Application {
	    private static MyApplication mContext;
	
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        mContext = this;
	        initOkHttp();//初始化OkHttp
	
	    }
	
	    /**
	     * 初始化单例OkHttpClient对象
	     */
	    private void initOkHttp() {
	        OkHttpClient client = OkHttp3Utils.getOkHttpClient();
	    }
	
	    public static MyApplication getContext() {
	        return mContext;
	    }
	
	}


* 12.网络请求示例

> 返回对象

    Call<MoviesBean> call2 = Retrofit2Utils.getServiceApi().getTheatersMovies("上海");
    call2.enqueue(new Callback<MoviesBean>() {
        @Override
        public void onResponse(Call<MoviesBean> call, Response<MoviesBean> response) {
            if (response.isSuccessful()) {
                setData(response);
            }

        }

        @Override
        public void onFailure(Call<MoviesBean> call, Throwable t) {

        }
    });


> 返回String

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


> 使用RxAndroid

	- 用Observable代替Call
    @GET("/v2/movie/in_theaters")
    Observable<MoviesBean> getTheatersMoviesObservable(@Query("city") String city);