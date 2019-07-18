package com.sagar.android.chatapp.ui.room;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Chat;
import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Event;
import com.sagar.android.chatapp.repository.Repository;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Event<Result>> mediatorLiveDataLeaveRoomResult;
    public MediatorLiveData<Event<Room>> mediatorLiveDataJoinRoomResult;
    public MediatorLiveData<Event<Result>> mediatorLiveDataJoinRoomError;
    public MediatorLiveData<Event<Result>> mediatorLiveDataConnectedToSocket;
    public MediatorLiveData<Event<ArrayList<Chat>>> mediatorLiveDataChats;
    public MediatorLiveData<Event<Room>> mediatorLiveDataRoom;

    public ChatRoomViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLeaveRoomResult = new MediatorLiveData<>();
        mediatorLiveDataJoinRoomResult = new MediatorLiveData<>();
        mediatorLiveDataJoinRoomError = new MediatorLiveData<>();
        mediatorLiveDataConnectedToSocket = new MediatorLiveData<>();
        mediatorLiveDataChats = new MediatorLiveData<>();
        mediatorLiveDataRoom = new MediatorLiveData<>();

        bindToRepo();
    }

    private void bindToRepo() {
        mediatorLiveDataLeaveRoomResult.addSource(
                repository.mutableLiveDataLeaveRoomResult,
                result -> mediatorLiveDataLeaveRoomResult.postValue(result)
        );

        mediatorLiveDataJoinRoomResult.addSource(
                repository.mutableLiveDataJoinRoomResult,
                room -> mediatorLiveDataJoinRoomResult.postValue(room)
        );

        mediatorLiveDataJoinRoomError.addSource(
                repository.mutableLiveDataJoinRoomError,
                result -> mediatorLiveDataJoinRoomError.postValue(result)
        );

        mediatorLiveDataConnectedToSocket.addSource(
                repository.mutableLiveDataConnectedToSocket,
                result -> mediatorLiveDataConnectedToSocket.postValue(result)
        );

        mediatorLiveDataChats.addSource(
                repository.mutableLiveDataChats,
                chats -> mediatorLiveDataChats.postValue(chats)
        );

        mediatorLiveDataRoom.addSource(
                repository.mutableLiveDataRoom,
                room -> mediatorLiveDataRoom.postValue(room)
        );
    }

    public UserData getUserData() {
        return repository.getUserData();
    }

    public void leaveRoom(String roomId) {
        repository.leaveRoom(roomId);
    }

    public void joinRoom(String roomId) {
        repository.joinRoom(roomId);
    }

    public void initialiseSockets() {
        repository.prepareSockets();
    }

    public void disconnectSocket() {
        repository.disconnectSocket();
    }

    public void getChatData(
            String limit,
            String skip,
            String roomId
    ) {
        repository.getChatData(
                limit,
                skip,
                roomId
        );
    }

    public void sendMessage(
            String message,
            String roomId
    ) {
        repository.sendMessage(
                message, roomId);
    }

    public void joinRoomSocket(String roomId) {
        repository.joinRoomSocket(roomId);
    }

    public void getRoom(String roomId) {
        repository.getRoom(roomId);
    }
}
