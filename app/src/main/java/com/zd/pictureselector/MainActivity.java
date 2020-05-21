package com.zd.pictureselector;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.selector.picture.EasyPhotos;
import com.selector.picture.entity.Photo;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_PHOTO = 1012;
    private TextView mTvUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvUrl = findViewById(R.id.tv_url);
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

    public void onClickSingle(View view) {
        EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
          .start(REQUEST_CODE_PHOTO);
    }

    public void onClickDouble(View view) {
        EasyPhotos.createAlbum(this, false, GlideEngine.getInstance())
          .setCount(9)
          .start(REQUEST_CODE_PHOTO);
    }
}
