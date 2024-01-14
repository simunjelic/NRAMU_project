package ba.sum.fsre.nramu_project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ba.sum.fsre.nramu_project.LoginActivity;
import ba.sum.fsre.nramu_project.MainActivity;
import ba.sum.fsre.nramu_project.R;
import ba.sum.fsre.nramu_project.RegistrationActivity;

public class ProfileFragment extends Fragment {

    private TextView currentUserTextView;
    private EditText newPasswordEditText, currentPasswordEditText;
    private Button updatePasswordButton,logoutButton,deleteUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentUserTextView = view.findViewById(R.id.currentUserTextView);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        updatePasswordButton = view.findViewById(R.id.updatePasswordButton);
        logoutButton = view.findViewById(R.id.Logout);
        deleteUser = view.findViewById(R.id.deleteUser);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //Ovdje samo dohvacao ime
        String email = currentUser.getEmail();
        String username = email.split("@")[0];

        if (currentUser != null) {
            currentUserTextView.setText("Pozdrav " + username);
        }


        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurenUser();
            }
        });


        return view;
    }

    private void deleteCurenUser() {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if(user != null){
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "Izbrisan profil", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {
                    Toast.makeText(getContext(), "Profil nije izbrisan", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    }

    private void logoutUser() {

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(),"Odjava uspjesna", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class));

    }

    private void updatePassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String currentPassword = currentPasswordEditText.getText().toString().trim();

        if (!newPassword.isEmpty() && !currentPassword.isEmpty()) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassword);

                currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updatePassword(newPassword).addOnCompleteListener(passwordUpdateTask -> {
                            if (passwordUpdateTask.isSuccessful()) {
                                Toast.makeText(getContext(), "Uspjesno ste promjenili lozinka", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Neuspjesno promjenjena lozinka", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Ponovna provjera autentičnosti nije uspjela", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getContext(), "Unesite novu lozinku i svoju trenutnu lozinku", Toast.LENGTH_SHORT).show();
        }
    }
}
