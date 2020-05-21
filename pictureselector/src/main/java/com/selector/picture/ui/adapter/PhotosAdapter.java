package com.selector.picture.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.selector.picture.constant.Type;
import com.selector.picture.entity.Photo;
import com.selector.picture.result.Result;
import com.selector.picture.result.Setting;
import com.selector.picture.utils.media.DurationUtils;
import com.selector.picture.R;
import com.selector.picture.ui.widget.PressedImageView;

import java.util.ArrayList;

/**
 * 专辑相册适配器
 */
public class PhotosAdapter extends RecyclerView.Adapter {
    private static final int TYPE_CAMERA = 1;
    private static final int TYPE_ALBUM_ITEMS = 2;

    private ArrayList<Object> dataList;
    private LayoutInflater mInflater;
    private OnClickListener listener;
    private boolean unable, isSingle;
    private int singlePosition;

    private boolean clearAd = false;

    public PhotosAdapter(Context cxt, ArrayList<Object> dataList, OnClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
        this.mInflater = LayoutInflater.from(cxt);
        this.unable = Result.count() == Setting.count;
        this.isSingle = Setting.count == 1;
    }

    public void change() {
        unable = Result.count() == Setting.count;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CAMERA:
                return new CameraViewHolder(mInflater.inflate(R.layout.item_camera_easy_photos, parent, false));
            default:
                return new PhotoViewHolder(mInflater.inflate(R.layout.item_rv_photos_easy_photos, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final int p = position;
        if (holder instanceof PhotoViewHolder) {
            final Photo item = (Photo) dataList.get(p);
            if (item == null) return;
            updateSelector(((PhotoViewHolder) holder).ivPhoto, ((PhotoViewHolder) holder).tvSelector, item.selected, item, p);
            String path = item.path;
            Uri uri = item.uri;
            String type = item.type;
            long duration = item.duration;
            final boolean isGif = path.endsWith(Type.GIF) || type.endsWith(Type.GIF);
            if (Setting.showGif && isGif) {
                Setting.imageEngine.loadGifAsBitmap(((PhotoViewHolder) holder).ivPhoto.getContext(), uri,
                  ((PhotoViewHolder) holder).ivPhoto);
                ((PhotoViewHolder) holder).tvType.setText(R.string.gif_easy_photos);
                ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
            } else if (Setting.showVideo && type.contains(Type.VIDEO)) {
                Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), uri,
                  ((PhotoViewHolder) holder).ivPhoto);
                ((PhotoViewHolder) holder).tvType.setText(DurationUtils.format(duration));
                ((PhotoViewHolder) holder).tvType.setVisibility(View.VISIBLE);
            } else {
                Setting.imageEngine.loadPhoto(((PhotoViewHolder) holder).ivPhoto.getContext(), uri,
                  ((PhotoViewHolder) holder).ivPhoto);
                ((PhotoViewHolder) holder).tvType.setVisibility(View.GONE);
            }

            ((PhotoViewHolder) holder).vSelector.setVisibility(View.VISIBLE);
            ((PhotoViewHolder) holder).tvSelector.setVisibility(View.VISIBLE);
            ((PhotoViewHolder) holder).ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int realPosition = p;
                    if (Setting.hasPhotosAd()) {
                        realPosition--;
                    }
                    if (Setting.isShowCamera && !Setting.isBottomRightCamera()) {
                        realPosition--;
                    }
                    listener.onPhotoClick(p, realPosition);
                }
            });

            ((PhotoViewHolder) holder).vSelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSingle) {
                        singleSelector(item, p);
                        return;
                    }
                    if (unable) {
                        if (item.selected) {
                            Result.removePhoto(item);
                            if (unable) {
                                unable = false;
                            }
                            listener.onSelectorChanged();
                            notifyDataSetChanged();
                            return;
                        }
                        listener.onSelectorOutOfMax(null);
                        return;
                    }
                    item.selected = !item.selected;
                    if (item.selected) {
                        int res = Result.addPhoto(item);
                        if (res != 0) {
                            listener.onSelectorOutOfMax(res);
                            item.selected = false;
                            return;
                        }
                        ((PhotoViewHolder) holder).tvSelector.setBackgroundResource(R.drawable.shap_icon_check);
                        ((PhotoViewHolder) holder).tvSelector.setText(String.valueOf(Result.count()));
                        //((PhotoViewHolder) holder).ivPhoto.setColorFilter(Color.parseColor("#77000000"));
                        if (Result.count() == Setting.count) {
                            unable = true;
                            notifyDataSetChanged();
                        }
                    } else {
                        Result.removePhoto(item);
                        if (unable) {
                            unable = false;
                        }
                        notifyDataSetChanged();
                    }
                    listener.onSelectorChanged();
                }
            });
            return;
        }

        if (holder instanceof CameraViewHolder) {
            ((CameraViewHolder) holder).flCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCameraClick();
                }
            });
        }
    }

    public void clearAd() {
        clearAd = true;
        notifyDataSetChanged();
    }

    private void singleSelector(Photo photo, int position) {
        if (!Result.isEmpty()) {
            if (Result.getPhotoPath(0).equals(photo.path)) {
                Result.removePhoto(photo);
                notifyItemChanged(position);
            } else {
                Result.removePhoto(0);
                Result.addPhoto(photo);
                notifyItemChanged(singlePosition);
                notifyItemChanged(position);
            }
        } else {
            Result.addPhoto(photo);
            notifyItemChanged(position);
        }
        listener.onSelectorChanged();
    }

    private void updateSelector(PressedImageView ivPhoto, TextView tvSelector, boolean selected, Photo photo, int position) {
        if (selected) {
            String number = Result.getSelectorNumber(photo);
            if (number.equals("0")) {
                tvSelector.setBackgroundResource(R.drawable.shap_icon_un_check);
                tvSelector.setText(null);
                //ivPhoto.setColorFilter(null);
                return;
            }
            tvSelector.setText(number);
            tvSelector.setBackgroundResource(R.drawable.shap_icon_check);
            //ivPhoto.setColorFilter(Color.parseColor("#77000000"));
            if (isSingle) {
                singlePosition = position;
                tvSelector.setText("1");
            }
        } else {
            if (unable) {
                tvSelector.setBackgroundResource(R.drawable.shap_icon_un_check);
            } else {
                tvSelector.setBackgroundResource(R.drawable.shap_icon_un_check);
            }
            tvSelector.setText(null);
            //ivPhoto.setColorFilter(null);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            if (Setting.isShowCamera && !Setting.isBottomRightCamera()) {
                return TYPE_CAMERA;
            }
        }
        if (1 == position && !Setting.isBottomRightCamera()) {
            if (Setting.hasPhotosAd() && Setting.isShowCamera) {
                return TYPE_CAMERA;
            }
        }
        return TYPE_ALBUM_ITEMS;
    }

    public interface OnClickListener {
        void onCameraClick();

        void onPhotoClick(int position, int realPosition);

        void onSelectorOutOfMax(@Nullable Integer result);

        void onSelectorChanged();
    }

    private class CameraViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout flCamera;

        CameraViewHolder(View itemView) {
            super(itemView);
            this.flCamera = itemView.findViewById(R.id.fl_camera);
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        final PressedImageView ivPhoto;
        final TextView tvSelector;
        final View vSelector;
        final TextView tvType;

        PhotoViewHolder(View itemView) {
            super(itemView);
            this.ivPhoto = itemView.findViewById(R.id.iv_photo);
            this.tvSelector = itemView.findViewById(R.id.tv_selector);
            this.vSelector = itemView.findViewById(R.id.v_selector);
            this.tvType = itemView.findViewById(R.id.tv_type);
        }
    }
}
