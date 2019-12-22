package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.DataReader;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final DataReader reader;
    private OnMenuItemClickListener itemClickListener;

    public NoteAdapter(DataReader reader) {
        this.reader = reader;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
        holder.bind(reader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return reader.getCount();
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.itemClickListener = onMenuItemClickListener;
    }

    public interface OnMenuItemClickListener {
        void onItemDeleteClick(Note note);

        void onItemDeleteAllClick(Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleNote;
        private Note note;

        public ViewHolder(View itemView) {
            super(itemView);
            titleNote = itemView.findViewById(R.id.itemWeatherText);
            titleNote.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    showPopupMenu(titleNote);
                }
            });
        }

        public void bind(Note note) {
            this.note = note;
            titleNote.setText(note.getCity() + ", Temperature: " + note.getTemperature() + ", Humidity: " + note.getHumidity() + ", Wind speed: " + note.getWind());
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.context_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        itemClickListener.onItemDeleteClick(note);
                        return true;
                    case R.id.menu_delete_all:
                        itemClickListener.onItemDeleteAllClick(note);
                        return true;
                }
                return false;
            });
            popup.show();
        }
    }
}
