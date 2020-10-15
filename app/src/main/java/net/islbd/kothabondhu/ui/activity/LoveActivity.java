package net.islbd.kothabondhu.ui.activity;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import net.islbd.kothabondhu.R;
import net.islbd.kothabondhu.ui.adapter.VideoRecyclerViewAdapter;

public class LoveActivity extends AppCompatActivity {
    private RecyclerView videoRecyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        initializeWidgets();
        initializeData();
        eventListeners();
    }

    private void initializeWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Love Tips");
        videoRecyclerView = findViewById(R.id.love_activity_video_recyclerView);
    }

    private void initializeData() {
        context = this;
        createList();
    }

    private void eventListeners() {

    }

    private void createList() {
        VideoRecyclerViewAdapter videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(context);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoRecyclerView.setLayoutManager(layoutManager);
        videoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        videoRecyclerView.setAdapter(videoRecyclerViewAdapter);
        videoRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
