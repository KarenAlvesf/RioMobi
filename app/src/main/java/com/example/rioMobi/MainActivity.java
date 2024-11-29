package com.example.rioMobi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.android.gms.tasks.Task;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        // Configuração do Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Substitua com seu Client ID
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //  e-mail, senha
        EditText emailEditText = findViewById(R.id.e_mail);
        EditText passwordEditText = findViewById(R.id.password_toggle);
        Button loginButton = findViewById(R.id.button);

        // Configuração do botão "Entrar"
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validação
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, preencha e-mail e senha", Toast.LENGTH_SHORT).show();
            } else {
                // Realizar o login com Firebase Authentication
                signInWithEmailPassword(email, password);
            }
        });

        // Configuração do botão de "Esqueci minha senha"
        TextView forgotPasswordTextView = findViewById(R.id.forgot_pass);
        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecuperarSenha.class);
            startActivity(intent);
        });

        // Configuração do botão de "Cadastre-se"
        TextView registerTextView = findViewById(R.id.textViewNavigate);
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Criar_conta.class);
            startActivity(intent);
        });

        // Configuração para o Google Sign-In Button
        findViewById(R.id.googleSignInButton).setOnClickListener(v -> signIn());  // Iniciar o fluxo de login com Google
    }

    // Login com Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
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
                        Log.d("MainActivity", "Login com Google bem-sucedido: " + user.getEmail());

                        // Navegar para a TelaInicial
                        Intent intent = new Intent(MainActivity.this, TelaInicial.class);
                        startActivity(intent);
                        finish();  // Para não voltar à MainActivity ao pressionar "Voltar"
                    } else {
                        // Se falhar
                        Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Falha no login com Google.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para autenticar com e-mail e senha usando Firebase
    private void signInWithEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login bem-sucedido
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("MainActivity", "Login com e-mail e senha bem-sucedido: " + user.getEmail());

                        // Navegar para a TelaInicial
                        Intent intent = new Intent(MainActivity.this, TelaInicial.class);
                        startActivity(intent);
                        finish();  // Para não voltar à MainActivity ao pressionar "Voltar"
                    } else {
                        // Falha no login
                        Log.w("MainActivity", "Erro no login com e-mail e senha", task.getException());
                        Toast.makeText(MainActivity.this, "Erro no login. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
