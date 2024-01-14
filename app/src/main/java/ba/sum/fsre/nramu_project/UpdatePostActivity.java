package ba.sum.fsre.nramu_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.UUID;

import ba.sum.fsre.nramu_project.Model.Post;

public class UpdatePostActivity extends AppCompatActivity {

    private String postId;
    private String title, breed, author, phone, description, picture;

    FirebaseStorage postsDatabase = FirebaseStorage.getInstance();
    StorageReference postsDatabaseRef;
    Uri pathToFile;
    private static final int IMAGE_PICKING_REQ_NUM = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);
        this.postsDatabase = FirebaseStorage.getInstance();
        this.postsDatabaseRef = this.postsDatabase.getReference();


        // Get post details from Intent
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra("postId");
            title = intent.getStringExtra("title");
            breed = intent.getStringExtra("breed");
            author = intent.getStringExtra("author");
            phone = intent.getStringExtra("phone");
            description = intent.getStringExtra("description");
            picture = intent.getStringExtra("picture");
        }


        updateUIWithPostDetails();

        Button selectImageButton = findViewById(R.id.updateButtonUploadImage);

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
        Button updateButton = findViewById(R.id.updateSubmitPostButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditTexts
                String title = ((EditText) findViewById(R.id.updateTitleEditText)).getText().toString();
                String breed = ((EditText) findViewById(R.id.updateBreedEditText)).getText().toString();
                String phoneNumber = ((EditText) findViewById(R.id.updatePhoneEditText)).getText().toString();
                String description = ((EditText) findViewById(R.id.updateDescriptionEditText)).getText().toString();

                // Validate Inputs
                if (!validateInputs(title, breed, author, phoneNumber, description)) {
                    Toast.makeText(UpdatePostActivity.this, "Unesite sva polja", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add movie to database
                updatePostInDatabase(postId,breed, description, phoneNumber, picture, title);
            }
        });







    }

    private void updateUIWithPostDetails() {
        // Update your UI elements with the details from the Intent
        // For example, set text to TextViews, load images, etc.
        TextView titleTextView = findViewById(R.id.updateTitleEditText);
        TextView breedTextView = findViewById(R.id.updateBreedEditText);
        TextView authorTextView = findViewById(R.id.updateAuthorEditText);
        TextView phoneTextView = findViewById(R.id.updatePhoneEditText);
        TextView descriptionTextView = findViewById(R.id.updateDescriptionEditText);

        titleTextView.setText(title);
        breedTextView.setText(breed);
        authorTextView.setText(author);
        phoneTextView.setText(phone);
        descriptionTextView.setText(description);
    }

    private boolean validateInputs(String title, String breed, String author, String phoneNumber, String description) {
        if (title.isEmpty() || breed.isEmpty() || phoneNumber.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Molimo popunite sva polja!", Toast.LENGTH_SHORT).show();

            return false;
        }
        return true;
    }
    private void updatePostInDatabase(String postId, String breed, String description, String phone, String picture, String title) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postsRef = database.getReference("posts");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        // Assuming you have the postId of the post you want to update
        if (postId != null) {
            Post updatedPost = new Post(currentUser.getEmail(), breed, description, phone, picture, title);

            // Use the postId to update the values of the existing post
            postsRef.child(postId).setValue(updatedPost)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UpdatePostActivity.this, "Objava uspješno ažurirana!", Toast.LENGTH_SHORT).show();

                        // Start MainActivity
                        Intent intent = new Intent(UpdatePostActivity.this, MainActivity.class);
                        startActivity(intent);

                        // Finish the current activity if needed
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdatePostActivity.this, "Greška pri ažuriranju objave: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // Handle the case where postId is null (e.g., show an error message)
            Toast.makeText(UpdatePostActivity.this, "Greška: Nedostaje identifikator objave.", Toast.LENGTH_SHORT).show();
        }
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
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Učitavanje slike u tijeku!");
            progressDialog.show();

            StorageReference ref = postsDatabaseRef.child("images/" + UUID.randomUUID().toString());
            ref.putFile(pathToFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdatePostActivity.this, "Slika uspješno učitana.", Toast.LENGTH_LONG).show();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    picture = task.getResult().toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(this, "Greška pri učitavanju slike: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }



}