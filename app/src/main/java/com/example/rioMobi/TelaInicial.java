package com.example.rioMobi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Import correto
import android.view.MenuItem;
import java.util.Calendar;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        // Referência para o EditText de "Data da Viagem"
        EditText travelDateEditText = findViewById(R.id.travel_date);

        // Referência para o EditText de "Data do Retorno"
        EditText travelDateBackEditText = findViewById(R.id.travel_date_back);

        // Criação de um Calendar para pegar a data atual
        Calendar calendar = Calendar.getInstance();

        // Definindo o listener de clique para o EditText de "Data da Viagem"
        travelDateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TelaInicial.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        travelDateEditText.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Definindo o listener de clique para o EditText de "Data do Retorno"
        travelDateBackEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TelaInicial.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        travelDateBackEditText.setText(date);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Referência ao BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Configurando o listener para os itens da barra de navegação
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_passagens:
                        // Navegue para a tela de passagens
                        // Coloque aqui a lógica para navegar para a tela de Passagens
                        return true;

                    case R.id.navigation_perfil:
                        // Navegue para a tela de perfil
                        // Coloque aqui a lógica para navegar para a tela de Perfil
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
