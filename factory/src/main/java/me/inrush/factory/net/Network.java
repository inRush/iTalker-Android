package me.inrush.factory.net;

import me.inrush.common.Common;
import me.inrush.factory.Factory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 *
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.net
 */

public class Network {
    /**
     * 构建一个Retrofit
     *
     * @return Retrofit
     */
    public static Retrofit getRetrofit() {
        // 得到一个Okhttp Client
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder();

        return builder.baseUrl(Common.Constance.API_URL)
                .client(client)
                // 设置Gson解析
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }
}
