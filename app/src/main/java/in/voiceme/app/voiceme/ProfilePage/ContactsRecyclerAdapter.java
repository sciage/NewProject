package in.voiceme.app.voiceme.ProfilePage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.voiceme.app.voiceme.R;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<String> data;
  private static final int ITEM = 1;

  public ContactsRecyclerAdapter(ArrayList<String> data) {
    this.data = data;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View view = layoutInflater.inflate(R.layout.cell_contacts, parent, false);
      return new ContactsRecyclerAdapter.VHItem(view);
    }

    throw new RuntimeException("NO VIEW TYPE FOUND");
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == ITEM) {
      ContactsRecyclerAdapter.VHItem item = (ContactsRecyclerAdapter.VHItem) holder;
      item.contact.setText(position + ") " + data.get(position));
    }
  }

  @Override public int getItemCount() {
    return data.size();
  }

  @Override public int getItemViewType(int position) {
    return ITEM;
  }

  private class VHItem extends RecyclerView.ViewHolder {

    private View itemView;
    private TextView contact;

    VHItem(View itemView) {
      super(itemView);
      this.itemView = itemView;
      contact = (TextView) itemView.findViewById(R.id.txt_contact);
    }
  }
}
