package com.rodrigo.gerenciadordeentregas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rodrigo.gerenciadordeentregas.bd.CriarBanco;

public class MainActivity extends AppCompatActivity {
    Button InserirRequisito, SalvarProjeto;
    EditText Nome, DataInicio, DataEntrega;
    CriarBanco gerenciadorBancoDeDados;
    SQLiteDatabase bancoDeDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gerenciadorBancoDeDados = new CriarBanco(this, "entrega", null, 1);
        bancoDeDados = gerenciadorBancoDeDados.getWritableDatabase();

        Nome = findViewById(R.id.edtNome);
        DataInicio = findViewById(R.id.edtDataInicio);
        DataEntrega = findViewById(R.id.edtDataEntrega);
        InserirRequisito = findViewById(R.id.btnInserirRequisito);
        SalvarProjeto = findViewById(R.id.btnSalvarProjeto);


        InserirRequisito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(MainActivity.this, RequisitosActivity.class);
                startActivity(intencao_de_navegacao);
            }
        });

        SalvarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues valores = new ContentValues();
                valores.put("nome", Nome.getText().toString());
                valores.put("data_inicio", DataInicio.getText().toString());
                valores.put("data_entrega", DataEntrega.getText().toString());

                bancoDeDados = gerenciadorBancoDeDados.getWritableDatabase();
                /* insere os valores na tabela "item" */
                long resultado = bancoDeDados.insert("projeto", null, valores);
                bancoDeDados.close();

                if (resultado ==-1)
                    Toast.makeText(MainActivity.this,
                            "Não foi possível inserir este item",
                            Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this,
                            "Projeto " + Nome.getText() + " criado com sucesso!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
                    });
    }
}