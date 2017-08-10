package me.inrush.factory.net;

import me.inrush.factory.model.api.RspModel;
import me.inrush.factory.model.api.account.AccountRspModel;
import me.inrush.factory.model.api.account.RegisterModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网路请求的所有的接口
 *
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.net
 */

public interface RemoteService {
    /**
     * 网络请求的一个注册的接口
     *
     * @param model 注册model
     * @return 回调
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body RegisterModel model);

    /**
     * 绑定设备Id
     * @param pushId 设备Id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);
}
