/**
 * Copyright 2016 Jeffrey Sibbold
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jsibbold.zoomage.example;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jsibbold.zoomage.ZoomageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ZoomageView demoView;
    private ImageView photo;
    private View optionsView;
    private AlertDialog optionsDialog;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoView = (ZoomageView) findViewById(R.id.demoView);
        Button button = (Button) findViewById(R.id.button);
        Button reset = (Button) findViewById(R.id.resetButton);
        container = (RelativeLayout) findViewById(R.id.container);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoView.resetMatrix();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoView.setImageResource(R.drawable.zoom_demo);
                demoView.scale(3f);
            }
        });
        prepareOptions();
    }

    private void prepareOptions() {
        optionsView = getLayoutInflater().inflate(R.layout.zoomage_options, null);
        setSwitch(R.id.zoomable, demoView.isZoomable());
        setSwitch(R.id.translatable, demoView.isTranslatable());
        setSwitch(R.id.animateOnReset, demoView.getAnimateOnReset());
        setSwitch(R.id.autoCenter, demoView.getAutoCenter());
        setSwitch(R.id.restrictBounds, demoView.getRestrictBounds());
        optionsView.findViewById(R.id.reset).setOnClickListener(this);
        optionsView.findViewById(R.id.autoReset).setOnClickListener(this);

        optionsDialog = new AlertDialog.Builder(this).setTitle("Zoomage Options")
                .setView(optionsView)
                .setPositiveButton("Close", null)
                .create();
    }

    private void setSwitch(int id, boolean state) {
        final SwitchCompat switchView = (SwitchCompat) optionsView.findViewById(id);
        switchView.setOnCheckedChangeListener(this);
        switchView.setChecked(state);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (!optionsDialog.isShowing()) {
            optionsDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.zoomable:
                demoView.setZoomable(isChecked);
                break;
            case R.id.translatable:
                demoView.setTranslatable(isChecked);
                break;
            case R.id.restrictBounds:
                demoView.setRestrictBounds(isChecked);
                break;
            case R.id.animateOnReset:
                demoView.setAnimateOnReset(isChecked);
                break;
            case R.id.autoCenter:
                demoView.setAutoCenter(isChecked);
                break;
        }
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.reset) {
            demoView.reset();
        } else {
            showResetOptions();
        }
    }

    private void showResetOptions() {
        CharSequence[] options = new CharSequence[]{"Under", "Over", "Always", "Never"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                demoView.setAutoResetMode(which);
            }
        });

        builder.create().show();
    }
}
