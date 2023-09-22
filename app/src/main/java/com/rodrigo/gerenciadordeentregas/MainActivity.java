package com.rodrigo.gerenciadordeentregas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rodrigo.gerenciadordeentregas.bd.CriarBanco;

public class MainActivity extends AppCompatActivity {
    Button InserirRequisito, SalvarProjeto, ListarRequisitos, EnviarRequisitos, Documentacao;
    EditText Nome, DataInicio, DataEntrega;
    CriarBanco gerenciadorBancoDeDados;
    SQLiteDatabase bancoDeDados;
    ListView listView;
    Cursor lista;
    WebView webview;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
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
        ListarRequisitos = findViewById(R.id.btnListarRequisitos);
        listView = findViewById(R.id.listViewRequisitos);
        EnviarRequisitos = findViewById(R.id.btnTelaRequisitos);
        webview = findViewById(R.id.webView);
        Documentacao = findViewById(R.id.btnDocumentacao);
        String url = "https://docs.google.com/document/d/1V1FT-3KeesBU-L4d_M_DYu-fOdIul7dF_BSy_8zlppo/edit?usp=sharing ";

        InserirRequisito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(MainActivity.this, RequisitosActivity.class);
                startActivity(intencao_de_navegacao);
            }
        });

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);

        Documentacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webview.loadUrl(url);
                webview.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);

                    }
                });
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
                            "Não foi possível salvar o projeto",
                            Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(MainActivity.this,
                            "Projeto " + Nome.getText() + " criado com sucesso!!!",
                            Toast.LENGTH_LONG).show();
                    Nome.setText("");
                    DataInicio.setText("");
                    DataEntrega.setText("");
                }
            }
                    });

        ListarRequisitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(MainActivity.this, ListarDados.class);
                startActivity(intencao_de_navegacao);

            }
        });

        EnviarRequisitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(MainActivity.this, ListarRequisitos.class);
                startActivity(intencao_de_navegacao);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}