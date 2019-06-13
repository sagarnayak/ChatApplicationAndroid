package com.sagar.android.chatapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sfbdbfgn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            IO.Options options = new IO.Options();
            options.query = "token=the dummy jwt token here";
            final Socket socket = IO.socket("http://192.168.1.145:3000", options);

            socket.on(
                    "connect",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG, "socket connected");

                            socket.emit("msg_from_client","Hello server");
                        }
                    }
            );

            socket.on(
                    "msg_from_server",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG,"Server : "+ Arrays.toString(args));
                        }
                    }
            );

            socket.on(
                    "disconnect",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG, "socket disconnected");
                        }
                    }
            );

            socket.on(
                    "error",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG, "socket auth" + Arrays.toString(args));
                        }
                    }
            );

            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.i(TAG, "socket failed : " + e.toString());
        }
    }
}
