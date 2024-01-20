package ba.sum.fsre.nramu_project.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.adapter.PostAdapter;
import ba.sum.fsre.nramu_project.Model.Post;


public class PostsFragment extends Fragment {

    private FirebaseDatabase postsDatabase;
    private PostAdapter postAdapter;
    private RecyclerView postRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        postsDatabase = FirebaseDatabase.getInstance();
        postRecyclerView = view.findViewById(R.id.postListView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(
                postsDatabase.getReference("posts"),
                Post.class
        ).build();

        postAdapter = new PostAdapter(options, this);
        postRecyclerView.setAdapter(postAdapter);

        // Add an OnClickListener to the filterIcon
        ImageView filterIcon = view.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortingOptionsDialog(requireContext());
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        postAdapter.stopListening();
    }

    public void showSortingOptionsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View sortingOptionsView = getLayoutInflater().inflate(R.layout.filter_dialog, null);

        RadioGroup sortingOptionsGroup = sortingOptionsView.findViewById(R.id.sortingOptionsGroup);
        Button applyButton = sortingOptionsView.findViewById(R.id.applyButton);

        builder.setView(sortingOptionsView);

        AlertDialog dialog = builder.create();
        dialog.show();

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedRadioButtonId = sortingOptionsGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId == R.id.radioOldest) {
                    postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                if (selectedRadioButtonId == R.id.radioNewest) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    postRecyclerView.setLayoutManager(layoutManager);
                }

                dialog.dismiss();
            }
        });
    }
}
