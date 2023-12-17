package ba.sum.fsre.nramu_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fsre.nramu_project.Model.CourseRVModal;

public class AddCourseActivity extends AppCompatActivity {

    private Button addBtn;
    private TextInputEditText NameEdt, DescEdt, AutorIDEdt, AutorNameEdt, BrTelefonaEdt, slikaEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        addBtn = findViewById(R.id.idBtnAdd);
        NameEdt = findViewById(R.id.ImeTxt);
        DescEdt = findViewById(R.id.OpisTxt);
        AutorIDEdt = findViewById(R.id.AutorIDTxt);
        AutorNameEdt = findViewById(R.id.AutorNameTxt);
        BrTelefonaEdt = findViewById(R.id.BrTelefonaTxt);
        slikaEdt = findViewById(R.id.slikaTxt);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Product");
        //Toast.makeText(AddCourseActivity.this,"dosao dovdje",Toast.LENGTH_SHORT).show();

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                String Name = NameEdt.getText().toString();
                String Desc = DescEdt.getText().toString();
                String AutorID = AutorIDEdt.getText().toString();
                String AutorName = AutorNameEdt.getText().toString();
                String BrTelefona = BrTelefonaEdt.getText().toString();
                String slika = slikaEdt.getText().toString();
                Toast.makeText(AddCourseActivity.this,"Dosao dovdje",Toast.LENGTH_SHORT).show();
                productID = AutorID;
                CourseRVModal courseRVModal = new CourseRVModal(productID, Name, Desc, AutorID, AutorName, BrTelefona, slika);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child(productID).setValue(courseRVModal);
                        Toast.makeText(AddCourseActivity.this,"Ubaceno",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCourseActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddCourseActivity.this,"nije uspjelo",Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
    }

}
