package com.sagar.android.chatapp.ui.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.databinding.RoomListItemBinding;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.util.OverlapDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RoomSearchListAdapter extends RecyclerView.Adapter<RoomSearchListAdapter.ViewHolder> {
    public interface CallBack {
        void roomSelected(Room room);
    }

    private ArrayList<Room> rooms;
    private Context context;
    private Picasso picasso;
    private CallBack callBack;

    public RoomSearchListAdapter(ArrayList<Room> rooms, Context context, Picasso picasso, CallBack callBack) {
        this.rooms = rooms;
        this.context = context;
        this.picasso = picasso;
        this.callBack = callBack;
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
        holder.bind(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoomListItemBinding binding;

        public ViewHolder(RoomListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.recyclerViewUsers.addItemDecoration(
                    new OverlapDecoration()
            );
        }

        public void bind(Room room) {
            binding.textViewRoomName.setText(
                    room.getName()
            );
            binding.recyclerViewUsers.setLayoutManager(null);
            binding.recyclerViewUsers.setAdapter(null);
            binding.recyclerViewUsers.setLayoutManager(
                    new LinearLayoutManager(
                            context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );
            binding.recyclerViewUsers.setAdapter(
                    new UserHorizontalListAdapter(
                            room.getUsers(),
                            picasso,
                            context
                    )
            );
            binding.container.setOnClickListener(
                    view -> callBack.roomSelected(room)
            );
        }
    }
}
