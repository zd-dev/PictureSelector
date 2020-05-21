package com.zd.pictureselector;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import android.os.Bundle;
import com.selector.picture.EasyPhotos;
import com.selector.picture.callback.SelectCallback;
import com.selector.picture.constant.Type;
import com.selector.picture.entity.Photo;
import com.zd.base.BaseActivity;
import com.zd.base.BaseDialog;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    public static final int REQUEST_CODE_PHOTO = 1012;
    private TextView mTvUrl;
    private TextView mTvSelector;

    @Override
    public int setLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mTvUrl = findViewById(R.id.tv_url);
        mTvSelector = findViewById(R.id.tv_selector);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    @Override
    protected void initEvents(Bundle savedInstanceState) {
        super.initEvents(savedInstanceState);
        mTvSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelectorDialog.create(MainActivity.this)
                  .setOnClickCallback(new BaseDialog.OnClickCallback() {
                      @Override
                      public void onClickType(int type) {
                          setPicture(type);
                      }
                  })
                  .show();
            }
        });
    }

    private void setPicture(int type) {
        switch (type) {
            case 1:
                EasyPhotos.createCamera(this)
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 2:
                EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 3:
                EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
                  .setCount(9)
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 4:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 5:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .setCount(22)
                  .start(new SelectCallback() {
                      @Override
                      public void onResult(ArrayList<Photo> resultPhotos, boolean isOriginal) {
                          if (resultPhotos == null) {
                              return;
                          }
                          String path = "";
                          for (int i = 0; i < resultPhotos.size(); i++) {
                              path = path + resultPhotos.get(i).getPath() + "\n\n";
                          }
                          mTvUrl.setText(path);
                      }
                  });
                break;
            case 6:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .setCount(9)
                  .setMinWidth(500)
                  .setMinHeight(500)
                  .setMinFileSize(1024 * 10)
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 7:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .setCount(9)
                  .setVideo(true)
                  .setGif(true)
                  .start(REQUEST_CODE_PHOTO);
                break;
            case 8:
                EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                  .setFileProviderAuthority("com.zd.pictureselector.fileprovider")
                  .setCount(9)
                  .filter(Type.VIDEO)
                  .start(REQUEST_CODE_PHOTO);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode != -1) {
                return;
            }
            //Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            if (resultPhotos == null) {
                return;
            }
            String path = "";
            for (int i = 0; i < resultPhotos.size(); i++) {
                path = path + resultPhotos.get(i).getPath() + "\n\n";
            }
            mTvUrl.setText(path);
        }
    }
}
