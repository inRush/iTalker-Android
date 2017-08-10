package me.inrush.factory.data.helper;

import me.inrush.factory.Factory;
import me.inrush.factory.R;
import me.inrush.factory.data.DataSource;
import me.inrush.factory.model.api.RspModel;
import me.inrush.factory.model.api.account.AccountRspModel;
import me.inrush.factory.model.api.account.RegisterModel;
import me.inrush.factory.model.db.User;
import me.inrush.factory.net.Network;
import me.inrush.factory.net.RemoteService;
import me.inrush.factory.persistence.Account;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author inrush
 * @date 2017/8/9.
 * @package me.inrush.factory.data.helper
 */

public class AccountHelper {

    /**
     * 注册方法
     *
     * @param model    注册的model
     * @param callBack 响应回调
     */
    public static void register(final RegisterModel model,
                                final DataSource.CallBack<User> callBack) {
        // 调用 Retrofit 进行代理
        RemoteService service = Network.getRetrofit().create(RemoteService.class);

        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);

        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                // 请求成功,返回全局的model,内部调用了Gson进行解析
                RspModel<AccountRspModel> rspModel = response.body();
                if (rspModel.success()) {
                    AccountRspModel accountRspModel = rspModel.getResult();
                    final User user = accountRspModel.getUser();
                    // 第一种就是直接保存用户到数据库中
                    user.save();

//                        // 第二种保存用户到数据库中
//                        FlowManager.getModelAdapter(User.class)
//                                .save(user);
//
//                        // 第三种放到事务中进行存储
//                        DatabaseDefinition definition = FlowManager.getDatabase(AppDataBase.class);
//                        definition.beginTransactionAsync(new ITransaction() {
//                            @Override
//                            public void execute(DatabaseWrapper databaseWrapper) {
//                                // 第二种保存用户到数据库中
//                                FlowManager.getModelAdapter(User.class)
//                                        .save(user);
//                            }
//                        }).build().execute();
                    //对用户信息进行持久化
                    Account.login(accountRspModel);
                    // 判断绑定状态
                    if (accountRspModel.isBind()) {
                        // 进行数据库的写入和绑定
                        // 然后返回
                        callBack.onDataLoaded(user);
                    } else {
                        bindPushId(callBack);
                    }

                } else {
                    // 进行错误解析
                    Factory.decodeRspCode(rspModel, callBack);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                // 网络请求失败
                callBack.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备Id进行绑定的操作
     *
     * @param callBack CallBack
     */
    public static void bindPushId(final DataSource.CallBack<User> callBack) {
        Account.setBind(true);
    }
}
