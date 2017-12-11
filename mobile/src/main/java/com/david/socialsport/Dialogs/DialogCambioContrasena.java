package com.david.socialsport.Dialogs;

import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.david.socialsport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by david on 11/12/17.
 */

public class DialogCambioContrasena extends Activity{
    EditText contrasena;
    Button enviar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cambiar_contrasena);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        contrasena = (EditText) findViewById(R.id.cambiar_contrasena);
        enviar = (Button) findViewById(R.id.cambiar_contrasena_boton);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String newPassword = contrasena.getText().toString();
                System.out.println(newPassword);
                user.updatePassword(newPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("cambio1");
                                if (task.isSuccessful()) {
                                    System.out.println("cambio");
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(DialogCambioContrasena.this);
                                    dialog.setMessage(R.string.cambio_correcto)
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
