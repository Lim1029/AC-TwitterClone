package com.mingkang.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AllUserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private ArrayList<String> allUserList;
    private ArrayAdapter<String> allUserAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshPull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        setTitle("Welcome "+ ParseUser.getCurrentUser().getUsername());

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar2);
        refreshPull = findViewById(R.id.refreshPull);



        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        refreshPull.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveUsername();
                refreshPull.setRefreshing(false);
            }
        });

        retrieveUsername();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_log_out,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemLogOut:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null) {
                            startActivity(new Intent(AllUserActivity.this, LogInActivity.class));
                            Toast.makeText(AllUserActivity.this, "Log out successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                            Toast.makeText(AllUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.itemTweet:
                startActivity(new Intent(AllUserActivity.this, SendTweetActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView = (CheckedTextView) view;
        if(checkedTextView.isChecked()) {
            Toast.makeText(AllUserActivity.this, allUserList.get(position).toString() + " is followed!", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().add("followedBy", allUserList.get(position));
        }
        else {
            Toast.makeText(AllUserActivity.this, allUserList.get(position).toString() + " is unfollowed!", Toast.LENGTH_SHORT).show();
            ParseUser.getCurrentUser().getList("followedBy").remove(allUserList.get(position));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("followedBy");
            ParseUser.getCurrentUser().remove("followedBy");
            ParseUser.getCurrentUser().put("followedBy",currentUserFanOfList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                    Toast.makeText(AllUserActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(AllUserActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveUsername(){
        allUserList = new ArrayList();
        allUserAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, allUserList);
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseUser> usernameQuery = ParseUser.getQuery();
        usernameQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        usernameQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseUser username : objects)
                        allUserList.add(username.getUsername());
                    listView.setAdapter(allUserAdapter);
                    for(String username : allUserList){
                        if(ParseUser.getCurrentUser().getList("followedBy")!=null)
                            if (ParseUser.getCurrentUser().getList("followedBy").contains(username))
                                listView.setItemChecked(allUserList.indexOf(username), true);
                            else
                                listView.setItemChecked(allUserList.indexOf(username), false);
                    }
                } else {
                    Toast.makeText(AllUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AllUserActivity.this,ChatActivity2.class);
        intent.putExtra("receiverUsername",allUserList.get(position));
        startActivity(intent);
        return false;
    }
}
