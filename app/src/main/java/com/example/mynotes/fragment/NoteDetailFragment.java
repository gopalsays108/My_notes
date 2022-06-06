package com.example.mynotes.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynotes.R;
import com.example.mynotes.adapter.PhotoRecyclerViewAdapter;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.FragmentAddNewNoteBinding;
import com.example.mynotes.databinding.FragmentNoteDetailBinding;
import com.example.mynotes.model.ImageModel;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteDetailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private NotesModel notesModel;
    private FragmentNoteDetailBinding binding;
    private RecyclerView recyclerView;
    private MyDatabase myDatabase;
    private PhotoRecyclerViewAdapter adapter;
    private List<Uri> imageUri;
    private Context context;
    private static final String TAG = NoteDetailFragment.class.getSimpleName();

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    public static NoteDetailFragment newInstance(NotesModel notesModel) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, notesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notesModel = (NotesModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.noteTitle.setText(notesModel.getTitle());
        binding.noteDescription.setText(notesModel.getDescription());
        recyclerView = binding.imageRv;
        imageUri = new ArrayList<>();
        if (getContext() != null)
            myDatabase = MyDatabase.getInstance(getContext().getApplicationContext());

        setUpRecyclerView();

        return view;
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.relativeLayout2.setVisibility(View.GONE);
        getImageFromDB();
    }

    private void getImageFromDB() {
        String email = SharedPreference.getUserEmail(context);
        long id = notesModel.getId();
        Log.i(TAG, "loadImageIntoRecyclerView: " + email + " " + id);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allImage = myDatabase.getImageDao().getAllImage(email, id);
                    List<Uri> im = new ArrayList<>();

                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (String u : allImage){
                                    im.add(Uri.parse(u));
                                    loadImageIntoRecyclerView(im);
                                }
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadImageIntoRecyclerView(List<Uri> allImage) {
        if (!allImage.isEmpty()) {
            binding.relativeLayout2.setVisibility(View.VISIBLE);
            adapter = new PhotoRecyclerViewAdapter(allImage);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            binding.relativeLayout2.setVisibility(View.GONE);
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