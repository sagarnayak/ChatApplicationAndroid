package com.sagar.android.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sagar.android.chatapp.ui.login.Login;

public class MainActivity extends Activity {

    private static final String TAG = "sfbdbfgn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        /*try {
            IO.Options options = new IO.Options();
            options.query = "token=the dummy jwt token here";
            final Socket socket = IO.socket("http://192.168.1.145:3000", options);

            socket.on(
                    "connect",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG, "socket connected");

                            socket.emit("msg_from_client", "Hello server");
                        }
                    }
            );

            socket.on(
                    "msg_from_server",
                    new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.i(TAG, "Server : " + Arrays.toString(args));
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
        }*/

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(
                                new Intent(
                                        MainActivity.this,
                                        Login.class
                                )
                        );
                    }
                },
                1000
        );
    }
}
