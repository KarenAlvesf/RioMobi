package com.example.rioMobi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;  // Necessário para o Log
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;  // Para GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;  // Para ApiException
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.android.gms.tasks.Task;  // Para Task
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView forgotPasswordTextView = findViewById(R.id.forgot_pass);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {
                Intent intent = new Intent(MainActivity.this, RecuperarSenha.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();

        @SuppressWarnings("deprecation")
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Client ID
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configuração do TextView clicável
        TextView textViewNavigate = findViewById(R.id.textViewNavigate);
        textViewNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar para a Activity Criar_conta
                Intent intent = new Intent(MainActivity.this, Criar_conta.class);
                startActivity(intent);
            }
        });

        // Configuração para o Google Sign-In Button (caso queira um botão de login)
        findViewById(R.id.googleSignInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();  // Iniciar o fluxo de login com Google
            }
        });
    }

    // Iniciar o fluxo de login com Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Processar o resultado do login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // O Task retornado do método de autenticação do Google
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Sucesso no login
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Falha no login
                Log.w("MainActivity", "Google sign in failed", e);
            }
        }
    }

    // Autentica o usuário com o Firebase usando a conta do Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Atualize a UI com os dados do usuário
                    } else {
                        // Se falhar
                        Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                    }
                });
    }
}