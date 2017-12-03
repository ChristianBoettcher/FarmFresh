package group3.tcss450.uw.edu.farmfresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


/**
 * SearchFragment that hold SearchFragment page where
 * user can type their zip code and start searching for
 * markets near that zip code.
 *
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment implements View.OnClickListener,
    android.widget.AdapterView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Creates View of SearchFragment and initializes submit button.
     * @return View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        Button submit = (Button) v.findViewById(R.id.search_button);
        submit.setOnClickListener(this);

        ListView lv = (ListView) v.findViewById(R.id.search_list);
        lv.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * Handles click on search by zip button.
     * @param v View of SearchFragment.
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.search_button:
                    mListener.searchZip();
                    //Search Zipcode.
                    break;
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * Calls corresponding method from SearchActivity.
     */
    public interface OnFragmentInteractionListener {
        void searchZip();

    }
}
