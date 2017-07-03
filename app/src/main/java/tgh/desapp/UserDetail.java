package tgh.desapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetail extends Activity {

    private CircleImageView img;
    private AppBarLayout app_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        img = (CircleImageView)findViewById(R.id.avatar1);
        app_bar = (AppBarLayout)findViewById(R.id.app_bar);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (appBarLayout.getHeight() / 2 < -verticalOffset) {
                    img.setVisibility(View.GONE);
                } else {
                    img.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
