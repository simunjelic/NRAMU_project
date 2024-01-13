package ba.sum.fsre.nramu_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.fragment.PostsFragment;
import ba.sum.fsre.nramu_project.model.Post;

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
        }

    }
}