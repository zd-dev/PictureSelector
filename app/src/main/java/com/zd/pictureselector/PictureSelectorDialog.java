package com.zd.pictureselector;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zd.base.BaseDialog;

/**
 * 选择 dialog
 */
public class PictureSelectorDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private TextView mTvCamera;
    private TextView mTvAlbumSingle;
    private TextView mTvAlbumMulti;
    private TextView mTvAlbumCameraSingle;
    private TextView mTvAlbumCameraMulti;
    private TextView mTvAlbumSize;
    private TextView mTvAlbumHasVideoGif;
    private TextView mTvAlbumOnlyVideo;

    public static PictureSelectorDialog create(Context context) {
        return new PictureSelectorDialog(context);
    }

    private PictureSelectorDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int getContentView() {
        return R.layout.dialog_heartbeat_state;
    }

    @Override
    protected float setDialogWith() {
        return 1f;
    }

    @Override
    protected void getWindows(Window window) {
        super.getWindows(window);
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.PictureDialog);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mTvCamera = findViewById(R.id.tv_camera);
        mTvAlbumSingle = findViewById(R.id.tv_album_single);
        mTvAlbumMulti = findViewById(R.id.tv_album_multi);
        mTvAlbumCameraSingle = findViewById(R.id.tv_album_camera_single);
        mTvAlbumCameraMulti = findViewById(R.id.tv_album_camera_multi);
        mTvAlbumSize = findViewById(R.id.tv_album_size);
        mTvAlbumHasVideoGif = findViewById(R.id.tv_album_has_video_gif);
        mTvAlbumOnlyVideo = findViewById(R.id.tv_album_only_video);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        mTvCamera.setOnClickListener(this);
        mTvAlbumSingle.setOnClickListener(this);
        mTvAlbumMulti.setOnClickListener(this);
        mTvAlbumCameraSingle.setOnClickListener(this);
        mTvAlbumCameraMulti.setOnClickListener(this);
        mTvAlbumSize.setOnClickListener(this);
        mTvAlbumHasVideoGif.setOnClickListener(this);
        mTvAlbumOnlyVideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_camera:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(1);
                }
                break;
            case R.id.tv_album_single:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(2);
                }
                break;
            case R.id.tv_album_multi:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(3);
                }
                break;
            case R.id.tv_album_camera_single:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(4);
                }
                break;
            case R.id.tv_album_camera_multi:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(5);
                }
                break;
            case R.id.tv_album_size:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(6);
                }
                break;
            case R.id.tv_album_has_video_gif:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(7);
                }
                break;
            case R.id.tv_album_only_video:
                dismiss();
                if (onClickCallback != null) {
                    onClickCallback.onClickType(8);
                }
                break;
            default:
                break;
        }
    }
}








