package com.example.mynotes.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynotes.activity.ImageFullScreenActivity;
import com.example.mynotes.adapter.PhotoRecyclerViewAdapter;
import com.example.mynotes.database.MyDatabase;
import com.example.mynotes.databinding.FragmentAddNewNoteBinding;
import com.example.mynotes.interfaces.RecyclerViewFullImageInterface;
import com.example.mynotes.model.ImageModel;
import com.example.mynotes.model.NotesModel;
import com.example.mynotes.utils.SharedPreference;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//todo image count
public class AddNewNoteFragment extends Fragment implements RecyclerViewFullImageInterface {

    private static final String ARG_PARAM1 = "param1";

    private FragmentAddNewNoteBinding binding;
    private String title;
    private String description;
    private Context context;
    private RecyclerView recyclerView;
    private PhotoRecyclerViewAdapter adapter;
    private MyDatabase myDatabase;
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private Toast myToast;
    private int totalNumberOfPhoto = 10;
    private ArrayList<Parcelable> parcelableArrayListExtra;
    private final String TAG = AddNewNoteFragment.class.getSimpleName();
    private List<Uri> list;
    private long noteId = -1;
    private String email;

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
        list = new ArrayList<>();
        email = SharedPreference.getUserEmail(context);
        if (getContext() != null)
            myDatabase = MyDatabase.getInstance(getContext().getApplicationContext());

        recyclerView = binding.imageRv;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.relativeLayout2.setVisibility(View.GONE);
        setUpListeners();
        return view;
    }

    private void setUpListeners() {
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
                if (checkCameraPermission()) {
                    if (totalNumberOfPhoto > 0)
                        addPhoto();
                    else
                        Toast.makeText(context, "Only 10 photos are allowed per note", Toast.LENGTH_SHORT).show();
                } else {
                    requestCameraPermission();
                }
            }
        });
    }

    private void requestCameraPermission() {
        String[] arr = new String[]{Manifest.permission.CAMERA};
        requestPermissionLauncher.launch(arr[0]);
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    addPhoto();
                } else {
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (getActivity() != null)
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestCameraPermission();
                                        }
                                    });
                        }
                }
            });

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        if (getActivity() != null)
            new AlertDialog.Builder(getActivity())
                    .setMessage("You need to allow camera permission")
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FishBun.FISHBUN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null)
                    parcelableArrayListExtra = data.getParcelableArrayListExtra(FishBun.INTENT_PATH);
                setImage();
            }
        }
    }

    private void setImage() {
        binding.relativeLayout2.setVisibility(View.VISIBLE);
        if (parcelableArrayListExtra != null && parcelableArrayListExtra.size() > 0) {
            for (Parcelable pr : parcelableArrayListExtra) {
                list.add(Uri.parse(pr.toString()));
            }
            totalNumberOfPhoto = totalNumberOfPhoto - parcelableArrayListExtra.size();
            adapter = new PhotoRecyclerViewAdapter(list,this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void saveImageToDatabase() {
        try {
            for (int i = 0; i < list.size(); i++) {
                ImageModel imageModel = new ImageModel(noteId, email, list.get(i).toString());
                resetValues();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i(TAG, "run: checking" + imageModel);
                            myDatabase.getImageDao().insert(imageModel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            getActivity().onBackPressed();
        } catch (Exception e) {
            Log.d(TAG, "saveImageToDatabase: " + e.getLocalizedMessage());
        }
    }

    private void addPhoto() {
        FishBun.with(AddNewNoteFragment.this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(totalNumberOfPhoto)
                .setActionBarColor(Color.parseColor("#FB503A"),
                        Color.parseColor("#F6412B"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .textOnNothingSelected("Nothing Selected")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .setActionBarTitle("Select Images")
                .setAllViewTitle("All")
                .setCamera(true)
                .hasCameraInPickerPage(true)
                .startAlbum();
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
                    try {
                        noteId = myDatabase.getListDao().insertNote(notesModel);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveImageToDatabase();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void resetValues() {
        totalNumberOfPhoto = 10;
        binding.noteTitle.setText("");
        binding.noteDescription.setText("");
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ImageFullScreenActivity.class);
        intent.putExtra("image_uri", list.get(position).toString());
        startActivity(intent);
    }
}