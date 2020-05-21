package com.selector.picture.ui.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selector.picture.entity.AlbumItem;
import com.selector.picture.result.Setting;
import com.selector.picture.R;

import java.util.ArrayList;

/**
 * 媒体列表适配器
 */

public class AlbumItemsAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ALBUM_ITEMS = 1;

    private ArrayList<Object> dataList;
    private LayoutInflater mInflater;
    private int selectedPosition;
    private OnClickListener listener;
    private int adPosition = 0;
    private int padding = 0;

    private boolean clearAd = false;

    public interface OnClickListener {
        void onAlbumItemClick(int position, int realPosition);
    }


    public AlbumItemsAdapter(Context cxt, ArrayList<Object> list, int selectedPosition, OnClickListener listener) {
        this.dataList = list;
        this.mInflater = LayoutInflater.from(cxt);
        this.listener = listener;
        this.selectedPosition = selectedPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumItemsViewHolder(mInflater.inflate(R.layout.item_dialog_album_items_easy_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int p = position;
        if (holder instanceof AlbumItemsViewHolder) {
            if (padding == 0) {
                padding = ((AlbumItemsViewHolder) holder).mRoot.getPaddingLeft();
            }
            if (p == getItemCount() - 1) {
                ((AlbumItemsViewHolder) holder).mRoot.setPadding(padding, padding, padding, padding);
            } else {
                ((AlbumItemsViewHolder) holder).mRoot.setPadding(padding, padding, padding, 0);
            }
            AlbumItem item = (AlbumItem) dataList.get(p);
            Setting.imageEngine.loadPhoto(((AlbumItemsViewHolder) holder).ivAlbumCover.getContext(), item.coverImageUri, ((AlbumItemsViewHolder) holder).ivAlbumCover);
            ((AlbumItemsViewHolder) holder).tvAlbumName.setText(item.name);
            ((AlbumItemsViewHolder) holder).tvAlbumPhotosCount.setText(String.valueOf(item.photos.size()));
            if (selectedPosition == p) {
                ((AlbumItemsViewHolder) holder).ivSelected.setVisibility(View.VISIBLE);
            } else {
                ((AlbumItemsViewHolder) holder).ivSelected.setVisibility(View.INVISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int realPosition = p;
                    if (Setting.hasAlbumItemsAd()) {
                        if (p > adPosition) {
                            realPosition--;
                        }
                    }
                    int tempSelected = selectedPosition;
                    selectedPosition = p;
                    notifyItemChanged(tempSelected);
                    notifyItemChanged(p);
                    listener.onAlbumItemClick(p, realPosition);
                }
            });
            return;
        }

    }

    public void clearAd() {
        clearAd = true;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int realPosition = position;
        if (Setting.hasAlbumItemsAd()) {
            if (position > adPosition) {
                realPosition--;
            }
        }
        int tempSelected = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(tempSelected);
        notifyItemChanged(position);
        listener.onAlbumItemClick(position, realPosition);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ALBUM_ITEMS;
    }

    public class AlbumItemsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumCover;
        TextView tvAlbumName;
        TextView tvAlbumPhotosCount;
        ImageView ivSelected;
        ConstraintLayout mRoot;

        AlbumItemsViewHolder(View itemView) {
            super(itemView);
            this.ivAlbumCover = (ImageView) itemView.findViewById(R.id.iv_album_cover);
            this.tvAlbumName = (TextView) itemView.findViewById(R.id.tv_album_name);
            this.tvAlbumPhotosCount = (TextView) itemView.findViewById(R.id.tv_album_photos_count);
            this.ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
            this.mRoot = (ConstraintLayout) itemView.findViewById(R.id.m_root_view);
        }
    }
}
