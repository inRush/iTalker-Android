package me.inrush.common;

/**
 * @author inrush
 * @date 2017/7/21.
 */

public class Common {
    // 一些不可变的参数,通常用于一些配置
    public interface Constance {
        // 手机号的正则
        String REGES_PHONE = "[1][3,4,5,7,8][0-9]{9}$";

        // 请求HOST
        String API_HOST = "192.168.0.188:8080";
        // 请求根路径
        String API_BASE_PATH = "/api/";
        // 请求URL
        String API_URL = String.format("http://%s%s", API_HOST, API_BASE_PATH);
    }
}
