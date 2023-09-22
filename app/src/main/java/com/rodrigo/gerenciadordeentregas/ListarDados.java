package com.rodrigo.gerenciadordeentregas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rodrigo.gerenciadordeentregas.bd.CriarBanco;
import com.rodrigo.gerenciadordeentregas.bd.entidade.Projeto;

import java.util.ArrayList;
import java.util.List;


public class ListarDados extends AppCompatActivity {
    Button listarProjetos, voltar;
    CriarBanco gerenciadorBancoDeDados;
    SQLiteDatabase bancoDeDados;
    ListView listView;
    Cursor cursor;
    List<Projeto> projetos;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_dados);
        
        listarProjetos = findViewById(R.id.btnListarProjeto);
        listView = findViewById(R.id.listViewRequisitos);
        voltar = findViewById(R.id.btnVoltarListar);

        gerenciadorBancoDeDados = new CriarBanco(this, "entrega", null, 1);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ListarDados.this, projetos.get(i).toString(), Toast.LENGTH_LONG).show();
            }
        });
        listarProjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String[] campos =  {"id", "nome"};
                    bancoDeDados = gerenciadorBancoDeDados.getReadableDatabase();
                    cursor = bancoDeDados.query("projeto", campos, null,
                            null, null, null, null, null);
                    projetos = new ArrayList<>();

                    //cursor.moveToFirst();
                    while(cursor.moveToNext()) {
                        projetos.add(new Projeto(cursor.getString(0), cursor.getString(1)));

                    }
                    ArrayAdapter<Projeto> meuAdapter = new ArrayAdapter<>(getBaseContext(),
                            android.R.layout.simple_list_item_1, projetos);
                    listView.setAdapter(meuAdapter);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(ListarDados.this, RequisitosActivity.class);
                startActivity(intencao_de_navegacao);
            }
        });
    }
}