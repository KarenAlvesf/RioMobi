package com.example.rioMobi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referenciar os campos do layout
        EditText emailEditText = findViewById(R.id.e_mail);
        Button resetPasswordButton = findViewById(R.id.buttonContinuar);

        // Configurar clique no botão
        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu e-mail.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar e-mail de redefinição de senha
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "E-mail de recuperação enviado!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erro ao enviar e-mail: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
