package com.sagar.android.chatapp.ui.create_room.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.databinding.AddMoreFriendChipBinding;
import com.sagar.android.chatapp.databinding.FriendChipBinding;
import com.sagar.android.chatapp.model.User;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FRIEND = 1;
    private static final int ADD_MORE = 2;

    private ArrayList<User> friends;
    private CallBack callBack;

    public FriendsAdapter(ArrayList<User> friends, CallBack callBack) {
        this.friends = friends;
        this.callBack = callBack;
    }

    public ArrayList<String> getAllIds() {
        ArrayList<String> toReturn = new ArrayList<>();
        for (User user :
                friends) {
            toReturn.add(user.getId());
        }
        return toReturn;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == FRIEND) {
            return new ViewHolderFriend(
                    FriendChipBinding.inflate(
                            LayoutInflater.from(viewGroup.getContext()),
                            viewGroup,
                            false
                    )
            );
        } else {
            return new ViewHolderAddMore(
                    AddMoreFriendChipBinding.inflate(
                            LayoutInflater.from(viewGroup.getContext()),
                            viewGroup,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderFriend) {
            ((ViewHolderFriend) viewHolder).bind(friends.get(i));
        } else if (viewHolder instanceof ViewHolderAddMore) {
            ((ViewHolderAddMore) viewHolder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return friends.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < friends.size())
            return FRIEND;
        return ADD_MORE;
    }

    class ViewHolderFriend extends RecyclerView.ViewHolder {
        private FriendChipBinding binding;

        ViewHolderFriend(FriendChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(User friend) {
            binding.chip.setChipText(
                    friend.getName()
            );
            binding.chip.setOnCloseClickListener(
                    v -> callBack.removeFriend(friend)
            );
        }
    }

    class ViewHolderAddMore extends RecyclerView.ViewHolder {
        private AddMoreFriendChipBinding binding;

        ViewHolderAddMore(AddMoreFriendChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind() {
            binding.chip.setOnSelectClickListener(
                    (v, selected) -> callBack.addMoreFriends()
            );
        }
    }

    public interface CallBack {
        void removeFriend(User friend);

        void addMoreFriends();
    }
}
