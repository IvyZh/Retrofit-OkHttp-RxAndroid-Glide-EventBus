package com.ivy.commondemo.net.api;

import com.ivy.commondemo.domain.MoviesBean;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Ivy on 2016/10/11.
 *
 * @description: 存放所有的网络请求-Demo示例
 */

public interface ServiceApi {


    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//
    //                                                                            //
    //                                                                            //
    //                             以下是所有请求的示例                              //
    //                                                                            //
    //                                                                            //
    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx//


    /////////////////////////////////////////////////////////////////
    //////                                                    ///////
    //////                     Get请求                        ///////
    //////                                                   ///////
    ////////////////////////////////////////////////////////////////
    //@Query   @QueryMap    Query集合  @Path

    @GET("/v2/movie/top250")
    Call<MoviesBean> getTop250Movies(@Query("start") int start, @Query("count") int count);

    @GET("/v2/movie/in_theaters")
    Call<MoviesBean> getTheatersMovies(@Query("city") String city);

    @GET("/v2/movie/in_theaters")
    Call<String> getTheatersMoviesString(@Query("city") String city);//还可以将结果转换成String


    @GET("/v2/movie/search")
    Call<MoviesBean> getSearchMovies(@QueryMap Map<String, String> params);

    @GET("/v2/movie/new_movies")
    Call<MoviesBean> getNewMovies();// 需要权限


    //假如你需要添加相同Key值，但是value却有多个的情况，一种方式是添加多个@Query参数，还有一种简便的方式是将所有的value放置在列表中，然后在同一个@Query下完成添加
    @GET("book/search")
    Call<MoviesBean> getSearchBooks(@Query("q") List<String> name);

    @GET("book/{id}")
    Call<MoviesBean> getBook(@Path("id") String id);


    /////////////////////////////////////////////////////////////////
    //////                                                    ///////
    //////                     POST请求                       ///////
    //////                                                   ///////
    ////////////////////////////////////////////////////////////////


    // @field  @FieldMap @Body

    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviews(@Field("book") String bookId, @Field("title") String title, @Field("content") String content, @Field("rating") String rating);

    // FormUrlEncoded将会自动将请求参数的类型调整为application/x-www-form-urlencoded，假如content传递的参数为Good Luck，那么最后得到的请求体就是
    //ield注解将每一个请求参数都存放至请求体中，还可以添加encoded参数，该参数为boolean型，具体的用法为 @Field(value = "book", encoded = true) String book


    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviewsMap(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("book/reviews")
    Call<String> addReviews(@Body MoviesBean reviews);


    /////////////////////////////////////////////////////////////////
    //////                                                    ///////
    //////                     上传请求                        ///////
    //////                                                   ///////
    ////////////////////////////////////////////////////////////////

    //上传单个文件，Filedata是一个可传可修改的字段，type也是
    @Multipart
    @POST("/utility/uploadhead")
    Call<String> uploadHead(@Part("type") String type, @Part("Filedata\";filename=\"1.jpg") RequestBody file);

    //上传多个文件，
    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs1,
                             @Part("file\"; filename=\"image.png\"") RequestBody imgs3);

    @Multipart
    @POST("/upload")
    Call<String> uploadImage(@Part("fileName") String description, @Part("file\"; filename=\"image.png\"") RequestBody... imgs);


}
