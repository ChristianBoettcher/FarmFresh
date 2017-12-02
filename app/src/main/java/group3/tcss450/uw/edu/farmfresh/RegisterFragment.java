package group3.tcss450.uw.edu.farmfresh;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * Register fragment that gold RegisterFragment page
 * where user enters their credentials and registers.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Creates and returns View of RegisterFragment and initializes
     * submit button.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return RegisterFragment View.
     */
    @SuppressWarnings("JavaDoc")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        Button submit = (Button) v.findViewById(R.id.register_submit);
        submit.setOnClickListener(this);

        return v;
    }

    /**
     * Manages submit button of RegistrationFragment.
     * @param view View of RegistrationFragment.
     */
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.register_submit:
                    mListener.goPin();
                    //Submit returns to Login page.
                    break;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Calls corresponding method from LoginActivity.
     */
    public interface OnFragmentInteractionListener {

        void goPin();

    }

}
