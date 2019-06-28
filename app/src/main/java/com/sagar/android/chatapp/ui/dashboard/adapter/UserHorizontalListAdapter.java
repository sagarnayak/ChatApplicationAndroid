package com.sagar.android.chatapp.ui.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.databinding.MoreCountForUserHorListBinding;
import com.sagar.android.chatapp.databinding.UserHorListItemBinding;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.squareup.picasso.Picasso;

public class UserHorizontalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int USER = 1;
    private static final int COUNT = 2;

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
        if (holder instanceof ViewHolderUser)
            ((ViewHolderUser) holder).bind(position);
        else if (holder instanceof ViewHolderMoreCount)
            ((ViewHolderMoreCount) holder).bind();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 3)
            return COUNT;
        return USER;
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        private UserHorListItemBinding binding;

        public ViewHolderUser(UserHorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int position) {
            switch (position) {
                case 0:
                    Picasso.get()
                            .load(
                                    "https://randomuser.me/api/portraits/women/3.jpg"
                            )
                            .transform(
                                    new CircleTransformation()
                            )
                            .into(
                                    binding.appcompatImageViewUserImage
                            );
                    break;
                case 1:
                    Picasso.get()
                            .load(
                                    "https://randomuser.me/api/portraits/women/4.jpg"
                            )
                            .transform(
                                    new CircleTransformation()
                            )
                            .into(
                                    binding.appcompatImageViewUserImage
                            );
                    break;
                case 2:
                    Picasso.get()
                            .load(
                                    "https://randomuser.me/api/portraits/women/5.jpg"
                            )
                            .transform(
                                    new CircleTransformation()
                            )
                            .into(
                                    binding.appcompatImageViewUserImage
                            );
                    break;
            }
        }
    }

    class ViewHolderMoreCount extends RecyclerView.ViewHolder {
        private MoreCountForUserHorListBinding binding;

        public ViewHolderMoreCount(MoreCountForUserHorListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
        }
    }
}
