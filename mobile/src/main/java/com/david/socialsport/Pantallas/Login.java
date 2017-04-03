package com.david.socialsport.Pantallas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.david.socialsport.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    /**
     * Código de solicitud utilizado para iniciar la Actividad de Inicio de Sesión de Google.
     */
    private static final int CS_INICIAR_SESION = 9001;

    /**
     * Objeto Cliente API de Google.
     */
    private GoogleApiClient clienteApiGoogle;

    // Variables que utiliza Firebase.
    /**
     * FirebaseAuth es el objeto que contiene el listener que escucha los cambios en la cuenta
     * y con el que se puede iniciar sesión con Google.
     */
    private FirebaseAuth autenticacionFirebase;
    /**
     * El listener escucha cambios en la cuenta. Cuando se inicia sesión de manera exitosa,
     * se ejecuta el método onAuthStateChanged y se obtiene un FirebaseUser != null. Si el FirebaseUser
     * es null, quiere decir que no se inició sesión.
     */
    private FirebaseAuth.AuthStateListener listenerAutenticacion;

    // Variables de Facebook
    /**
     * Objeto de Facebook que se utiliza para manejar las llamadas desde la actividad.
     */
    private CallbackManager manejadorDeLlamadasFacebook;

    /**
     * SignInButton es el botón de Google para iniciar sesión.
     */
    private SignInButton botonIniciarSesionGoogle;


    /**
     * Botón de inicio de sesión de Facebook
     */
    private LoginButton botonIniciarSesionFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarVistas();
        inicializarGoogle();
        inicializarFacebook();
        inicializarAutenticacion();
    }

    /**
     * Se necesita agregar el listener de Firebase en onStart().
     */
    @Override
    public void onStart() {
        super.onStart();
        autenticacionFirebase.addAuthStateListener(listenerAutenticacion);
    }

    /**
     * Quitar el listener antes de salir de la actividad.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (listenerAutenticacion != null) {
            autenticacionFirebase.removeAuthStateListener(listenerAutenticacion);
        }
    }

    /**
     * Este método se llama, en este ejemplo, después de seleccionar la cuenta con la que iniciarás
     * sesión. Se tienen los datos de la cuenta con la que el usuario inició sesión en Google.
     * <p>
     * Debemos mandar llamar al método onActivityResult() de nuestro manejador de llamadas facebook.
     *
     * @param codigoSolicitud
     * @param codigoResultado
     * @param datos
     */
    @Override
    public void onActivityResult(int codigoSolicitud, int codigoResultado, Intent datos) {
        super.onActivityResult(codigoSolicitud, codigoResultado, datos);
        manejadorDeLlamadasFacebook.onActivityResult(codigoSolicitud, codigoResultado, datos);

        /**
         * Revisamos que sea nuestro código de solicitud.
         */
        if (codigoSolicitud == CS_INICIAR_SESION) {
            /**
             * Obtenemos el resultado de la acción, en este caso el resultado del inicio de sesión.
             */
            GoogleSignInResult resultado = Auth.GoogleSignInApi.getSignInResultFromIntent(datos);
            if (resultado.isSuccess()) {
                /**
                 * Obtenemos el objeto cuenta que trae información del usuario.
                 */
                GoogleSignInAccount cuenta = resultado.getSignInAccount();
                /**
                 * Se inicia sesión con Firebase utilizando la cuenta de Google seleccionada.
                 */
                inicioSesionFirebaseConCuentaGoogle(cuenta);
            } else {
                /**
                 * Notificar que hubo algún error.
                 */
                Toast.makeText(this, R.string.error_inicio_sesion, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método para inicializar nuestras vistas.
     */
    private void inicializarVistas() {
        botonIniciarSesionFacebook = (LoginButton) findViewById(R.id.boton_login_facebook);
        botonIniciarSesionGoogle = (SignInButton) findViewById(R.id.google_login);
        botonIniciarSesionGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesionConGoogle();
            }
        });
    }

    /**
     * Método para inicializar Google.
     */
    private void inicializarGoogle() {
        /**
         * Este objeto son las opciones que Google nos proporcionará al momento de hacer login.
         * En este ejemplo, utilizamos la configuración por defecto.
         *
         * Se debe de asignar el requestIdToken que nos da Firebase en la consola, a la hora de habilitar
         * el inicio de sesión con Google.
         *
         * TODO: Agreguen su requestIdToken aquí, para que funcione.
         */
        GoogleSignInOptions opcionesInicioSesionGoogle = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        /**
         * Objeto que se utiliza para iniciar la actividad para iniciar sesión. Se configura con el listener
         * para escuchar los cambios en la conexión con Google. Se le agrega el API que vamos a utilizar,
         * que es el GOOGLE_SIGN_IN_API. También se le agregan las opciones de GoogleSignInOptions que
         * se definieron antes.
         */
        clienteApiGoogle = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, opcionesInicioSesionGoogle)
                .build();
    }
    /**
     * Método donde se inicializan los objetos de Facebook.
     */
    private void inicializarFacebook() {
        /**
         * Creamos el manejador de llamadas de Facebook.
         */
        manejadorDeLlamadasFacebook = CallbackManager.Factory.create();

        /**
         * Agregamos los permisos que queremos pedir al usuario. Al menos debemos de pedir
         * el permiso de email y public_profile.
         */
        botonIniciarSesionFacebook.setReadPermissions("email", "public_profile");

        /**
         * Necesitamos agregar el manejador de llamadas al botón de inicio de sesión. Creamos
         * un FacebookCallback<LoginResult> el cual tiene métodos que se ejecutarán cuando el usuario
         * haya iniciado sesión, cuando haya cancelado el inicio de sesión, o cuando hubo algún error.
         */
        botonIniciarSesionFacebook.registerCallback(manejadorDeLlamadasFacebook, new FacebookCallback<LoginResult>() {
            /**
             * Si fue exitoso el inicio de sesión, iniciamos sesión con Firebase. Necesitamos el
             * Access Token que obtenemos de Facebook para esto.
             * @param resultadoInicioSesionFacebook
             */
            @Override
            public void onSuccess(LoginResult resultadoInicioSesionFacebook) {
                iniciarSesionFirebaseConFacebook(resultadoInicioSesionFacebook.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Usuario canceló inicio de sesión con Facebook.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, "Hubo un error al iniciar sesión con Facebook.", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * El AccessTokenTracker nos sirve para saber cuando hubo algún cambio en el estado de la sesión.
         * Si el tokenDeAccesoActual es null, quiere decir que se cerró sesión de Facebook y procedemos
         * a cerrar sesión de Firebase.
         */
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken tokenDeAccesoAnterior,
                    AccessToken tokenDeAccesoActual) {

                if (tokenDeAccesoActual == null) {
                   //  cerrarSesionFirebase();
                }
            }
        };
    }



    /**
     * Método en el cual se inicializa Firebase.
     */
    private void inicializarAutenticacion() {
        /**
         * Obtener la instancia de FirebaseAuth.
         */
        autenticacionFirebase = FirebaseAuth.getInstance();

        listenerAutenticacion = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuario = firebaseAuth.getCurrentUser();
                if (usuario != null) {
                    /**
                     * El usuario ha iniciado sesión correctamente.
                     */
                    botonIniciarSesionGoogle.setEnabled(false);
                    finish();
                    startActivity(new Intent(Login.this, Principal.class));
                    Toast.makeText(Login.this, "Usuario: " + usuario.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * El usuario aún no ha iniciado sesión.
                     */
                    Toast.makeText(Login.this, "Usuario sin sesión", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    /**
     * Método para cerrar sesión con Firebase.
     */
   /* private void cerrarSesionFirebase() {
        autenticacionFirebase.signOut();
    }*/

    /**
     * Este método inicia la actividad para iniciar sesión con Google. Esta actividad es donde
     * puedes elegir con qué correo iniciar sesión. Se requiere pasar el CS_INICIAR_SESION
     * para obtener el resultado de esa actividad una vez que el usuario termina.
     */
    private void iniciarSesionConGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(clienteApiGoogle);
        startActivityForResult(intent, CS_INICIAR_SESION);
    }

    /**
     * Una vez que se inició sesión con Google y se obtuvo la cuenta, se debe iniciar sesión
     * utilizando Firebase.
     *
     * @param cuentaGoogle
     */
    private void inicioSesionFirebaseConCuentaGoogle(GoogleSignInAccount cuentaGoogle) {
        /**
         * Debemos obtener esta credencial de Firebase utilizando el Token ID de nuestra cuenta Google.
         */
        AuthCredential credencial = GoogleAuthProvider.getCredential(cuentaGoogle.getIdToken(), null);
        /**
         * Iniciar sesión con Firebase, utilizando la credencial obtenida.
         */
        autenticacionFirebase.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            /**
                             * Hubo un error al iniciar la sesión con Firebase.
                             */
                            Toast.makeText(Login.this, "Falló la autenticación.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void iniciarSesionFirebaseConFacebook(AccessToken tokenDeAcceso) {
        /**
         * Creamos la credencial utilizando el token de acceso que nos da Facebook.
         */
        AuthCredential credencial = FacebookAuthProvider.getCredential(tokenDeAcceso.getToken());
        /**
         * Iniciamos sesión con Firebase utilizando la credencial.
         */
        autenticacionFirebase.signInWithCredential(credencial)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            /**
                             * Hubo algún error al iniciar la sesión.
                             */
                            Toast.makeText(Login.this, "Hubo un error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
