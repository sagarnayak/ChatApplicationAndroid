package com.sagar.android.chatapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            IO.Options options = new IO.Options();
            options.query = "token=dvdvdfvvs";
            Socket socket = IO.socket("http://192.168.0.101:3000/", options);

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
