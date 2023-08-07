package com.rodrigo.gerenciadordeentregas.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;



public class CriarBanco extends SQLiteOpenHelper {


    public CriarBanco(@Nullable Context context, @Nullable String name,
                      @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE projeto" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, data_inicio DATE, data_entrega DATE)");

        db.execSQL("CREATE TABLE requisitos (id INTEGER PRIMARY KEY AUTOINCREMENT, idProjeto TEXT," +
                "data_registro DATE, importancia_cliente TEXT, dificuldade_implemento TEXT," +
                "tempo_entrega TEXT, FOREIGN KEY(idProjeto) REFERENCES projeto(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
