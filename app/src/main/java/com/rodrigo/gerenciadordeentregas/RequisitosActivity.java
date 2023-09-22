package com.rodrigo.gerenciadordeentregas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rodrigo.gerenciadordeentregas.bd.CriarBanco;

public class RequisitosActivity extends AppCompatActivity {
    public int REQUEST_IMAGE_CAPTURE;
    public ImageView imagem_foto;
    Button salvarRequisitos, voltar, foto;
    EditText idRequisito, descricao, dataRegistro, nivelImportancia, nivelDificuldade, previsaoEntrega;
    CriarBanco gerenciadorBancoDeDados;
    SQLiteDatabase bancoDeDados;
    TextView posicao;

    FusedLocationProviderClient localizacaoService;


    private boolean confirmaPermissaoDeLocalizacao() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return false;
        }
    }

    @SuppressLint({"MissingInflatedId", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisitos);

        confirmaPermissaoDeLocalizacao();

        gerenciadorBancoDeDados = new CriarBanco(this, "entrega", null, 1);
        bancoDeDados = gerenciadorBancoDeDados.getWritableDatabase();

        idRequisito = findViewById(R.id.edtIdRequisito);
        descricao = findViewById(R.id.edtDescricaoRequisito);
        dataRegistro = findViewById(R.id.edtDataRegistro);
        nivelImportancia = findViewById(R.id.edtNivelImportancia);
        nivelDificuldade = findViewById(R.id.edtNivelDificuldade);
        previsaoEntrega = findViewById(R.id.editTextTime);
        salvarRequisitos = findViewById(R.id.btnSalvarRequisitos);
        voltar = findViewById(R.id.btnVoltarRequisitos);
        posicao = findViewById(R.id.posicao);
        foto = findViewById(R.id.btnFoto);
        this.imagem_foto = (ImageView) findViewById(R.id.imageFoto);
        

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TirarFoto();
            }
        });
        

        localizacaoService = LocationServices.getFusedLocationProviderClient(this);
        localizacaoService.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            posicao.setText("Latitude: " + location.getLatitude()
                                    + " Longitude: " + location.getLongitude());
                        }
                    }
                });

        salvarRequisitos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues valores = new ContentValues();
                valores.put("idProjeto", idRequisito.getText().toString());
                valores.put("tempo_entrega", previsaoEntrega.getText().toString());
                valores.put("importancia_cliente", nivelImportancia.getText().toString());
                valores.put("data_registro", dataRegistro.getText().toString());
                valores.put("dificuldade_implemento", nivelDificuldade.getText().toString());
                valores.put("descricao", descricao.getText().toString());




                bancoDeDados = gerenciadorBancoDeDados.getWritableDatabase();
                /* insere os valores na tabela "item" */
                long resultado = bancoDeDados.insert("requisitos", null, valores);
                bancoDeDados.close();

                if (resultado ==-1) {
                    Toast.makeText(RequisitosActivity.this,
                            "Não foi possível inserir estes requisitos",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(RequisitosActivity.this,
                            "Requisitos adicionados com sucesso!!!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencao_de_navegacao = new Intent(RequisitosActivity.this, MainActivity.class);
                startActivity(intencao_de_navegacao);
            }
        });

    }

    private void TirarFoto() {
        this.REQUEST_IMAGE_CAPTURE = 1;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, this.REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.imagem_foto.setImageBitmap(imageBitmap);
        }
    }
}