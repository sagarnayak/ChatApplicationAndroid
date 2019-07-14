package com.sagar.android.chatapp.ui.room.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagar.android.chatapp.core.URLs;
import com.sagar.android.chatapp.databinding.ChatBubbleLeftBinding;
import com.sagar.android.chatapp.databinding.ChatBubbleRightBinding;
import com.sagar.android.chatapp.model.Chat;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.util.CircleTransformation;
import com.sagar.android.chatapp.util.PicassoForUserAvatar;
import com.sagar.android.chatapp.util.TextDrawableUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatRoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Chat> chats;
    private Picasso picasso;
    private UserData userData;
    private Context context;

    private static final int MY_MSG = 1;
    private static final int OTHERS_MSG = 2;

    public ChatRoomAdapter(ArrayList<Chat> chats, Picasso picasso, UserData userData, Context context) {
        this.chats = chats;
        this.picasso = picasso;
        this.userData = userData;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_MSG)
            return new ViewHolderRight(
                    ChatBubbleRightBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        return new ViewHolderLeft(
                ChatBubbleLeftBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderRight)
            ((ViewHolderRight) holder).bind(chats.get(position), position);
        if (holder instanceof ViewHolderLeft)
            ((ViewHolderLeft) holder).bind(chats.get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        if (chats.get(position).getAuthor().equals(userData.getUser().getId()))
            return MY_MSG;
        return OTHERS_MSG;
    }

    class ViewHolderLeft extends RecyclerView.ViewHolder {
        private ChatBubbleLeftBinding binding;

        ViewHolderLeft(ChatBubbleLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Chat chat, int position) {
            if (
                    (position + 1) == chats.size() ||
                            (
                                    !chat.getAuthorDetail().getId().equals(
                                            chats.get(position + 1).getAuthorDetail().getId()
                                    )
                            )
            ) {
                binding.appcompatImageViewUserImage.setVisibility(View.VISIBLE);
                binding.textViewUserName.setVisibility(View.VISIBLE);
                new PicassoForUserAvatar(
                        context,
                        picasso,
                        new CircleTransformation(),
                        binding.appcompatImageViewUserImage,
                        chat.getAuthorDetail().getId(),
                        chat.getAuthorDetail().getName(),
                        () -> {
                            picasso.invalidate(
                                    URLs.PROFILE_PICTURE_URL + chat.getAuthorDetail().getId()
                            );
                            //noinspection ConstantConditions
                            picasso
                                    .load(
                                            URLs.PROFILE_PICTURE_URL + chat.getAuthorDetail().getId()
                                    )
                                    .transform(
                                            new CircleTransformation()
                                    )
                                    .placeholder(
                                            TextDrawableUtil.getPlaceHolder(
                                                    chat.getAuthorDetail().getName(),
                                                    TextDrawableUtil.Shape.ROUND
                                            )
                                    )
                                    .into(
                                            new Target() {
                                                @Override
                                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                    binding.appcompatImageViewUserImage.setImageBitmap(bitmap);
                                                }

                                                @Override
                                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                }

                                                @Override
                                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                }
                                            }
                                    );
                        }
                );
                binding.textViewUserName.setText(
                        chat.getAuthorDetail().getName()
                );
            } else {
                binding.appcompatImageViewUserImage.setVisibility(View.GONE);
                binding.textViewUserName.setVisibility(View.GONE);
            }
            if (
                    (position + 1) == chats.size() ||
                            (
                                    chat.getCalendarCreated().get(Calendar.DATE) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.DATE)
                            ) ||
                            (
                                    chat.getCalendarCreated().get(Calendar.MONTH) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.MONTH)
                            ) ||
                            (
                                    chat.getCalendarCreated().get(Calendar.YEAR) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.YEAR)
                            )
            ) {
                binding.textViewDate.setVisibility(View.VISIBLE);
                binding.textViewDate.setText(
                        chat.getCalendarCreated().get(Calendar.DATE) + "-" +
                                (chat.getCalendarCreated().get(Calendar.MONTH) + 1) + "-" +
                                chat.getCalendarCreated().get(Calendar.YEAR)
                );
            } else {
                binding.textViewDate.setVisibility(View.GONE);
            }
            binding.textViewMessage.setText(
                    chat.getMessage()
            );
        }
    }

    class ViewHolderRight extends RecyclerView.ViewHolder {
        private ChatBubbleRightBinding binding;

        ViewHolderRight(ChatBubbleRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Chat chat, int position) {
            if (
                    (position + 1) == chats.size() ||
                            (
                                    !chat.getAuthorDetail().getId().equals(
                                            chats.get(position + 1).getAuthorDetail().getId()
                                    )
                            )
            ) {
                binding.appcompatImageViewUserImage.setVisibility(View.VISIBLE);
                binding.textViewUserName.setVisibility(View.VISIBLE);
                new PicassoForUserAvatar(
                        context,
                        picasso,
                        new CircleTransformation(),
                        binding.appcompatImageViewUserImage,
                        chat.getAuthorDetail().getId(),
                        chat.getAuthorDetail().getName(),
                        () -> {
                            picasso.invalidate(
                                    URLs.PROFILE_PICTURE_URL + chat.getAuthorDetail().getId()
                            );
                            //noinspection ConstantConditions
                            picasso
                                    .load(
                                            URLs.PROFILE_PICTURE_URL + chat.getAuthorDetail().getId()
                                    )
                                    .transform(
                                            new CircleTransformation()
                                    )
                                    .placeholder(
                                            TextDrawableUtil.getPlaceHolder(
                                                    chat.getAuthorDetail().getName(),
                                                    TextDrawableUtil.Shape.ROUND
                                            )
                                    )
                                    .into(
                                            new Target() {
                                                @Override
                                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                    binding.appcompatImageViewUserImage.setImageBitmap(bitmap);
                                                }

                                                @Override
                                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                }

                                                @Override
                                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                }
                                            }
                                    );
                        }
                );
                binding.textViewUserName.setText(
                        chat.getAuthorDetail().getName()
                );
            } else {
                binding.appcompatImageViewUserImage.setVisibility(View.GONE);
                binding.textViewUserName.setVisibility(View.GONE);
            }
            if (
                    (position + 1) == chats.size() ||
                            (
                                    chat.getCalendarCreated().get(Calendar.DATE) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.DATE)
                            ) ||
                            (
                                    chat.getCalendarCreated().get(Calendar.MONTH) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.MONTH)
                            ) ||
                            (
                                    chat.getCalendarCreated().get(Calendar.YEAR) !=
                                            chats.get(position + 1).getCalendarCreated().get(Calendar.YEAR)
                            )
            ) {
                binding.textViewDate.setVisibility(View.VISIBLE);
                binding.textViewDate.setText(
                        chat.getCalendarCreated().get(Calendar.DATE) + "-" +
                                (chat.getCalendarCreated().get(Calendar.MONTH) + 1) + "-" +
                                chat.getCalendarCreated().get(Calendar.YEAR)
                );
            } else {
                binding.textViewDate.setVisibility(View.GONE);
            }
            binding.textViewMessage.setText(
                    chat.getMessage()
            );
        }
    }
}
