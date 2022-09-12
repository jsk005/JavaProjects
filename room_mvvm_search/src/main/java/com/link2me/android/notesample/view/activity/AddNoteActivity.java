package com.link2me.android.notesample.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.link2me.android.notesample.R;
import com.link2me.android.notesample.databinding.ActivityAddNoteBinding;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "EXTRA_PRIORITY";

    private ActivityAddNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_note);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // 기존 actionBar는 theme 에서 NoActionBar로 설정하여 제거하고 툴바를 생성한다.
        setSupportActionBar(binding.noteToolbar);
        // manifest.xml 에서 android:parentActivityName=".view.activity.MainActivity" 추가하고 아래 2줄 추가한다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0000")));

        binding.numberPickerPriority.setMinValue(1);
        binding.numberPickerPriority.setMaxValue(10);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            getSupportActionBar().setTitle("Edit Note");
            binding.noteToolbar.setTitleTextColor(Color.WHITE);
            binding.editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            binding.editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            binding.numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            getSupportActionBar().setTitle("Add Note");
            binding.noteToolbar.setTitleTextColor(Color.WHITE);
        }
    }

    private void saveNote() {
        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        int priority = binding.numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}