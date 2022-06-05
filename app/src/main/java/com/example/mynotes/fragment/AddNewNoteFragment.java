package com.example.mynotes.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.FragmentAddNewNoteBinding;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.utils.SharedPreference;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddNewNoteFragment extends Fragment {
    private FragmentAddNewNoteBinding binding;
    private String title;
    private String description;
    private Context context;
    private MyDatabase myDatabase;
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private Toast myToast;
    private int totalNumberOfPhoto = 10;


    public AddNewNoteFragment() {
        // Required empty public constructor
    }

    public static AddNewNoteFragment newInstance() {
        return new AddNewNoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        myToast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);

        if (getContext() != null)
            myDatabase = MyDatabase.getInstance(getContext().getApplicationContext());

        binding.saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput();
            }
        });

        binding.noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 100) {
                    myToast.setText("Title cannot exceed more than 100 character");
                    myToast.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.noteDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1000) {
                    myToast.setText("Description cannot exceed more than 1000 character");
                    myToast.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        
        binding.addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
        return view;
    }

    private void addPhoto() {

    }

    private void validateInput() {
        title = binding.noteTitle.getText().toString();
        description = binding.noteDescription.getText().toString();

        if (title.length() < 5) {
            myToast.setText("Title length should be at least 5 character");
            myToast.show();
        } else if (description.length() < 10) {
            myToast.setText("Description length should be at least 100 character");
            myToast.show();
        } else {
            saveToDatabase();
        }
    }

    private void saveToDatabase() {
        if (getContext() != null) {
            String email = SharedPreference.getUserEmail(getContext());
            NotesModel notesModel = new NotesModel(email, title, description);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    myDatabase.getListDao().insertNote(notesModel);
                }
            });

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myToast.setText("Notes Saved");
                        myToast.show();
                    }
                });
                getActivity().onBackPressed();
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}