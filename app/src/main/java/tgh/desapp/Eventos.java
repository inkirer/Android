package tgh.desapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import Constants.PreferenciasManager;
import tgh.desapp.EventosPages.Evento_detalles;
import tgh.desapp.EventosPages.Evento_invitar;
import tgh.desapp.EventosPages.Evento_localizacion;
import tgh.desapp.EventosPages.Evento_participantes;

public class Eventos extends AppCompatActivity implements Evento_detalles.OnFragmentInteractionListener,
        Evento_localizacion.OnFragmentInteractionListener,
        Evento_participantes.OnFragmentInteractionListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String id_evento, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        Intent intent = getIntent();
        id_evento = intent.getExtras().getString("ID_EVENTO");
        token = PreferenciasManager.ObtenerPreferencia(PreferenciasManager.TOKEN, this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEventos);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(Eventos.this);
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settingeventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings_add_user) {
            AñadrirParticipante();
        }

        return super.onOptionsItemSelected(item);
    }

    public void AñadrirParticipante() {
        Intent intent = new Intent(this, Evento_invitar.class);
        intent.putExtra("ID_EVENTO", id_evento);
        startActivity(intent);
        this.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new Evento_detalles().newInstance(id_evento, token);
            }
            else {
                return new Evento_participantes().newInstance(id_evento, token);
            }

            /*
            switch (position) {
                case 0:
                    return new Evento_detalles().newInstance(id_evento, token);
                case 1:
                    return new Evento_participantes().newInstance(id_evento, token);
                case 2:
                    return new Evento_localizacion().newInstance(id_evento, token);
            }
            return null;
            */
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detalles";
                case 1:
                    return "Participantes";
            }
            return null;
        }
    }
}
