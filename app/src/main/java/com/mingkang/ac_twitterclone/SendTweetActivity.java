package com.mingkang.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity {

    private Button btnTweet;
    private EditText edtTweet;
    private ListView listView;
    private SwipeRefreshLayout pullToRefresh;
    private ProgressBar progressBarTweet;
//    private ArrayList<String> tweetArray;
//    private ArrayList<String> usernameArray;
//    private ListAdapter tweetListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        btnTweet = findViewById(R.id.btnTweet);
        edtTweet = findViewById(R.id.edtTweet);
        listView = findViewById(R.id.listView);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        progressBarTweet = findViewById(R.id.progressBarTweet);

            retrieveTweet();

//        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
//        final SimpleAdapter adapter = new SimpleAdapter(this,tweetList,android.R.layout.simple_list_item_2,new String[]{"username","tweet"},new int[]{android.R.id.text1,android.R.id.text2});

//        ParseQuery<ParseObject> othersTweetQuery = ParseQuery.getQuery("tweet");
//        try {
//            othersTweetQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("followedBy"));
//            othersTweetQuery.orderByDescending("createdAt");
//            othersTweetQuery.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> objects, ParseException e) {
//                    if (e == null && objects.size() > 0) {
//                        for (ParseObject tweet : objects) {
//                            HashMap<String, String> userTweet = new HashMap<>();
//                            userTweet.put("username", tweet.getString("username"));
//                            userTweet.put("tweet", tweet.getString("tweet"));
//                            tweetList.add(userTweet);
//                        }
//                        listView.setAdapter(adapter);
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject userTweet = new ParseObject("tweet");
                userTweet.put("username",ParseUser.getCurrentUser().getUsername());
                userTweet.put("tweet",edtTweet.getText().toString());
                userTweet.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                            Toast.makeText(SendTweetActivity.this,"Tweeted!",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SendTweetActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveTweet(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    public void retrieveTweet(){
        progressBarTweet.setVisibility(View.VISIBLE);
        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(this,tweetList,android.R.layout.simple_list_item_2,new String[]{"username","tweet"},new int[]{android.R.id.text1,android.R.id.text2});
        ParseQuery<ParseObject> othersTweetQuery = ParseQuery.getQuery("tweet");
        othersTweetQuery.whereContainedIn("username", ParseUser.getCurrentUser().getList("followedBy"));
        othersTweetQuery.orderByDescending("createdAt");
        othersTweetQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size()>0) {
                    for (ParseObject tweet : objects) {
                        HashMap<String, String> userTweet = new HashMap<>();
                        userTweet.put("username", tweet.getString("username"));
                        userTweet.put("tweet", tweet.getString("tweet"));
                        tweetList.add(userTweet);
                    }
                    listView.setAdapter(adapter);
                } else
                    Toast.makeText(SendTweetActivity.this, "No tweet found", Toast.LENGTH_SHORT).show();
                progressBarTweet.setVisibility(View.GONE);
            }
        });
    }
}
