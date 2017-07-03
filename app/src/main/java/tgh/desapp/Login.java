package tgh.desapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import BBDD.BBDDbase;
import BBDD.UsuariosProvider;
import Constants.PreferenciasManager;

public class Login extends AppCompatActivity {

    private EditText txtPasword, txtUsuario;
    private Button btnLogin;
    private View mProgressView;
    private View mLoginFormView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        try {
            String user = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.USERNAME, context);
            String pass = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.PASSWORD, context);
            if (user != null && pass != null) {
                //txtPasword.setText(pass);
                //txtUsuario.setText(user);
                //OnLoginClick();
                IrActividadHome();
            }
        }
        catch (Exception e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = (EditText)findViewById(R.id.txtUsuario);
        txtPasword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnLoginClick();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
    }

    private void OnLoginClick(){
        // Reset errors.
        txtUsuario.setError(null);
        txtPasword.setError(null);

        final String apodo = txtUsuario.getText().toString();
        final String password = txtPasword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(apodo)) {
            txtUsuario.setError(getString(R.string.error_field_required));
            focusView = txtUsuario;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            txtPasword.setError(getString(R.string.error_field_required));
            focusView = txtPasword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            UsuariosProvider.LoginUsuario(new BBDDbase.OnJSONResponseCallback() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) throws JSONException {
                    JSONObject json = new JSONObject(response.toString());
                    String Errortxt = json.getString("ERRORES");

                    if (TextUtils.isEmpty(Errortxt)) {
                        JSONObject User = json.getJSONObject("USER");
                        String token = User.getString("TOKEN");
                        String id = User.getString("USER_ID");

                        PreferenciasManager.GuardarPreferencia(PreferenciasManager.USERNAME, apodo, context);
                        PreferenciasManager.GuardarPreferencia(PreferenciasManager.PASSWORD, password, context);
                        PreferenciasManager.GuardarPreferencia(PreferenciasManager.TOKEN, token, context);
                        PreferenciasManager.GuardarPreferencia(PreferenciasManager.USER_ID, id, context);

                        showProgress(false);
                        IrActividadHome();
                    } else {
                        showProgress(false);
                        View focusView = null;
                        txtPasword.setError(Errortxt);
                        focusView = txtPasword;
                        focusView.requestFocus();
                    }
                }
            }, apodo, password);
        }
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

    public void IrActividadHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void IrActividadRegister(View v){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
