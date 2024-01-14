package ba.sum.fsre.nramu_project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.UpdatePostActivity;
import ba.sum.fsre.nramu_project.fragment.PostsFragment;
import ba.sum.fsre.nramu_project.Model.Post;

public class PostAdapter extends FirebaseRecyclerAdapter<Post,PostAdapter.PostViewHolder> {

    Context ctx;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param postsFragment
     */
    public PostAdapter(@NonNull FirebaseRecyclerOptions<Post> options, PostsFragment postsFragment) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {

        if (model.author.contains("@")) {
            String authorEmail = model.author;
            model.author = authorEmail.substring(0, authorEmail.indexOf('@'));
        }
        holder.title.setText(model.title);
        holder.breed.setText(model.breed);
        holder.author.setText(model.author);
        holder.phone.setText(model.phone);
        holder.description.setText(model.description);
        Picasso.get().load(model.picture).into(holder.picture);

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.ctx = parent.getContext();
        View view = LayoutInflater.from(this.ctx).inflate(R.layout.post_item_list_view,parent,false);
        return new PostViewHolder(view);
    }

    Button btnUpdate, btnDelete;

    public class PostViewHolder extends RecyclerView.ViewHolder{
        ImageView picture;
        TextView title, breed, author, phone, description;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            this.picture = itemView.findViewById(R.id.postImage);
            this.title = itemView.findViewById(R.id.title);
            this.breed = itemView.findViewById(R.id.breed);
            this.author = itemView.findViewById(R.id.author);
            this.phone = itemView.findViewById(R.id.phone);
            this.description = itemView.findViewById(R.id.description);

            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle update button click
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the selected post
                        Post selectedPost = getItem(position);

                        // Open UpdatePostActivity and pass post details
                        Intent intent = new Intent(itemView.getContext(), UpdatePostActivity.class);
                        intent.putExtra("postId", getRef(position).getKey());
                        intent.putExtra("title", selectedPost.title);
                        intent.putExtra("breed", selectedPost.breed);
                        intent.putExtra("author", selectedPost.author);
                        intent.putExtra("phone", selectedPost.phone);
                        intent.putExtra("description", selectedPost.description);
                        intent.putExtra("picture", selectedPost.picture);

                        // Start the activity
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(btnUpdate.getContext());
                    builder.setTitle("Potvrda brisanja");
                    builder.setMessage("Jeste li sigurni da želite obrisati objavu?");

                    // Add the buttons
                    builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked Yes button, proceed with deletion
                            String postId = getRef(getAdapterPosition()).getKey();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference postsRef = database.getReference("posts");
                            postsRef.child(postId).removeValue();

                            Toast.makeText(btnUpdate.getContext(), "Objava uspješno obrisana!", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked No button, do nothing
                            dialog.dismiss();
                        }
                    });

                    // Create and show the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }
    }

    private void deletePost(Integer postition) {
        // Implement the logic to delete the post from the database
        // You can use the post ID or any other identifier to delete the specific post

        // For example:
        // String postId = getRef(getAdapterPosition()).getKey();
        // postsRef.child(postId).removeValue();

        // After deletion, you might want to navigate back or perform any other action
        // For simplicity, I'll just log a message here
        Log.d("PostAdapter", "Post deleted for position: " + postition);
    }


}
