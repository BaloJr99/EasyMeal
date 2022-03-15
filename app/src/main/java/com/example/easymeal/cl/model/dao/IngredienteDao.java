package com.example.easymeal.cl.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.easymeal.cl.model.bd.Ingrediente;
import com.example.easymeal.cl.model.bd.Usuario;

import java.util.ArrayList;

public class IngredienteDao {

    Context c;
    Ingrediente ing;
    ArrayList<Ingrediente> listaIngredientes;
    SQLiteDatabase sql;
    String bd = "easymeal.db";
    ContentValues cv = new ContentValues();

    public void ingredienteDao(Context c){
        this.c = c;
        sql = c.openOrCreateDatabase(bd,c.MODE_PRIVATE,null);
        ing = new Ingrediente();
    }

    public boolean insertarIngrediente(Ingrediente i){
        cv = new ContentValues();
        cv.put("descripcion", i.getDescripcion());
        cv.put("cantidad", i.getCantidad());
        cv.put("imagen", i.getImagen());
        return (sql.insert("t_ingrediente",null,cv)>0);
    }

    public ArrayList<Ingrediente> listaIngredientes(){
        ArrayList<Ingrediente> lista = new ArrayList<>();
        Cursor c = sql.rawQuery("select * from t_ingrediente",null);
        if (c.moveToFirst()){
            do {
                ing = new Ingrediente(c.getInt(0), c.getString(2), c.getString(1), c.getString(4), c.getFloat(3), c.getBlob(5));
                lista.add(ing);
            } while(c.moveToNext());
        }
        return lista;
    }

    public ArrayList<Ingrediente> listaMandado() {
        ArrayList<Ingrediente> lista = new ArrayList<>();
        Cursor c = sql.rawQuery("SELECT * FROM t_lista", null);

        if (c.moveToFirst()){
            System.out.println("Hola");
            do {
                ing = new Ingrediente(c.getInt(3), c.getString(1), c.getFloat(2));
                lista.add(ing);
            } while(c.moveToNext());
        }else{
            System.out.println("Hola2");
            Cursor busqueda = sql.rawQuery("SELECT * FROM t_ingrediente WHERE cantidad = 0", null);
            if (busqueda.moveToFirst()){
                do {
                    cv = new ContentValues();
                    cv.put("descripcion", busqueda.getString(1));
                    cv.put("cantidad", busqueda.getFloat(3));
                    cv.put("idIngrediente", busqueda.getString(0));
                    sql.insert("t_lista",null,cv);
                } while(c.moveToNext());
            }
        }

        return lista;
    }
}
