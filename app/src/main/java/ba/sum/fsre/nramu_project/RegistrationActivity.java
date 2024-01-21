package ba.sum.fsre.nramu_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, signupName, signupLastName, signupPhoneNumber;
    private Button signupButton;
    private TextView loginRedirectText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupName = findViewById(R.id.signup_name);
        signupLastName = findViewById(R.id.signup_lastname);
        signupPhoneNumber = findViewById(R.id.signup_phonenumber);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();
                String name = signupName.getText().toString().trim();
                String lastName = signupLastName.getText().toString().trim();
                String phoneNumber = signupPhoneNumber.getText().toString().trim();

                if (user.isEmpty()){
                    signupEmail.setError("Email ne smije biti prazan.");
                }
                if(pass.isEmpty()){
                    signupPassword.setError("Lozinka ne smije biti prazna.");
                } else{
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                saveAdditionalUserInfo(name, lastName, phoneNumber);

                                Toast.makeText(RegistrationActivity.this, "Registracija uspješna!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                            } else{
                                Toast.makeText(RegistrationActivity.this, "Registracija nije uspjela: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    private void saveAdditionalUserInfo(String name, String lastName, String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String userId = auth.getCurrentUser().getUid();

        DatabaseReference usersRef = database.getReference("users");

        usersRef.child(userId).child("name").setValue(name);
        usersRef.child(userId).child("lastName").setValue(lastName);
        usersRef.child(userId).child("phoneNumber").setValue(phoneNumber);
    }
}