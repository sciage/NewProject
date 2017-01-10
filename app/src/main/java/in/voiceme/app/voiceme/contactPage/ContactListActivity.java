package in.voiceme.app.voiceme.contactPage;

import android.os.Bundle;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class ContactListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setTitle("Home");
        setNavDrawer(new MainNavDrawer(this));
    }

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
