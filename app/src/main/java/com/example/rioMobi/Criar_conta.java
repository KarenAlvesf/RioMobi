package com.example.rioMobi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Criar_conta extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        // Inicializar Firebase Authentication e Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referenciar os elementos da interface
        EditText nomeEditText = findViewById(R.id.nome);
        EditText sobrenomeEditText = findViewById(R.id.editTextText2);
        EditText cpfEditText = findViewById(R.id.editTextNumber);
        EditText dataEditText = findViewById(R.id.editTextDate);
        EditText emailEditText = findViewById(R.id.editTextTextEmailAddress);
        EditText senhaEditText = findViewById(R.id.editTextNumberPassword);
        EditText confirmarSenhaEditText = findViewById(R.id.editTextNumberPassword2);
        Button criarContaButton = findViewById(R.id.button2);

        // Configurar ação para o botão de criar conta
        criarContaButton.setOnClickListener(v -> {
            // Obter os valores dos campos
            String nome = nomeEditText.getText().toString().trim();
            String sobrenome = sobrenomeEditText.getText().toString().trim();
            String cpf = cpfEditText.getText().toString().trim();
            String dataNascimento = dataEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();
            String confirmarSenha = confirmarSenhaEditText.getText().toString().trim();

            // Validação dos campos
            if (nome.isEmpty() || sobrenome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() ||
                    email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!senha.equals(confirmarSenha)) {
                Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Registrar usuário no Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Obter o ID do usuário autenticado
                            String userId = mAuth.getCurrentUser().getUid();

                            // Criar objeto com os dados do usuário
                            Map<String, Object> user = new HashMap<>();
                            user.put("nome", nome);
                            user.put("sobrenome", sobrenome);
                            user.put("cpf", cpf);
                            user.put("dataNascimento", dataNascimento);
                            user.put("email", email);

                            // Salvar os dados no Firestore
                            db.collection("users").document(userId).set(user)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(this,
                                            "Conta criada com sucesso!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(this,
                                            "Erro ao salvar os dados no Firestore.",
                                            Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(this, "Erro ao criar conta: " +
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
