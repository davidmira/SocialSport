package com.david.socialsport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.david.socialsport.Pantallas.Login;
//import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by david on 26/03/2017.
 */

public class Pruebas extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainwelcome);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {

            TextView txtDisplayName = (TextView) findViewById(R.id.txtUsername);
            txtDisplayName.setText(firebaseUser.getDisplayName());

            TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
            txtEmail.setText(firebaseUser.getEmail());

            Uri imageUri = firebaseUser.getPhotoUrl();
            //ImageView imageAvatar = (ImageView) findViewById(R.id.imgAvatar);
            //Glide.with(this).load(imageUri).into(imageAvatar);

            TextView txtUid = (TextView) findViewById(R.id.txtUid);
            txtUid.setText(firebaseUser.getUid());

            Toast.makeText(this, "Provider ID:" + firebaseUser.getProviderId(), Toast.LENGTH_SHORT).show();
            System.out.println("sfasfasf");

        }
        Button buttonSignOut = (Button) findViewById(R.id.signOutButton);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Pruebas.this, Login.class));
        finish();

    }
}
