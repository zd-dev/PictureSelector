package com.selector.picture.utils.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.selector.picture.callback.SelectCallback;
import com.selector.picture.EasyPhotos;
import com.selector.picture.entity.Photo;
import com.selector.picture.ui.EasyPhotosActivity;

import java.util.ArrayList;

/**
 * HolderFragment
 *
 */
public class HolderFragment extends Fragment {

    private static final int HOLDER_SELECT_REQUEST_CODE = 0x44;
    private static final int HOLDER_PUZZLE_REQUEST_CODE = 0x55;
    private SelectCallback mSelectCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startEasyPhoto(SelectCallback callback) {
        mSelectCallback = callback;
        EasyPhotosActivity.start(this, HOLDER_SELECT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case HOLDER_SELECT_REQUEST_CODE:
                    if (mSelectCallback != null) {
                        ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                        boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
                        mSelectCallback.onResult(resultPhotos,  selectedOriginal);
                    }
                    break;
            }
        }
    }
}
