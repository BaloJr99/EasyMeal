package com.example.easymeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easymeal.cl.model.bd.Ingrediente;
import com.example.easymeal.cl.model.dao.IngredienteDao;
import com.example.easymeal.pdf.TemplatePDF;

import java.lang.reflect.Array;
import java.security.Permission;
import java.util.ArrayList;

public class ListaMandado extends AppCompatActivity {

    //Inicializamo variable
    DrawerLayout dl;
    Button btnAgregar;
    static String username;

    IngredienteDao ingDao;
    Ingrediente ing;
    ArrayList<Ingrediente> listaIng;

    TableLayout tling;
    TableRow tring;

    TextView tvDescripcion, tvCantidad, tvMedida;
    ImageView ivEliminar;
    String tipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_lista_mandado);

        //Asignamos variable
        dl = findViewById(R.id.drawer_listamandado);
        btnAgregar = findViewById(R.id.agregarlista);
        tling = findViewById(R.id.tling);

        Bundle datos = this.getIntent().getExtras();
        tipo = datos.getString("tipo");
        llenarMandado();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abrirAgregarLista = new Intent(ListaMandado.this, AgregarLista.class);
                abrirAgregarLista.putExtra("tipo", tipo);
                startActivity(abrirAgregarLista);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

    }

    public void ClickMenu(View v){
        //Abrimos Drawer
        Menu.openDrawer(dl);
    }

    public void ClickLogo(View v){
        //Cerramos drawer
        Menu.closeDrawer(dl);
    }

    public void ClickInicio(View v){
        //Redireccionamos activity a inicio
        Menu.redirectActivity(this, Menu.class);
    }

    public void ClickRecetas(View v){
        //Redireccionamos actividad a tablero
        Menu.redirectActivity(this, Recetas.class);
    }

    public void ClickAcercaDe(View v){
        //Recreamos actividad
        Menu.redirectActivity(this, AcercaNosotros.class);
    }

    public void ClickLista(View view){
        //Redireccionamos actividad a dashboard
        recreate();
    }

    public void ClickHorario(View view){
        //Redireccionamos actividad a dashboard
        Menu.redirectActivity(this, Horario.class);
    }

    public void ClickUsuarios (View v){
        //Nos dirijimos al menu de los usuarios
        Menu.redirectActivity(this,MenuUsuario.class);
    }

    public void ClickSalir(View v){
        //Cerramos app
        Menu.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cerramos Drawer
        Menu.closeDrawer(dl);
    }

    public void ClickEliminar(int id) {
        ingDao = new IngredienteDao();
        ingDao.ingredienteDao(this);
        if(ingDao.eliminarLista(id)){
            Toast.makeText(this, "ELIMINADO", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
        this.recreate();
    }

    public void llenarMandado(){
        ingDao = new IngredienteDao();
        ingDao.ingredienteDao(this);
        listaIng = ingDao.listaMandado(tipo);
        TableRow.LayoutParams lfila = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams ldescripcion = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4f);
        TableRow.LayoutParams lcantidad = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4f);
        TableRow.LayoutParams lmedida = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4f);
        TableRow.LayoutParams leliminar = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 3f);

        for(Ingrediente listing: listaIng){
            tring = new TableRow(this);
            tring.setLayoutParams(lfila);

            tvDescripcion = new TextView(this);
            tvDescripcion.setText(listing.getDescripcion());
            tvDescripcion.setLayoutParams(ldescripcion);
            tvDescripcion.setGravity(Gravity.CENTER);
            tring.addView(tvDescripcion);

            tvCantidad = new TextView(this);
            if(tipo.equals("mandado")){
                tvCantidad.setText(String.valueOf(listing.getCantidadAComprar()));
            }else{
                tvCantidad.setText(String.valueOf(listing.getCantidad()));
            }
            tvCantidad.setLayoutParams(lcantidad);
            tvCantidad.setGravity(Gravity.CENTER);
            tring.addView(tvCantidad);

            tvMedida = new TextView(this);
            tvMedida.setText(listing.getUnidadDeMedida());
            tvMedida.setLayoutParams(lmedida);
            tvMedida.setGravity(Gravity.CENTER);
            tring.addView(tvMedida);

            ivEliminar = new ImageView(this);
            ivEliminar.setTag(listing.getIdIngrediente());
            ivEliminar.setImageResource(R.drawable.ic_delete);
            ivEliminar.setLayoutParams(leliminar);
            ivEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TableRow tablerow = (TableRow) view.getParent();
                    ImageView items = (ImageView) tablerow.getChildAt(3);

                    ClickEliminar(Integer.parseInt(items.getTag().toString()));
                }
            });
            tring.addView(ivEliminar);
            tling.addView(tring);

        }
    }

    private String[] header = {"Id", "Nombre", "Apellido"};
    private String shortText = "Hola";
    private String longText = "Nunca consideres el estudio como una obligacion";

    public void ClickGenerar(View view) {
        TemplatePDF templatePDF = new TemplatePDF(this);
        templatePDF.openDocument();
        templatePDF.addMetaData("Clientes", "Ventas", "Braulio");
        templatePDF.addTitles("Tienda CodigoFacilito", "Clientes", "06/12/2017");
        templatePDF.addParagraph(shortText);
        templatePDF.addParagraph(longText);
        templatePDF.createTable(header, getClients());
        templatePDF.closeDocument();
        templatePDF.appViewPDF(this);
    }

    private ArrayList<String[]> getClients(){
        ArrayList<String[]> rows = new ArrayList<>();

        rows.add(new String[]{"1","Pedro", "Lopez"});
        rows.add(new String[]{"2","Sofia", "Hernandez"});
        rows.add(new String[]{"3","Naomi", "Alfaro"});
        rows.add(new String[]{"4","Lorena", "Espejel"});

        return rows;
    }
}