package com.sagar.android.chatapp.ui.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.databinding.RoomListItemBinding;
import com.sagar.android.chatapp.util.OverlapDecoration;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {
    private Context context;

    public RoomListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                RoomListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoomListItemBinding binding;

        public ViewHolder(RoomListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.recyclerViewUsers.setLayoutManager(
                    new LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );
            binding.recyclerViewUsers.setAdapter(
                    new UserHorizontalListAdapter()
            );
            binding.recyclerViewUsers.addItemDecoration(
                    new OverlapDecoration()
            );
        }
    }
}
