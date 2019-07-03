package com.sagar.android.chatapp.ui.create_room.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.FriendListItemBinding;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.TextDrawableUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    public interface CallBack {
        void userSelected(User user);
    }

    private ArrayList<User> users;
    private CallBack callBack;

    public SearchResultAdapter(ArrayList<User> users, CallBack callBack) {
        this.users = users;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                FriendListItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private FriendListItemBinding binding;

        public ViewHolder(FriendListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            //noinspection ConstantConditions
            Picasso.get()
                    .load(
                            URLs.PROFILE_PICTURE_URL + user.getId()
                    )
                    .transform(
                            new CircleTransformation()
                    )
                    .placeholder(
                            TextDrawableUtil.getPlaceHolder(
                                    user.getName(),
                                    TextDrawableUtil.Shape.ROUND
                            )
                    )
                    .into(
                            binding.appcompatImageViewUserImage
                    );

            binding.textViewUserName.setText(
                    user.getName()
            );

            binding.container.setOnClickListener(
                    view -> callBack.userSelected(user)
            );
        }
    }
}
