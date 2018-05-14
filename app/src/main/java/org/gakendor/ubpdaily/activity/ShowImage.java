package org.gakendor.ubpdaily.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import org.gakendor.ubpdaily.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowImage extends AppCompatActivity {

    @BindView(R.id.iv_zoom)
    ZoomageView ivZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        String link = getIntent().getStringExtra("link");

        Glide.with(this).load(link).into(ivZoom);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
