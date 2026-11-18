package com.splitsync.presentation;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Base activity that keeps content below the system/action bars.
 * Target SDK 35+ enables edge-to-edge by default, which was covering the first form fields
 * (e.g. Group name on Create Group).
 */
public abstract class SplitSyncActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        applyContentInsets();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        applyContentInsets();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        applyContentInsets();
    }

    private void applyContentInsets() {
        View content = findViewById(android.R.id.content);
        if (content == null) {
            return;
        }
        ViewCompat.setOnApplyWindowInsetsListener(content, (v, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            int top = bars.top;
            if (getSupportActionBar() != null) {
                top += actionBarSizePx();
            }
            v.setPadding(bars.left, top, bars.right, bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(content);
    }

    private int actionBarSizePx() {
        TypedValue value = new TypedValue();
        if (getTheme().resolveAttribute(androidx.appcompat.R.attr.actionBarSize, value, true)) {
            return TypedValue.complexToDimensionPixelSize(value.data, getResources().getDisplayMetrics());
        }
        return 0;
    }
}
