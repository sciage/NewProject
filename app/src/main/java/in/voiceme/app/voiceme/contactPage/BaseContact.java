package in.voiceme.app.voiceme.contactPage;

import android.content.Intent;
import android.os.Bundle;

import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;

public class BaseContact extends BaseActivity {
    protected String usedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!application.getAuth().getUser().isLoggedIn()) {
            if (MySharedPreferences.getContactSent(preferences) == null) {
                Intent intent = new Intent(this, ContactsActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(this, ContactListActivity.class));
            }
            finish();
            return;
        }
    }
}
