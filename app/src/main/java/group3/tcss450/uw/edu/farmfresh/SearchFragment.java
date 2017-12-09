package group3.tcss450.uw.edu.farmfresh;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group3.tcss450.uw.edu.farmfresh.handler.GetAPIDetailsAsync;
import group3.tcss450.uw.edu.farmfresh.sqlite.ListDB;
import group3.tcss450.uw.edu.farmfresh.sqlite.ListEntry;


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

    private ArrayList<String> marketList;
    private HashMap<String, Integer> marketMap;
    private ListView lv;
    private OnFragmentInteractionListener mListener;
    private ListDB mMarketDB;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        marketList = new ArrayList<String>();
        marketMap = new HashMap<String, Integer>();
        if (getArguments() != null) {


            if (mMarketDB == null) {
                mMarketDB = new ListDB(getActivity());
            }

            List<ListEntry> marketEntries = mMarketDB.getList();
            marketList = getArguments().getStringArrayList(getString(R.string.MARKET_LIST));
            marketMap = (HashMap<String,Integer>)
                    getArguments().getSerializable(getString(R.string.MARKET_MAP));

            /*
            for (ListEntry market : marketEntries) {
                marketList.add(market.getMarketName());
                marketMap.put(market.getMarketName(), market.getMarketId());
                Log.d("MARKET LIST TEST", market.getMarketName());
            }
            */

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    R.layout.list_view_layout, R.id.custom_text_view, marketList);

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String marketname = (String) lv.getItemAtPosition(position);
                    String marketid = marketMap.get(marketname).toString();
                    Log.d("YOU SELECTED ITEM: " + marketname, "YOU SELECTED ITEM: " + marketid);

                    //SearchActivity.this.setContentView(R.layout.fragment_farm_details);

                    ArrayList<String> detailList = new ArrayList<>();

                    GetAPIDetailsAsync detailsApiTask = new GetAPIDetailsAsync((SearchActivity)
                            getActivity());
                    detailsApiTask.execute(marketid);


                    FarmDetailsFragment fdg = new FarmDetailsFragment();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_container, fdg)
                            .addToBackStack(null)
                            .commit();

                /*
                GetAPIDetailsAsync detailsApiTask = new GetAPIDetailsAsync(detailsAdapter, detailsItemList);
                detailsApiTask.execute(marketid);*/

                }
            });

        }
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

        lv = (ListView) v.findViewById(R.id.search_list);

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
