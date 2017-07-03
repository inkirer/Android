package tgh.desapp.EventosPages;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BBDD.BBDDbase;
import BBDD.ParticipantesProvider;
import Entities.UsuariosCls;
import de.hdodenhof.circleimageview.CircleImageView;
import tgh.desapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Evento_participantes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Evento_participantes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Evento_participantes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String id_evento;
    private String token;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Evento_participantes.
     */
    // TODO: Rename and change types and number of parameters
    public static Evento_participantes newInstance(String param1, String param2) {
        Evento_participantes fragment = new Evento_participantes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Evento_participantes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_evento = getArguments().getString(ARG_PARAM1);
            token = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_evento_participantes, container, false);


        ParticipantesProvider.GetAllParticipantes(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Participantes = json.getJSONArray("PARTICIPANTES");
                    ListView lv = (ListView) rootView.findViewById(R.id.listParticipantes);
                    ArrayList<UsuariosCls> list = new ArrayList<UsuariosCls>();
                    for (int i = 0; i < Participantes.length(); i++) {
                        byte[] bt = Base64.decode(Participantes.getJSONObject(i).getString("IMAGEN"), Base64.NO_WRAP);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(bt, 0, bt.length);
                        //Bitmap resized = Bitmap.createScaledBitmap(decodedByte, 96, 96, true);
                        // conv_bm = ImagenAyuda.GetImageCircular(resized);

                        list.add(new UsuariosCls(Participantes.getJSONObject(i).getInt("USER_ID"),
                                Participantes.getJSONObject(i).getString("NAME"),
                                Participantes.getJSONObject(i).getString("NICKNAME"),
                                Participantes.getJSONObject(i).getString("GCM_ID"),
                                decodedByte));
                    }
                    ParticipantesAdapter adapter = new ParticipantesAdapter(getActivity(), list);
                    lv.setAdapter(adapter);

                } else {
                    Toast.makeText(getActivity(), Errortxt, Toast.LENGTH_LONG).show();
                }
            }
        }, id_evento, token);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ParticipantesAdapter extends ArrayAdapter<UsuariosCls> {
        private class ViewHolder {
            TextView Nombre;
            TextView Nickname;
            CircleImageView imgPerfil;
        }

        public ParticipantesAdapter(Context context, ArrayList<UsuariosCls> Participantes) {
            super(context, R.layout.item_participante, Participantes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UsuariosCls Participantes = getItem(position);

            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_participante, parent, false);
                viewHolder.Nombre = (TextView) convertView.findViewById(R.id.txtNombrePart);
                viewHolder.Nickname = (TextView) convertView.findViewById(R.id.txtNicknamePart);
                viewHolder.imgPerfil = (CircleImageView) convertView.findViewById(R.id.imgUsuario);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.Nombre.setText(Participantes.getNombre());
            viewHolder.Nickname.setText(Participantes.getApodo());
            viewHolder.imgPerfil.setImageBitmap(Participantes.getImagen());

            return convertView;
        }
    }

}
