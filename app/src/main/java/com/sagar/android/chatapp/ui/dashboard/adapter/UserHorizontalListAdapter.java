package com.sagar.android.chatapp.ui.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.MoreCountForUserHorListBinding;
import com.sagar.android.chatapp.databinding.UserHorListItemBinding;
import com.sagar.android.chatapp.model.User;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.TextDrawableUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserHorizontalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int USER = 1;
    private static final int COUNT = 2;

    private static final int MAX_VISIBLE_USER = 5;

    private ArrayList<User> users;

    public UserHorizontalListAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == USER)
            return new ViewHolderUser(
                    UserHorListItemBinding.inflate(
                            LayoutInflater.from(
                                    parent.getContext()
                            ),
                            parent,
                            false
                    )
            );
        else
            return new ViewHolderMoreCount(
                    MoreCountForUserHorListBinding.inflate(
                            LayoutInflater.from(
                                    parent.getContext()
                            ),
                            parent,
                            false
                    )
            );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderUser) {
            ((ViewHolderUser) holder).bind(users.get(position));
        } else if (holder instanceof ViewHolderMoreCount)
            ((ViewHolderMoreCount) holder).bind();
    }

    @Override
    public int getItemCount() {
        if (
                users.size() < MAX_VISIBLE_USER + 1
        ) {
            return users.size();
        } else {
            return MAX_VISIBLE_USER + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (
                users.size() < MAX_VISIBLE_USER + 1
        ) {
            return USER;
        } else {
            if (position < MAX_VISIBLE_USER)
                return USER;
            else return COUNT;
        }
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        private UserHorListItemBinding binding;

        public ViewHolderUser(UserHorListItemBinding binding) {
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
        }
    }

    class ViewHolderMoreCount extends RecyclerView.ViewHolder {
        private MoreCountForUserHorListBinding binding;

        public ViewHolderMoreCount(MoreCountForUserHorListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.textViewMoreCount.setText(
                    String.valueOf(
                            users.size() - MAX_VISIBLE_USER
                    )
            );
        }
    }
}
