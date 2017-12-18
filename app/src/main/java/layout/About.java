package layout;


import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.absontwikkeling.rijtjes.BuildConfig;
import com.absontwikkeling.rijtjes.R;

import org.w3c.dom.Text;

import java.text.CollationElementIterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment {


    public About() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView versionInfo = (TextView) view.findViewById(R.id.versionInfo);

        String stringCodeText = getResources().getString(R.string.versionCode);
        String stringNameText = getResources().getString(R.string.versionName);

        versionInfo.setText(stringNameText + BuildConfig.VERSION_NAME + "; " +  stringCodeText + BuildConfig.VERSION_CODE);

        return view;
    }

}
