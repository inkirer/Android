package tgh.desapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BBDD.BBDDbase;
import BBDD.CategoriasProvider;
import BBDD.EventosProvider;
import BBDD.UsuariosProvider;
import Constants.PreferenciasManager;
import Entities.CategoriaCls;
import Entities.EventosCls;
import Entities.ImagenAyuda;
import de.hdodenhof.circleimageview.CircleImageView;


public class Home extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtHeader, descHeader;
    private String user_id, token;
    private ImageView imgEvento;
    private CircleImageView imgPerfilMaterial;
    private Context con;
    private ListView lvCat, lvEvent;
    private View Progres;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user_id = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.USER_ID, this);
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);
        con = this;

        Progres = findViewById(R.id.progressBar3);
        lvCat = (ListView)findViewById(R.id.ListCategorias);
        lvCat.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                CategoriaCls cls = ((CategoriaCls)lvCat.getAdapter().getItem(position));
                String id_cat = String.valueOf(cls.getId_categoria());

                //Progres.setVisibility(View.VISIBLE);
                //lvCat.setVisibility(View.GONE);

                Intent intent = new Intent(con, EventosList.class);
                intent.putExtra("ID_CATEGORIA", id_cat);
                startActivity(intent);
                //CargarGridEventos(id_cat);
            }
        });




        //Progres.setVisibility(View.VISIBLE);
        //lvCat.setVisibility(View.GONE);
        //lvEvent.setVisibility(View.GONE);

        mSwipeRefreshWidget = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorScheme(R.color.Verde, R.color.Azul, R.color.Rojo,
                R.color.Amarillo);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showRefreshProgress();
                CargarGridCategorias();
                hideRefreshProgress();
            }
        });

        /*
        Bitmap bm = BitmapFactory.decodeResource(getResources(), imgEvento.getId());
        Bitmap resized = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
        Bitmap conv_bm = ImagenAyuda.GetImageCircular(resized);
        imgEvento.setImageBitmap(conv_bm);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrActividadEvento();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.dr_Inicio);
        navigationView.setNavigationItemSelectedListener(this);

        //View vi = navigationView.inflateHeaderView(R.layout.nav_header_home);
        //View header = LayoutInflater.from(this).inflate(R.layout.nav_header_home, null);
        //navigationView.addHeaderView(header);

        txtHeader = (TextView)findViewById(R.id.NombreHeader);
        descHeader = (TextView)findViewById(R.id.descHeader);
        imgEvento = (ImageView)findViewById(R.id.imgEvento);
        imgPerfilMaterial = (CircleImageView)findViewById(R.id.imgPerfilMaterial);

        CargarDatosPersona();
        CargarGridCategorias();
        //CargarGridEventos("1");
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_settings){
            btnSalir();
        }
        else if(id == R.id.setting_event){
            IrActividadEvento();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dr_Inicio) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void IrActividadEvento(){
        Intent intent = new Intent(this, Create_Event.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void btnSalir(){
        PreferenciasManager.BorrarPreferencia(PreferenciasManager.USERNAME, this);
        PreferenciasManager.BorrarPreferencia(PreferenciasManager.PASSWORD, this);
        PreferenciasManager.BorrarPreferencia(PreferenciasManager.TOKEN, this);
        PreferenciasManager.BorrarPreferencia(PreferenciasManager.USER_ID, this);
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void CargarDatosPersona(){
        UsuariosProvider.GetUserData(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    JSONObject User = json.getJSONObject("USER");
                    String nombre = User.getString("NAME");
                    String strImg = User.getString("IMAGEN");
                    String apodo = User.getString("NICKNAME");
                    byte[] bt = Base64.decode(strImg, Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                    Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 96, 96, true);
                    //Bitmap conv_bm = ImagenAyuda.GetImageCircular(resized);
                    imgPerfilMaterial.setImageBitmap(resized);
                    txtHeader.setText(nombre);
                    descHeader.setText(apodo);
                } else {
                    Toast.makeText(getApplicationContext(), Errortxt, Toast.LENGTH_LONG).show();
                }
            }
        }, token, user_id);
    }

    public void CargarGridCategorias(){
        CategoriasProvider.GetAllCategorys(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Categoorias = json.getJSONArray("CATEGORIAS");

                    ArrayList<CategoriaCls> list = new ArrayList<>();
                    for (int i = 0; i < Categoorias.length(); i++) {
                        byte[] bt = Base64.decode(Categoorias.getJSONObject(i).getString("IMAGEN"), Base64.NO_WRAP);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                        Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 96, 96, true);
                        Bitmap conv_bm = ImagenAyuda.GetImageSinEsquinas(resized);
                        list.add(new CategoriaCls(Categoorias.getJSONObject(i).getInt("ID_CATEGORIA"),
                                Categoorias.getJSONObject(i).getString("NOM_CATEGORIA"),
                                conv_bm,
                                Categoorias.getJSONObject(i).getInt("NUM")));
                    }
                    CategoriaAdapter adapter = new CategoriaAdapter(con, list);
                    lvCat.setAdapter(adapter);

                } else {
                    Toast.makeText(getApplicationContext(), Errortxt, Toast.LENGTH_LONG).show();
                }
                //Progres.setVisibility(View.GONE);
                //lvCat.setVisibility(View.VISIBLE);
            }
        }, token);
    }

    public void CargarGridEventos(String id_cat){
        EventosProvider.GetAllEventos(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Eventos = json.getJSONArray("EVENTOS");

                    ArrayList<EventosCls> list = new ArrayList<>();
                    for (int i = 0; i < Eventos.length(); i++) {

                        list.add(new EventosCls(Eventos.getJSONObject(i).getInt("ID_EVENTO"),
                                Eventos.getJSONObject(i).getString("NOM_EVENTO"),
                                Eventos.getJSONObject(i).getString("DESCRIPCION"),
                                Eventos.getJSONObject(i).getString("HORA_INICIO"),
                                Eventos.getJSONObject(i).getString("FECHA_INICIO")));
                    }
                    EventoAdapter adapter = new EventoAdapter(con, list);
                    lvEvent.setAdapter(adapter);

                } else {
                    Toast.makeText(getApplicationContext(), Errortxt, Toast.LENGTH_LONG).show();
                }
                //Progres.setVisibility(View.GONE);
                //lvEvent.setVisibility(View.VISIBLE);
            }
        }, token, id_cat);
    }

    public class EventoAdapter extends ArrayAdapter<EventosCls>{
        private class ViewHolder {
            TextView Nombre;
            TextView Descripcion;
            TextView Fecha;
        }

        public EventoAdapter(Context context, ArrayList<EventosCls> Eventos) {
            super(context, R.layout.item_evento, Eventos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EventosCls Evento = getItem(position);

            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_evento, parent, false);
                viewHolder.Nombre = (TextView) convertView.findViewById(R.id.txtTitulo);
                viewHolder.Descripcion = (TextView) convertView.findViewById(R.id.txtDescripcion);
                viewHolder.Fecha = (TextView) convertView.findViewById(R.id.txtFecha);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Nombre.setText(Evento.getNombre());
            viewHolder.Descripcion.setText(Evento.getDescripcion());
            viewHolder.Fecha.setText(Evento.getFecha()+" "+ Evento.getHora());

            return convertView;
        }
    }

    public class CategoriaAdapter extends ArrayAdapter<CategoriaCls>{
        private class ViewHolder {
            TextView Nombre;
            TextView Numero;
            CircleImageView Img;
        }

        public CategoriaAdapter(Context context, ArrayList<CategoriaCls> Categorias) {
            super(context, R.layout.item_categoria, Categorias);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CategoriaCls Categoria = getItem(position);

            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_categoria, parent, false);
                viewHolder.Nombre = (TextView) convertView.findViewById(R.id.txtTitulo);
                viewHolder.Numero = (TextView) convertView.findViewById(R.id.txtDescripcion);
                viewHolder.Img = (CircleImageView) convertView.findViewById(R.id.imgCategoria);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Nombre.setText(Categoria.getNom_categoria());
            viewHolder.Numero.setText("Numero de eventos : " + String.valueOf(Categoria.getNumero()));
            viewHolder.Img.setImageBitmap(Categoria.getImagen());
            return convertView;
        }
    }

    public void showRefreshProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideRefreshProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    public void enableSwipe() {
        mSwipeRefreshWidget.setEnabled(true);
    }

    public void disableSwipe() {
        mSwipeRefreshWidget.setEnabled(false);
    }
}
