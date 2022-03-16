package com.example.easymeal.cl.model.dao;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.easymeal.cl.model.bd.Receta;
import com.example.easymeal.cl.model.bd.Usuario;

import java.util.ArrayList;
import android.content.Context;


public class RecetaDao {
    Context c;
    Receta receta;
    ArrayList<Receta> listaRecetas;
    SQLiteDatabase sql;
    String bd = "easymeal.db";

    public RecetaDao(Context c){
        this.c = c;
        sql = c.openOrCreateDatabase(bd,c.MODE_PRIVATE,null);
        receta = new Receta();
    }

    public boolean insertarReceta(Receta r){
        ContentValues cv = new ContentValues();
        cv.put("nombre", r.getNombre());
        cv.put("pasos", r.getPasos());
        return (sql.insert("t_receta",null,cv) > 0);
    }
    public boolean updateReceta(Receta r){
        ContentValues cv = new ContentValues();
        cv.put("nombre", r.getNombre());
        cv.put("pasos", r.getPasos());
        return (sql.update("t_receta",cv,"idReceta="+r.getIdReceta(),null)>0);
    }
    public ArrayList<Receta> selectReceta(String nombre){
        ArrayList<Receta> listaRecetas =new ArrayList<Receta>();
        listaRecetas.clear();
        Cursor cr = sql.rawQuery("select * from t_receta where nombre='"+nombre+"'",null);
        if(cr != null && cr.moveToFirst()){
            do{
                Receta receta = new Receta();
                receta.setIdReceta(cr.getInt(0));
                receta.setNombre(cr.getString(1));
                receta.setNombre(cr.getString(2));
                listaRecetas.add(receta);
            }while(cr.moveToNext());
        }
        return listaRecetas;
    }

}
