package ba.sum.fsre.nramu_project.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.model.Post;


public class AddPostFragment extends Fragment {

    FirebaseStorage postsDatabase = FirebaseStorage.getInstance();
    StorageReference postsDatabaseRef;
    String animalImage;
    Uri pathToFile;
    private static final int IMAGE_PICKING_REQ_NUM = 22;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.postsDatabase = FirebaseStorage.getInstance();
        this.postsDatabaseRef = this.postsDatabase.getReference();
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        Button selectImageButton = view.findViewById(R.id.buttonUploadImage);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(i, "Odaberite sliku životinje."), 22
                );
            }
        });

        Button insertMovieButton = view.findViewById(R.id.submitPostButton);
        insertMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditTexts
                String title = ((EditText) view.findViewById(R.id.titleEditText)).getText().toString();
                String breed = ((EditText) view.findViewById(R.id.breedEditText)).getText().toString();
                String author = ((EditText) view.findViewById(R.id.authorEditText)).getText().toString();
                String phoneNumber = ((EditText) view.findViewById(R.id.phoneEditText)).getText().toString();
                String description = ((EditText) view.findViewById(R.id.descriptionEditText)).getText().toString();

                // Validate Inputs
                if (!validateInputs(title, breed, author, phoneNumber, description) || animalImage == null) {
                    Toast.makeText(getContext(), "Please upload an image and fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add movie to database
                addPostToDatabase(author, breed, description, phoneNumber, animalImage, title);
                resetForm(view);
            }
        });


        return view;
    }

    private boolean validateInputs(String title, String breed, String author, String phoneNumber, String description) {
        if (title.isEmpty() || breed.isEmpty() || phoneNumber.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Molimo popunite sva polja!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetForm(View view) {
        ((EditText) view.findViewById(R.id.titleEditText)).setText("");
        ((EditText) view.findViewById(R.id.breedEditText)).setText("");
        ((EditText) view.findViewById(R.id.authorEditText)).setText("");
        ((EditText) view.findViewById(R.id.phoneEditText)).setText("");
        ((EditText) view.findViewById(R.id.descriptionEditText)).setText("");
        animalImage = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICKING_REQ_NUM && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            pathToFile = data.getData();
            uploadImage(); // Call uploadImage method here
        }
    }

    private void uploadImage() {
        if (pathToFile != null) {
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Učitavanje slike u tijeku!");
            progressDialog.show();

            StorageReference ref = postsDatabaseRef.child("images/" + UUID.randomUUID().toString());
            ref.putFile(pathToFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Slika uspješno učitana.", Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    animalImage = task.getResult().toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Greška pri učitavanju slike: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void addPostToDatabase(String author, String breed, String description, String phone, String picture, String title) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        String postId = postsRef.push().getKey();
        Post post = new Post(currentUser.getEmail(), breed, description, phone, picture, title);
        postsRef.child(postId).setValue(post)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Objava uspješno dodana!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Greška pri dodavanju objave: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}