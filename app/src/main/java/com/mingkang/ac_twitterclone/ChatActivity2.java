package com.mingkang.ac_twitterclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity2 extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private ArrayList<String> chatArray,messageArray;
    private ArrayAdapter adapter;
    private String selectedUser, currentUser;
    private Button btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser = ParseUser.getCurrentUser().getUsername();
        selectedUser = getIntent().getStringExtra("receiverUsername");
        setTitle(selectedUser);
        listView = findViewById(R.id.chatListView);

        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);
        retrieveMessage();
    }

    @Override
    public void onClick(View v) {
        EditText edtMessage = findViewById(R.id.edtMessage);
        ParseObject message = new ParseObject("Chat");
        message.put("sender",currentUser);
        message.put("recipient",selectedUser);
        message.put("message",edtMessage.getText().toString());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                    Toast.makeText(ChatActivity2.this,"Message send!",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ChatActivity2.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        retrieveMessage();
    }

    public void retrieveMessage(){
        chatArray = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatArray);
        listView.setAdapter(adapter);
        final ParseQuery<ParseObject> messageQuery = ParseQuery.getQuery("Chat");
        String[] usernameInvolved = {currentUser,selectedUser};
        messageQuery.whereContainedIn("sender", Arrays.asList(usernameInvolved));
        messageQuery.whereContainedIn("recipient", Arrays.asList(usernameInvolved));
        messageQuery.orderByAscending("createdAt");
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    if(objects.size()>0){
                        for(ParseObject eachMessage : objects){
                            chatArray.add(eachMessage.get("sender")+": "+eachMessage.get("message"));
                        }
                        adapter.notifyDataSetChanged();
                    } else
                        Toast.makeText(ChatActivity2.this,"No message found",Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ChatActivity2.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
