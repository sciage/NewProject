package in.voiceme.app.voiceme.ProfilePage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import in.voiceme.app.voiceme.R;

public class ContactsActivity extends AppCompatActivity {
    private ContactsRecyclerAdapter recyclerAdapter;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts2);
    data = getIntent().getStringArrayListExtra("contacts");

    initUI();
}

    private void initUI() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerAdapter = new ContactsRecyclerAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
