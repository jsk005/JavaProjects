package com.link2me.android.notesample.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.link2me.android.notesample.R;
import com.link2me.android.notesample.databinding.ActivityMainBinding;
import com.link2me.android.notesample.model.Note;
import com.link2me.android.notesample.view.adapter.NoteViewAdapter;
import com.link2me.android.notesample.viewmodel.MyFactory;
import com.link2me.android.notesample.viewmodel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ActivityMainBinding binding;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel viewModel;

    private RecyclerView mRecyclerView;
    //private NoteAdapter mAdapter;
    private NoteViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mContext = MainActivity.this;

        initView();
    }

    private void initView() {
        // 동기식 데이터 처리에서는 순서가 중요함.
        setToolbar();
        buildRecyclerView();
        initViewModel();
        setButtons();
    }

    private void setToolbar() {
        // activity에 툴바를 추가하려면 우선 기본 앱바(AppBar)부터 비활성화해야 한다.
        // 기존 actionBar는 theme 에서 NoActionBar로 설정하여 제거하고 툴바를 생성한다.
        setSupportActionBar(binding.mainToolbar);
        // Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_24);

        // 타이틀명 변경
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TODO LIST");
            binding.mainToolbar.setTitleTextColor(Color.WHITE);
        }

    }

    private void initViewModel() {
        // viewModel init
        //viewModel = new ViewModelProvider(this, new MyFactory(getApplication())).get(NoteViewModel.class);
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // viewModel observe
        viewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //mAdapter.submitList(notes);
                mAdapter.setNotes(notes);
                for(Note note:notes){
                    Log.e(TAG,note.getId() + ", " + note.getTitle() + ", " + note.getDescription() + ", " + note.getPriority());
                }
                Log.e(TAG, "note_db size : " + notes.size());
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"onNewIntent");
    }

    private void buildRecyclerView() {
        mRecyclerView = binding.recyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);

//        mAdapter = new NoteAdapter();
        mAdapter = new NoteViewAdapter();

        DividerItemDecoration decoration = new DividerItemDecoration(mContext, manager.getOrientation());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setButtons() {
        // 안드로이드 디자인 지원 라이브러리에는 FloatingActionButton 클래스가 제공된다.
        FloatingActionButton buttonAddNote = binding.buttonAddNote;
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.delete(mAdapter.getNoteAt(viewHolder.getBindingAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerView);

        mAdapter.setOnItemClickListener(new NoteViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            viewModel.insert(note);

            Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            viewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * 메뉴는 프로젝트의 res/menu 폴더에 저장되는 XML 리소스 형식으로 정의할 수 있다.
     * 메뉴는 루트 노드에서 menu 태그와 일련의 item 태그로 구성된다.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 부모 activity 나 fragment 와 메뉴를 연관시켜야 하므로 항상 super.onCreateOptionsMenu를 호출해야 한다.
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        // 검색 메뉴 등록
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 문자열 입력을 완료했을 때 문자열 반환
                if(query != null) {
                    searchDatabase(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 문자열이 변할 때마다 바로바로 문자열 반환
                if(newText != null) {
                    searchDatabase(newText);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                viewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchDatabase(String search){
        String searchQuery = "%" + search + "%";

        viewModel.searchDatabase(searchQuery).observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //mAdapter.submitList(notes);
                mAdapter.setNotes(notes);
            }
        });
    }

}