package com.selector.picture.utils.bitmap;

import java.io.File;
import java.io.IOException;

/**
 * 保存图片到本地的回调
 */

public interface SaveBitmapCallBack {
    void onSuccess(File file);

    void onIOFailed(IOException exception);

    void onCreateDirFailed();
}
