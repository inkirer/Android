package tgh.desapp.EventosPages;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import BBDD.BBDDbase;
import BBDD.EventosProvider;
import BBDD.ProvinciasProvider;
import Entities.SpinnerCls;
import tgh.desapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Evento_detalles.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Evento_detalles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Evento_detalles extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner ddlProvincias;
    // TODO: Rename and change types of parameters
    private String id_evento;
    private String token;
    private Context con;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id_evento Parameter 1.
     * @param token Parameter 2.
     * @return A new instance of fragment Evento_detalles.
     */
    // TODO: Rename and change types and number of parameters
    public static Evento_detalles newInstance(String id_evento, String token) {
        Evento_detalles fragment = new Evento_detalles();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id_evento);
        args.putString(ARG_PARAM2, token);
        fragment.setArguments(args);
        return fragment;
    }

    public Evento_detalles() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id_evento = getArguments().getString(ARG_PARAM1);
            token = getArguments().getString(ARG_PARAM2);
        }
        con = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_evento_detalles, container, false);
        ddlProvincias = (Spinner)rootView.findViewById(R.id.ddlProvincia);
        CargarProvincias();
        EventosProvider.GetEventoByID(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");
                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Evento = json.getJSONArray("EVENTOS");
                    String titulo = Evento.getJSONObject(0).getString("NOM_EVENTO");
                    String descripcion = Evento.getJSONObject(0).getString("DESCRIPCION");
                    String fecha = Evento.getJSONObject(0).getString("FECHA_INICIO");
                    String hora = Evento.getJSONObject(0).getString("HORA_INICIO");
                    String provincia = Evento.getJSONObject(0).getString("ID_PROVINCIA");

                    ddlProvincias.setEnabled(false);
                    ddlProvincias.setClickable(false);
                    ddlProvincias.setSelection(Integer.parseInt(provincia));

                    EditText txtTitulo = (EditText) rootView.findViewById(R.id.txtNombreEvento);
                    txtTitulo.setText(titulo);
                    EditText txtDesc = (EditText) rootView.findViewById(R.id.txtDescEvento);
                    txtDesc.setText(descripcion);
                    EditText txtFecha = (EditText) rootView.findViewById(R.id.txtFecha);
                    txtFecha.setText(fecha);
                    EditText txtHora = (EditText) rootView.findViewById(R.id.txtHora);
                    txtHora.setText(hora);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void CargarProvincias(){
        ProvinciasProvider.GetAllProvincias(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");

                if (TextUtils.isEmpty(Errortxt)) {
                    JSONArray Provincias = json.getJSONArray("PROVINCIAS");

                    JSONArray arr = Provincias;
                    List<SpinnerCls> list = new ArrayList<SpinnerCls>();
                    for (int i = 0; i < arr.length(); i++) {
                        list.add(new SpinnerCls(Integer.parseInt(arr.getJSONObject(i).getString("ID_PROVINCIA")),
                                arr.getJSONObject(i).getString("NOM_PROVINCIA")));
                    }

                    ArrayAdapter<SpinnerCls> adapter = new ArrayAdapter<SpinnerCls>(con, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ddlProvincias.setAdapter(adapter);

                } else {
                }
            }
        }, token);
    }
}
