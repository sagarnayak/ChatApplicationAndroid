package com.sagar.android.chatapp.ui.room;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.sagar.android.chatapp.model.Result;
import com.sagar.android.chatapp.model.Room;
import com.sagar.android.chatapp.model.UserData;
import com.sagar.android.chatapp.repository.Repository;

public class ChatRoomViewModel extends ViewModel {
    private Repository repository;

    public MediatorLiveData<Result> mediatorLiveDataLeaveRoomResult;
    public MediatorLiveData<Room> mediatorLiveDataJoinRoomResult;
    public MediatorLiveData<Result> mediatorLiveDataJoinRoomError;
    public MediatorLiveData<Result> mediatorLiveDataConnectedToSocket;

    public ChatRoomViewModel(Repository repository) {
        this.repository = repository;

        mediatorLiveDataLeaveRoomResult = new MediatorLiveData<>();
        mediatorLiveDataJoinRoomResult = new MediatorLiveData<>();
        mediatorLiveDataJoinRoomError = new MediatorLiveData<>();
        mediatorLiveDataConnectedToSocket = new MediatorLiveData<>();

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
}
