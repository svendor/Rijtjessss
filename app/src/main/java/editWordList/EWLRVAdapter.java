package editWordList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.absontwikkeling.rijtjes.R;

import java.util.Collections;
import java.util.List;

public class EWLRVAdapter extends RecyclerView.Adapter<EWLRVAdapter.ViewHolder> {

    private editWordList editWordList;
    private LayoutInflater inflater;
    List<item_word_to_word> data = Collections.emptyList();

    public EWLRVAdapter(Context ctx, List<item_word_to_word> dataIn, editWordList editWordList) {
        inflater = LayoutInflater.from(ctx);
        this.editWordList = editWordList;
        this.data = dataIn;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view_item_edit_word_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item_word_to_word row_from_data = data.get(position);
        holder.wordLan1.setText(row_from_data.wordLan1);
        holder.wordLan2.setText(row_from_data.wordLan2);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        EditText wordLan1;
        EditText wordLan2;

        public ViewHolder(View itemView) {
            super(itemView);
            wordLan1 = itemView.findViewById(R.id.wordLan1);
            wordLan2 = itemView.findViewById(R.id.wordLan2);
        }
    }

}
