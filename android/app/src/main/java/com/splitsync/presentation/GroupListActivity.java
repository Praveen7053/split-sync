package com.splitsync.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.splitsync.R;
import com.splitsync.domain.model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends SplitSyncActivity {

    private final List<Group> groups = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        setTitle(R.string.groups_title);

        ListView listGroups = findViewById(R.id.listGroups);
        TextView textEmpty = findViewById(R.id.textEmpty);
        MaterialButton buttonCreateGroup = findViewById(R.id.buttonCreateGroup);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listGroups.setAdapter(adapter);

        listGroups.setOnItemClickListener((parent, view, position, id) -> {
            Group group = groups.get(position);
            Intent intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra(GroupDetailActivity.EXTRA_GROUP_ID, group.groupId().value());
            startActivity(intent);
        });

        buttonCreateGroup.setOnClickListener(v -> startActivity(new Intent(this, CreateGroupActivity.class)));

        // Keep empty-state reference for refresh.
        listGroups.setTag(textEmpty);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshGroups();
    }

    private void refreshGroups() {
        TextView textEmpty = findViewById(R.id.textEmpty);
        AppExecutors.io(() -> {
            try {
                List<Group> loaded = UiHelpers.services(this).groups().listGroups();
                runOnUiThread(() -> {
                    groups.clear();
                    groups.addAll(loaded);
                    adapter.clear();
                    for (Group group : loaded) {
                        adapter.add(group.name() + " · " + group.currency().value());
                    }
                    adapter.notifyDataSetChanged();
                    textEmpty.setVisibility(loaded.isEmpty() ? View.VISIBLE : View.GONE);
                });
            } catch (Exception error) {
                runOnUiThread(() -> UiHelpers.showError(this, error));
            }
        });
    }
}
