package tgh.desapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import BBDD.BBDDbase;
import BBDD.UsuariosProvider;
import Constants.PreferenciasManager;
import Entities.ImagenAyuda;
import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    //private UserLoginTask mAuthTask = null;

    private EditText txtPasword, txtApodo, txtEmail, txtNombre;
    private TextView lblError, lblMessage;
    private View Register_progress;
    private View register_form;
    private CircleImageView imgPerfil;
    private Button btnRegistrar;
    public static String apodo, email;
    private Context context;
    private static int RESULT_LOAD_IMG = 1;
    String imgPath, fileName;
    private Uri mImageCaptureUri;
    Controller aController;
    String regId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this;

        Controller aController = (Controller)getApplicationContext();
        if (!aController.isConnectingToInternet()) {
            aController.showAlertDialog(context,
                    "Error con la conexion a Internet",
                    "Conectate a una red", false);
            return;
        }

        registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.DISPLAY_MESSAGE_ACTION));
        regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
            regId = GCMRegistrar.getRegistrationId(this);
        } else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                Toast.makeText(getApplicationContext(),
                        "Already registered with GCM Server",
                        Toast.LENGTH_LONG).
                        show();
            }
        }

        txtPasword = (EditText)findViewById(R.id.txtPassword);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtApodo = (EditText)findViewById(R.id.txtApodo);
        imgPerfil = (CircleImageView)findViewById(R.id.imgPerfil);
        lblError = (TextView)findViewById(R.id.lblMessage);

        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnRegisterClick();
            }
        });

        Register_progress = findViewById(R.id.Register_progress);
        register_form = findViewById(R.id.register_form);

        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubirImagenPerfil();
            }
        });

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile_pic);
        //Bitmap resized = Bitmap.createScaledBitmap(bm, 96, 96, true);
        //Bitmap conv_bm = ImagenAyuda.GetImageCircular(resized);
        imgPerfil.setImageBitmap(bm);
    }

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            //aController.acquireWakeLock(getApplicationContext());
            // Display message on the screen
            lblMessage.append(newMessage + " ");
            Toast.makeText(getApplicationContext(), "Got Message: " + newMessage,
                    Toast.LENGTH_LONG).show();
            // Releasing wake lock
            //aController.releaseWakeLock();
        }
    };

    private void OnRegisterClick(){
        txtApodo.setError(null);
        txtEmail.setError(null);
        txtNombre.setError(null);
        txtPasword.setError(null);

        String nombre = txtNombre.getText().toString();
        apodo = txtApodo.getText().toString();
        email = txtEmail.getText().toString();
        final String password = txtPasword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPasword.setError(getString(R.string.error_invalid_password));
            focusView = txtPasword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(nombre)) {
            txtNombre.setError(getString(R.string.error_field_required));
            focusView = txtNombre;
            cancel = true;
        } else if (TextUtils.isEmpty(apodo)) {
            txtApodo.setError(getString(R.string.error_field_required));
            focusView = txtApodo;
            cancel = true;
        } else if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
            cancel = true;
        } else if (isCamposValid(nombre)) {
            txtNombre.setError(getString(R.string.error_field_expression));
            focusView = txtNombre;
            cancel = true;
        } else if (isCamposValid1(apodo)) {
            txtApodo.setError(getString(R.string.error_field_expression));
            focusView = txtApodo;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            lblMessage = (TextView) findViewById(R.id.lblMessage);

            final Context context = this;
            Bitmap bitmap = ((BitmapDrawable) imgPerfil.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            String imagen = Base64.encodeToString(byte_arr, Base64.NO_WRAP);

            UsuariosProvider.InsertarUsuario(new BBDDbase.OnJSONResponseCallback() {
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
                        if (Errortxt.contains("Usuario")) {
                            txtApodo.setError(Errortxt);
                            focusView = txtApodo;
                        } else {
                            txtEmail.setError(Errortxt);
                            focusView = txtEmail;
                        }
                        focusView.requestFocus();
                    }
                }
            }, nombre, apodo, password, email, imagen, regId);
        }
        showProgress(false);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            Register_progress.setVisibility(show ? View.GONE : View.VISIBLE);
            register_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    register_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            Register_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            Register_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Register_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            Register_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    private boolean isCamposValid(String campo) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]");
        return pattern.matcher(campo).matches();
    }
    private boolean isCamposValid1(String campo) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_]");
        return pattern.matcher(campo).matches();
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public void IrActividadHome(){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void SubirImagenPerfil(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(galleryIntent, "Pruebas"), 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                doCrop();
                break;
            case 3:
                mImageCaptureUri = data.getData();
                doCrop();
                break;
            case 2:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    Bitmap resized = Bitmap.createScaledBitmap(photo, 96, 96, true);
                    Bitmap conv_bm = ImagenAyuda.GetImageCircular(resized);
                    imgPerfil.setImageBitmap(conv_bm);
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists())
                    f.delete();
                break;
        }
    }
    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    public class CropOptionAdapter extends ArrayAdapter<CropOption>{
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options){
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group){
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }


    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            intent.putExtra("circleCrop", "true");

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

                startActivityForResult(i, 2);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);

                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        2);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            getContentResolver().delete(mImageCaptureUri, null,
                                    null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }
}
