package com.example.mynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.activity.LoginActivity;
import com.example.mynotes.activity.RegisterActivity;
import com.example.mynotes.adapter.ListRecyclerViewAdapter;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.ActivityMainBinding;
import com.example.mynotes.fragment.AddNewNoteFragment;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.utils.SharedPreference;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private RecyclerView listRecyclerView;
    private ListRecyclerViewAdapter adapter;
    private AddNewNoteFragment addNewNoteFragment;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FragmentTransaction transaction;
    private MyDatabase myDatabase;
    private final ExecutorService service = Executors.newSingleThreadExecutor();


    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!SharedPreference.getIsLogin(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
            return;
        }
        addNewNoteFragment = AddNewNoteFragment.newInstance();
        name = SharedPreference.getUserName(getApplicationContext());
        if (name.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return;
        }
        myDatabase = MyDatabase.getInstance(getApplicationContext());
        listRecyclerView = binding.recyclerView;
        listRecyclerView.setHasFixedSize(true);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setToolbarTitle(name);
        binding.addNewNoteFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewNote();
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        SharedPreference.setIsLogin(getApplicationContext(), false);
        SharedPreference.setUserEmail(getApplicationContext(), "");
        SharedPreference.setUserName(getApplicationContext(), "");

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void addNewNote() {
        if (addNewNoteFragment != null) {
            binding.userName.setText(getString(R.string.add_new_notes));
            changeFragment(addNewNoteFragment);
        }
    }

    public void changeFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameLayout.getId(), fragment);
        transaction.commit();

    }

    private void setToolbarTitle(String name) {
        binding.userName.setText(String.format("%s %s %s",
                name.trim(),
                "'s",
                "Notes"));
        ;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(binding.frameLayout.getId());
        if (fragment != null) {
            getData();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            setToolbarTitle(name);
        } else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    void getData() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                String email = SharedPreference.getUserEmail(getApplicationContext());
                List<NotesModel> notesModels = myDatabase.getListDao().noteList(email);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(notesModels);
                    }
                });
            }
        });

    }

    void updateUI(List<NotesModel> notesModels) {
        adapter = new ListRecyclerViewAdapter(notesModels);
        listRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}