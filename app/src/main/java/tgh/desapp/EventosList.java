package tgh.desapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BBDD.BBDDbase;
import BBDD.EventosProvider;
import Constants.PreferenciasManager;
import Entities.EventosCls;

public class EventosList extends AppCompatActivity {

    private String Cat, user_id, token;
    private ListView lvEvent;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_list);
        con = this;
        user_id = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.USER_ID, this);
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);
        Intent intent = getIntent();
        Cat = intent.getExtras().getString("ID_CATEGORIA");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEventosList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvEvent = (ListView)findViewById(R.id.listEventos);
        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventosCls cls = ((EventosCls)lvEvent.getAdapter().getItem(position));
                Intent intent = new Intent(con, Eventos.class);
                intent.putExtra("ID_EVENTO", String.valueOf(cls.getId_evento()));
                startActivity(intent);
            }
        });



        CargarGridEventos(Cat);
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

    public class EventoAdapter extends ArrayAdapter<EventosCls> {
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
}
