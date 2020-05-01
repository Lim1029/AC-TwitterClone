package com.mingkang.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private ArrayList<String> chatArray;
    private ArrayAdapter adapter;
    private String selectedUser;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        selectedUser = getIntent().getStringExtra("receiverUsername");
        setTitle(selectedUser);
        listView = findViewById(R.id.chatListView);
        chatArray = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,chatArray);
        btnSend = findViewById(R.id.btnSend);
        listView.setAdapter(adapter);

        ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
        ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

        firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        firstUserChatQuery.whereEqualTo("recipient", selectedUser);

        secondUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        secondUserChatQuery.whereEqualTo("recipient", selectedUser);

        ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
        allQueries.add(firstUserChatQuery);
        allQueries.add(secondUserChatQuery);

        ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
        myQuery.orderByAscending("createdAt");

        myQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size()>0 && e==null){
                    for(ParseObject chatObject : objects){
                        String message = chatObject.get("message")+"";
                        if(chatObject.get("sender").equals(ParseUser.getCurrentUser().getUsername()))
                            message = ParseUser.getCurrentUser().getUsername() + ": " + message;
                        if(chatObject.get("sender").equals(selectedUser))
                            message = selectedUser+": "+ message;
                        chatArray.add(message);
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });
        btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final EditText edtMessage = findViewById(R.id.edtMessage);
        ParseObject chat = new ParseObject("Chat");
        chat.put("sender", ParseUser.getCurrentUser().getUsername().toString());
        chat.put("recipient", selectedUser);
        chat.put("message", edtMessage.getText().toString());
        chat.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Toast.makeText(ChatActivity.this, "Mesasge send!", Toast.LENGTH_SHORT).show();
                    chatArray.add(ParseUser.getCurrentUser().getUsername() + ": " + edtMessage.getText().toString());
                    adapter.notifyDataSetChanged();
                    edtMessage.setText("");
                }
                else
                    Toast.makeText(ChatActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
