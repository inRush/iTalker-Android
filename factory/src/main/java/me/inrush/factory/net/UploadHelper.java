package me.inrush.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.Date;

import me.inrush.factory.Factory;
import me.inrush.utils.HashUtil;

/**
 * 上传帮助类,用于上传任意文件到OSS
 *
 * @author inrush
 * @date 2017/8/8.
 * @package me.inrush.factory.net
 */

public class UploadHelper {

    private static final String TAG = "UploadHelper";

    private static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAITkAIo6A1eMPm";
    private static final String ACCESS_KEY_SECRET = "Mr7Iw53Gk5Ki8R5fo7hzJ9VMiGL8G5";
    private static final String BUCKET_NAME = "italker-inrush";

    private static OSS getClient() {
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考访问控制章节
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        OSS oss = new OSSClient(Factory.app(), ENDPOINT, credentialProvider);
        return oss;
    }

    /**
     * 上传的最终方法,成功则返回一个路径
     *
     * @param objectKey 上传上去之后,在服务器中的一个独立的key
     * @param path      需要上传的文件的路径
     * @return 返回成功上传后的文件路径
     */
    private static String upload(String objectKey, String path) {
        // 构造一个上传的请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, path);

        try {
            // 初始化上传的client
            OSS client = getClient();
            // 开始同步上传
            PutObjectResult result = client.putObject(request);
            // 得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME, objectKey);
            Log.d(TAG, "upload: " + url);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            // 如果有异常则返回空
            return null;
        }
    }

    /**
     * 上传的普通图片
     *
     * @param path 本地路径
     * @return 上传成功后的外网URL
     */
    public static String uploadImage(String path) {
        String key = getImageObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传的头像
     *
     * @param path 本地路径
     * @return 上传成功后的外网URL
     */
    public static String uploadPortrait(String path) {
        String key = getPortraitObjKey(path);
        return upload(key, path);
    }

    /**
     * 上传的录音
     *
     * @param path 本地路径
     * @return 上传成功后的外网URL
     */
    public static String uploadAudio(String path) {
        String key = getAudioObjKey(path);
        return upload(key, path);
    }

    private static String getPortraitObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateStr = getDateString();
        return String.format("portrait/%s/%s.jpg", dateStr, fileMd5);
    }

    private static String getImageObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateStr = getDateString();
        return String.format("image/%s/%s.jpg", dateStr, fileMd5);
    }

    private static String getAudioObjKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateStr = getDateString();
        return String.format("audio/%s/%s.mp3", dateStr, fileMd5);
    }


    /**
     * 返回分月存储日期文件夹名称
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }
}
