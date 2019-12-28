package com.example.myapplication.ui.weather_hist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Note;
import com.example.myapplication.NoteAdapter;
import com.example.myapplication.R;
import com.example.myapplication.db.DataSource;

import java.sql.SQLException;

public class WeatherHistFragment extends Fragment {

    private WeatherHistViewModel weatherHistViewModel;
    private NoteAdapter adapter;
    private DataSource dataSource;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        weatherHistViewModel =
                ViewModelProviders.of(this).get(WeatherHistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather_hist, container, false);
        final TextView textView = root.findViewById(R.id.text_weather_hist);
        weatherHistViewModel.getText().observe(this, s -> textView.setText(s));

        RecyclerView recyclerView = root.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        dataSource = new DataSource(root.getContext());
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adapter = new NoteAdapter(dataSource.getReader());
        adapter.setOnMenuItemClickListener(new NoteAdapter.OnMenuItemClickListener() {
            @Override
            public void onItemDeleteClick(Note note) {
                delete(note);
            }

            @Override
            public void onItemDeleteAllClick(Note note) {
                deleteAll();
            }
        });
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void delete(Note note) {
        dataSource.delete(note);
        refreshData();
    }

    private void deleteAll() {
        dataSource.deleteAll();
        refreshData();
    }

    public void refreshData() {
        dataSource.getReader().refresh();
        adapter.notifyDataSetChanged();
    }
}