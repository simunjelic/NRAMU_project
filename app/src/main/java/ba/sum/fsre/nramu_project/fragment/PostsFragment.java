package ba.sum.fsre.nramu_project.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.adapter.PostAdapter;
import ba.sum.fsre.nramu_project.Model.Post;


public class PostsFragment extends Fragment {

    FirebaseDatabase postsDatabase = FirebaseDatabase.getInstance();
    PostAdapter postAdapter;
    RecyclerView postRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        this.postRecyclerView = view.findViewById(R.id.postListView);
        this.postRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(
                this.postsDatabase.getReference("posts"),
                Post.class
        ).build();

        this.postAdapter = new PostAdapter(options,this);
        this.postRecyclerView.setAdapter(this.postAdapter);


        // Inflate the layout for this fragment
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        this.postAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.postAdapter.stopListening();
    }
}