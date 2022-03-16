package com.example.easymeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.easymeal.Excepciones.MisExcepciones;
import com.example.easymeal.cl.model.bd.Ingrediente;
import com.example.easymeal.cl.model.bd.Producto;
import com.example.easymeal.cl.model.dao.ProductoDao;
import com.example.easymeal.cl.model.dao.IngredienteDao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AgregarLista extends AppCompatActivity {

    //Inicializamo variable
    DrawerLayout dl;
    EditText etCantidad, etMarca, etDescripcion, etMedida;
    Spinner sMedida, sMarca, sDescripcion;
    ImageView ivFoto;
    Producto pro;
    Ingrediente ing;
    IngredienteDao ingdao;
    ProductoDao prodao;
    ArrayList<Ingrediente> listaing;
    ArrayList<Producto> listaprod;

    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agregar_lista);

        //Asignamos variable
        dl = findViewById(R.id.drawer_agregar_lista);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMarca = findViewById(R.id.etMarca);
        etCantidad = findViewById(R.id.etCantidad);
        sDescripcion = findViewById(R.id.sDescripcion);
        sMarca = findViewById(R.id.sMarca);
        sMedida = findViewById(R.id.sMedida);
        ivFoto = findViewById(R.id.ivFoto);
        etMedida = findViewById(R.id.etMedida);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null) {
            tipo = parametros.getString("tipo");
        }

        if(tipo.equals("mandado")){
            ivFoto.setVisibility(View.GONE);
        }
        llenarSpinners();
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
        Menu.redirectActivity(this, ListaMandado.class);
    }

    public void ClickHorario(View view){
        //Redireccionamos actividad a dashboard
        Menu.redirectActivity(this, Horario.class);
    }

    public void ClickSalir(View v){
        //Cerramos app
        Menu.logout(this);
    }

    public void ClickSalirAgregar(View v){
        //Cerramos app
        Menu.redirectActivity(this, ListaMandado.class);
    }

    public void ClickUsuarios (View v){
        //Nos dirijimos al menu de los usuarios
        Menu.redirectActivity(this,MenuUsuario.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Cerramos Drawer
        Menu.closeDrawer(dl);
    }

    public void AgregarMarca(View view) {
        String marca = etMarca.getText().toString();
        if(!marca.trim().isEmpty()){


            Toast.makeText(this, "INSERTADO", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Escribir el proveedor", Toast.LENGTH_LONG).show();
        }

    }

    public void ClickAgregarLista(View view) {
        try {

            String descripcion;
            String medida;
            String marca;

            String fechaCaducidad = null;

            ingdao = new IngredienteDao();
            ingdao.ingredienteDao(this);
            ing = new Ingrediente();
            prodao = new ProductoDao();
            prodao.productoDao(this);
            pro = new Producto();
            Bitmap bitmap = null;
            byte[] img = null;

            if(!ivFoto.getTag().equals("pred")){
                bitmap = ((BitmapDrawable)ivFoto.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap .compress(Bitmap.CompressFormat.PNG, 100, bos);
                img = bos.toByteArray();
            }

            if(!camposVacios()){
                if(sDescripcion.getSelectedItemPosition() == 0){
                    descripcion = etDescripcion.getText().toString();
                }else{
                    descripcion = sDescripcion.getSelectedItem().toString();
                }

                if(sMedida.getSelectedItemPosition() == 0){
                    medida = etMedida.getText().toString();
                }else{
                    medida = etMedida.getText().toString();
                }

                if(sMarca.getSelectedItemPosition() == 0){
                    marca = etMarca.getText().toString();
                }else{
                    marca = etMarca.getText().toString();
                }

                ing.setDescripcion(descripcion);
                ing.setUnidadDeMedida(medida);
                pro.setProveedor(marca);
                ing.setImagen(img);
                if(tipo.equals("mandado")){
                    System.out.println("Hola");
                    ing.setMandado(1);
                    ing.setCantidadAComprar(Float.valueOf(etCantidad.getText().toString()));
                    ing.setCantidad(0f);
                    ing.setFechaCaducidad(null);
                }else{
                    System.out.println("Hola2");
                    ing.setMandado(0);
                    ing.setCantidadAComprar(0f);
                    ing.setCantidad(Float.valueOf(etCantidad.getText().toString()));
                    ing.setFechaCaducidad(fechaCaducidad);
                }
                prodao.insertarProducto(pro);
                ingdao.insertarLista(ing);
                Toast.makeText(this, "INSERTADO", Toast.LENGTH_LONG).show();
                limpiarCampos();
            }
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }

    public boolean camposVacios(){
        try {
            if(etDescripcion.getText().toString().trim().isEmpty()||sDescripcion.getSelectedItemPosition() == 0){
                throw new MisExcepciones(1);
            }else if(etCantidad.getText().toString().trim().isEmpty()){
                throw new MisExcepciones(2);
            }else if(etMarca.getText().toString().trim().isEmpty()||sMarca.getSelectedItemPosition() == 0){
                throw new MisExcepciones(3);
            }else if(etMedida.getText().toString().trim().isEmpty()||sMedida.getSelectedItemPosition() == 0){
                throw new MisExcepciones(4);
            }
            return false;
        } catch (MisExcepciones me) {
            Toast.makeText(this, me.getMessage(), Toast.LENGTH_LONG);
            return true;
        }
    }

    public void llenarSpinners() {
        ingdao = new IngredienteDao();
        ingdao.ingredienteDao(this);
        listaing = ingdao.listaIngredientes();
        prodao = new ProductoDao();
        prodao.productoDao(this);
        listaprod = prodao.listaProducto();

        ArrayList<String> listadesc = new ArrayList<>();
        ArrayList<String> listaMedida = new ArrayList<>();
        ArrayList<String> listaprodu = new ArrayList<>();
        listadesc.add("Seleccione...");
        listaMedida.add("Seleccione...");
        listaprodu.add("Seleccione...");

        for(Ingrediente ingre: listaing){
            listadesc.add(ingre.getDescripcion());
            listaMedida.add(String.valueOf(ingre.getUnidadDeMedida()));
        }

        for(Producto produc: listaprod){
            listaprodu.add(produc.getProveedor());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listadesc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sDescripcion.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaMedida);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMedida.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listaprodu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sMarca.setAdapter(arrayAdapter);
    }

    public void limpiarCampos(){
        etDescripcion.setText("");
        etCantidad.setText("0.00");
        etMarca.setText("");
        etMedida.setText("");
        ivFoto.setImageResource(R.drawable.ic_camara);
        ivFoto.setTag("pred");
    }

    public void ClickFoto(View view) {
        Intent foto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(foto.resolveActivity(getPackageManager()) != null){
            startActivityForResult(foto, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFoto.setImageBitmap(imageBitmap);
            ivFoto.setTag("nopred");
        }
    }
}