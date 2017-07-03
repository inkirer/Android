package tgh.desapp.EventosPages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BBDD.BBDDbase;
import BBDD.InvitacionesProvider;
import BBDD.NotificacionesProvider;
import BBDD.ParticipantesProvider;
import Constants.CLASE_NAME;
import Constants.PreferenciasManager;
import Entities.UsuariosCls;
import de.hdodenhof.circleimageview.CircleImageView;
import tgh.desapp.R;

public class Evento_invitar extends AppCompatActivity {

    private EditText txtBuscar;
    private ListView lv;
    private Context con;
    private String id_evento, token;
    private String user_id;
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_invitar);

        Intent inten = getIntent();
        id_evento = inten.getExtras().getString("ID_EVENTO");
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);
        user_id = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.USER_ID, this);

        con = this;
        progress = findViewById(R.id.progressBar2);
        lv = (ListView) findViewById(R.id.listInvitar);
        lv.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        txtBuscar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lv.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                CargarParticipantes(s.toString());
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarParticipante);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(Evento_invitar.this);
            }
        });
    }

    public void CargarParticipantes(final String busqueda) {
        ParticipantesProvider.GetAllParticipantesInvitar(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");
                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Participantes = json.getJSONArray("PARTICIPANTES");
                    ArrayList<UsuariosCls> list = new ArrayList<>();
                    for (int i = 0; i < Participantes.length(); i++) {
                        byte[] bt = Base64.decode(Participantes.getJSONObject(i).getString("IMAGEN"), Base64.NO_WRAP);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                        //Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 96, 96, true);
                        //Bitmap conv_bm = ImagenAyuda.GetImageCircular(resized);

                        list.add(new UsuariosCls(Participantes.getJSONObject(i).getInt("USER_ID"),
                                Participantes.getJSONObject(i).getString("NAME"),
                                Participantes.getJSONObject(i).getString("NICKNAME"),
                                Participantes.getJSONObject(i).getString("GCM_ID"),
                                decodedByte));
                    }
                    if(busqueda.length() == 0){
                        ParticipantesAdapter adapter = new ParticipantesAdapter(con, new ArrayList<UsuariosCls>());
                        lv.setAdapter(adapter);
                    }
                    else {
                        ParticipantesAdapter adapter = new ParticipantesAdapter(con, list);
                        lv.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(con, Errortxt, Toast.LENGTH_LONG).show();
                }
                lv.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        }, id_evento, token, busqueda);
    }

    public class ParticipantesAdapter extends ArrayAdapter<UsuariosCls> {
        private class ViewHolder {
            TextView Nombre;
            TextView Nickname;
            ImageView imgInvitar;
            CircleImageView imgPerfil;
            View load;

        }

        public ParticipantesAdapter(Context context, ArrayList<UsuariosCls> Participantes) {
            super(context, R.layout.item_invitar, Participantes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final UsuariosCls Participantes = getItem(position);
            final int pos = position;
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_invitar, parent, false);
                viewHolder.Nombre = (TextView) convertView.findViewById(R.id.txtNombrePart);
                viewHolder.Nickname = (TextView) convertView.findViewById(R.id.txtNicknamePart);
                viewHolder.imgPerfil = (CircleImageView) convertView.findViewById(R.id.imgUsuario);
                viewHolder.imgInvitar = (ImageView) convertView.findViewById(R.id.imgInvitar);
                viewHolder.load = convertView.findViewById(R.id.progressBar);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Nombre.setText(Participantes.getNombre());
            viewHolder.Nickname.setText(Participantes.getApodo());
            viewHolder.imgPerfil.setImageBitmap(Participantes.getImagen());
            viewHolder.load.setVisibility(View.GONE);
            viewHolder.imgInvitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.imgInvitar.setVisibility(View.GONE);
                    viewHolder.load.setVisibility(View.VISIBLE);
                    Invitar(Participantes.getUser_id(), Participantes.getGcm_id(), pos);
                }
            });
            return convertView;
        }
    }

    public void Invitar(int invitado,final String reg_id, final int position){
        InvitacionesProvider.InsertarInvitacion(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    CargarParticipantes(txtBuscar.getText().toString());
                    Toast.makeText(getApplicationContext(), "Invitación enviada", Toast.LENGTH_LONG).show();

                    NotificacionesProvider.EnviarNotificacion(new BBDDbase.OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success, JSONObject response) throws JSONException {

                        }
                    }, "Has recibido una invitaicon a un evento", reg_id, CLASE_NAME.Evento, "Invitacion evento", "ID_EVENTO,17");
                } else {
                    Toast.makeText(getApplicationContext(), Errortxt, Toast.LENGTH_LONG).show();
                }
            }
        }, Integer.parseInt(id_evento), invitado, Integer.parseInt(user_id), token, reg_id);
    }
}
