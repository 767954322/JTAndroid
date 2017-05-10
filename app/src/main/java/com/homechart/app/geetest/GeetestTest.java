package com.homechart.app.geetest;

import android.content.Context;
import android.content.DialogInterface;

import com.homechart.app.commont.utils.CustomProgress;
import com.homechart.app.commont.utils.ToastUtils;
import com.homechart.app.home.activity.UserRegisterActivity;
import com.squareup.okhttp.Interceptor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/18/018.
 */

public class GeetestTest {
    //2：打开验证界面
    public static void openGtTest(final Context ctx, JSONObject params, final CallBack callBack) {
        GtDialog dialog = new GtDialog(ctx, params);
        // 启用debug可以在webview上看到验证过程的一些数据
        dialog.setDebug(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //TODO 取消验证
                ToastUtils.showCenter(ctx, "已取消");
            }
        });
        dialog.setGtListener(new GtDialog.GtListener() {
                                 @Override
                                 public void gtResult(boolean success, String result) {
                                     if (success) {
                                         try {
                                             JSONObject jsonObject = new JSONObject(result);
                                             String challenge = jsonObject.getString("geetest_challenge");
                                             String validate = jsonObject.getString("geetest_validate");
                                             String seccode = jsonObject.getString("geetest_seccode");
                                             //后续操作
                                             callBack.geetestCallBack(challenge, validate, seccode);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 }
                                 @Override
                                 public void gtCallClose() {
                                     ToastUtils.showCenter(ctx, "已取消");
                                 }

                                 @Override
                                 public void gtCallReady(Boolean status) {
                                     CustomProgress.cancelDialog();
                                     if (!status) {
                                         ToastUtils.showCenter(ctx, "加载失败，请重新加载");
                                     }
                                 }

                                 @Override
                                 public void gtError() {
                                     CustomProgress.cancelDialog();
                                     ToastUtils.showCenter(ctx, "加载失败，请重新加载");
                                 }
                             }

        );
    }

    public interface CallBack {
        public void geetestCallBack(String challenge, String validate, String seccode);
    }
}
