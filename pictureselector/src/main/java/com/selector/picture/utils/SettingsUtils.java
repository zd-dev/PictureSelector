package com.selector.picture.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.selector.picture.constant.Code;

/**
 * 系统设置界面启动器
 */

public class SettingsUtils {

    /**
     * 启动应用详情界面
     * @param cxt 上下文
     * @param packageName 应用包名
     */
    public static void startMyApplicationDetailsForResult(Activity cxt, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
        cxt.startActivityForResult(intent, Code.REQUEST_SETTING_APP_DETAILS);
    }
}
