package com.example.cavanscoffin_thomas.ichack;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PresenceChannel;
import com.pusher.client.channel.PresenceChannelEventListener;
import com.pusher.client.channel.User;
import com.pusher.client.util.HttpAuthorizer;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private int connectedUsers = 0;
    private boolean isLive = true;
    private TextView countText;
    private TextView updateText;
    private Button liveButton;
    private PresenceChannel channel = null;
    private Pusher pusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpAuthorizer authorizer = new HttpAuthorizer("https://mighty-falls-32265.herokuapp.com/pusher/auth");
        PusherOptions options = new PusherOptions().setAuthorizer(authorizer);
        pusher = new Pusher("3eb9bec48fc38569f209", options);
        countText = (TextView) findViewById(R.id.countNum);
        updateText = (TextView) findViewById(R.id.countText);
        liveButton = (Button) findViewById(R.id.liveButton);

        this.channel = pusher.subscribePresence("presence-online-users",
                new PresenceChannelEventListener() {
                    @Override
                    public void onEvent(String s, String s1, String s2) {
                        Log.d("error", s);
                    }

                    @Override
                    public void onSubscriptionSucceeded(String s) {
                        setUsers();

                    }

                    @Override
                    public void onAuthenticationFailure(String s, Exception e) {
                        Log.d("error", s);

                    }

                    @Override
                    public void onUsersInformationReceived(String s, Set<User> set) {

                    }

                    @Override
                    public void userSubscribed(String s, User user) {
                        setUsers();
                    }

                    @Override
                    public void userUnsubscribed(String s, User user) {
                        setUsers();
                    }
                });

        pusher.connect();

        liveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLive == true){
                    updateText.setText("Last Current State:");
                    liveButton.setText("Go Live!");
                    isLive = false;
                    pusher.unsubscribe(String.valueOf(channel.getName()));
                }else{
                    updateText.setText("Current Live Users:");
                    liveButton.setText("Go Offline");
                    isLive = true;
                    pusher.subscribePresence(String.valueOf(channel.getName()));
                }
            }
        });
    }

    public void setUsers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectedUsers = channel.getUsers().size();
                countText.setText(String.valueOf(connectedUsers));
            }
        });

    }

    public void leave() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }


}
