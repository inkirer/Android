package tgh.desapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import BBDD.BBDDbase;
import BBDD.EventosProvider;
import BBDD.ProvinciasProvider;
import Constants.PreferenciasManager;
import Entities.SpinnerCls;

public class Create_Event extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNombre, txtDesc, txtNumParticipantes, txtPrecio, txtFecha, txtHora;
    private View mProgressView, mLoginFormView;
    private Spinner ddlProvincias;
    private String token;
    private SimpleDateFormat dateFormatter;
    private Context con;
    private DatePickerDialog fromDatePickerDialog;
    private TimePickerDialog fromTimePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__event);
        txtNombre = (EditText)findViewById(R.id.txtNombreEvento);
        txtDesc = (EditText)findViewById(R.id.txtDescEvento);
        txtNumParticipantes = (EditText)findViewById(R.id.txtIntegrantes);
        txtPrecio = (EditText)findViewById(R.id.txtPrecio);
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        ddlProvincias = (Spinner)findViewById(R.id.ddlProvincia);
        txtFecha = (EditText) findViewById(R.id.txtFecha);
        txtFecha.setInputType(InputType.TYPE_NULL);
        txtHora = (EditText) findViewById(R.id.txtHora);
        txtHora.setInputType(InputType.TYPE_NULL);

        con = this;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        ddlProvincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int databaseId = ((SpinnerCls) ddlProvincias.getSelectedItem()).getId();
                String databaseValue = ((SpinnerCls) ddlProvincias.getSelectedItem()).getValue();
                Toast.makeText(getApplicationContext(), databaseId + " : " + databaseValue, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/
        setDateTimeField();
        setTimePickerField();
        CargarProvincias();
    }

    @Override
    public void onClick(View view) {
        if(view == txtFecha) {
            fromDatePickerDialog.show();
        }else if(view == txtHora) {
            fromTimePickerDialog.show();
        }
    }

    private void setDateTimeField() {
        txtFecha.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        final Date Current = Calendar.getInstance().getTime();
        fromDatePickerDialog = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (Current.after(newDate.getTime())) {
                    txtFecha.setError("Fecha anterior a la actual");
                } else {
                    txtFecha.setError(null);
                    txtFecha.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    private void setTimePickerField() {
        txtHora.setOnClickListener(this);
        TimePicker Time = new TimePicker(con);
        //Calendar newCalendar = Calendar.getInstance();
        //final Date Current = Calendar.getInstance().getTime();
        fromTimePickerDialog = new TimePickerDialog(con, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimePicker newTime = new TimePicker(con);
                txtHora.setText(hourOfDay + ":" + minute);
            }
        }, Time.getCurrentHour(), Time.getCurrentMinute(), true);
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId()) {
            case R.id.radio_sin_precio:
                if (checked)
                    txtPrecio.setVisibility(View.GONE);
                    break;
            case R.id.radio_paypal:
                if (checked)
                    txtPrecio.setText("");
                    txtPrecio.setVisibility(View.VISIBLE);
                    break;
        }
    }

    public void CargarProvincias(){
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);
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

                    showProgress(false);
                } else {
                    showProgress(false);
                }
            }
        }, token);
    }

    public void CrearEvento(View v){
        showProgress(true);
        String nombre = txtNombre.getText().toString();
        String fecha = txtFecha.getText().toString();
        String tipo_pago = "2";
        String id_categoria = "1";
        int id_provincia = ((SpinnerCls) ddlProvincias.getSelectedItem()).getId();
        String num_participantes = txtNumParticipantes.getText().toString();
        String descripcion = txtDesc.getText().toString();
        String precio = txtPrecio.getText().toString();
        String hora = txtHora.getText().toString();
        String user_id = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.USER_ID, con);
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);

        EventosProvider.InsertarEvento(new BBDDbase.OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                showProgress(false);
                JSONObject json = new JSONObject(response.toString());
                String Errortxt = json.getString("ERRORES");
                if (TextUtils.isEmpty(Errortxt)) {
                    JSONObject User = json.getJSONObject("EVENTO");
                    //Toast.makeText(getApplicationContext(), User.getString("ID_EVENTO"), Toast.LENGTH_LONG).show();
                    IrActividadEvento();
                } else {
                    Toast.makeText(getApplicationContext(), Errortxt, Toast.LENGTH_LONG).show();
                }
            }
        }, nombre, fecha, tipo_pago, id_categoria, id_provincia, num_participantes, descripcion, precio, hora, user_id, token);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, Home.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Titulo prueba")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Titulo prueba")
                .setContentText("Descripcion del mensaje prueba")
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void IrActividadEvento(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
