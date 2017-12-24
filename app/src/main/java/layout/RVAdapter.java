package layout;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.absontwikkeling.rijtjes.R;

import java.util.Collections;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private displayList dList;
    private LayoutInflater inflater;
    List<item_data> data = Collections.emptyList();

    public RVAdapter(Context ctx, List<item_data> dataIn, displayList dList) {
        inflater = LayoutInflater.from(ctx);
        this.data = dataIn;
        this.dList = dList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item_data rowFromData = data.get(position);
        long id = rowFromData.id;
        String langToLangString = rowFromData.language1 + " - " + rowFromData.language2;
        holder.listName.setText(rowFromData.listName);
        holder.languageToLanguage.setText(langToLangString);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView listName;
        TextView languageToLanguage;
        ImageView editButton;
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            languageToLanguage = itemView.findViewById(R.id.languagueToLanguage);
            listName = itemView.findViewById(R.id.list_name);
            constraintLayout = itemView.findViewById(R.id.recycler_item_view_container);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dList.questionTheList(getAdapterPosition(), data);
                }
            });
            editButton = itemView.findViewById(R.id.editButton);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dList.editList(getAdapterPosition(), data);
                }
            });
        }
    }
}
