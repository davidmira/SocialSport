package com.david.socialsport.Pantallas;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.david.socialsport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by david on 26/06/2017.
 */

public class RestablecerContrasena extends Activity {
    EditText email;
    Button restablecer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restablecer_contrasena);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        email = (EditText) findViewById(R.id.restablecer_texto_email);
        restablecer = (Button) findViewById(R.id.restablecer_boton);

        restablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                final String emailAddress = email.getText().toString();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(RestablecerContrasena.this);

                                    dialog.setMessage(getString(R.string.email_enviado)+" "+emailAddress+" "+getString(R.string.revise_bandeja))
                                            .setNeutralButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                }
                            }
                        });
            }
        });
    }
}
