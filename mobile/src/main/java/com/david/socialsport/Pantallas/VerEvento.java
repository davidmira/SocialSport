package com.david.socialsport.Pantallas;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.socialsport.Objetos.Evento;
import com.david.socialsport.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by david on 16/05/2017.
 */

public class VerEvento extends AppCompatActivity implements View.OnClickListener {
    TextView deporteText;
    String eventoID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventoID = getIntent().getStringExtra("eventoId");

        System.out.println("Identificador Evento:  " + eventoID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_evento);


        deporteText = (TextView) findViewById(R.id.evento_ver_deporte);
        TextView localizacion = (TextView) findViewById(R.id.evento_localizacion);
        TextView ubicacionEvento = (TextView) findViewById(R.id.evento_ubicacion);
        TextView tipoLugar = (TextView) findViewById(R.id.evento_tipo_lugar);
        TextView precio = (TextView) findViewById(R.id.evento_precio);
        TextView fecha = (TextView) findViewById(R.id.evento_fecha);
        TextView hora = (TextView) findViewById(R.id.evento_hora);
        ImageView icono = (ImageView) findViewById(R.id.evento_icono);

/*        localizacion.setText(evento.getLocalizacion());
        ubicacionEvento.setText(evento.getUbicacionEvento());
        tipoLugar.setText(evento.getTipoLugar());
        precio.setText(evento.getPrecio().toString() + " €");
*/
       /* Date fechaHora = evento.getFecha_hora();
        fecha.setText(new SimpleDateFormat("dd/MM/yy").format(fechaHora));
        hora.setText(new SimpleDateFormat("HH:mm").format(fechaHora));*/
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Object>> t = new GenericTypeIndicator<Map<String, Object>>() {
                };

                Map<String, Object> eventosId = dataSnapshot.child("evento").child(eventoID).getValue(t);
                if (eventosId != null) {
                    String deporte = dataSnapshot.child("evento").child(eventoID).child("deporte").getValue(String.class);

                    deporteText.setText(deporte);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}

