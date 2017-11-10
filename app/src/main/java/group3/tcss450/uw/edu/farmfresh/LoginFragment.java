package group3.tcss450.uw.edu.farmfresh;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Login Fragment that holds Login Fragment page.
 * Initial page of the app.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes LoginFragment:
     * email editText;
     * password editText;
     * Login, Register, Forgot password buttons.
     * If user have just registered then email
     * text field will already have user's email.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            String user = getArguments().getString(getString(R.string.email_key));
            EditText email = (EditText) getActivity().findViewById(R.id.login_email);
            email.setText(user);
            Toast.makeText(getActivity(),
                    "You have successfully registered. ",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes view and buttons of loginFragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = (Button) v.findViewById(R.id.login_button);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.register_button);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.forgot_button);
        b.setOnClickListener(this);

        return v;
    }

    /**
     * Manages clicks on 3 buttons:
     * Login, Register, Forgot Password.
     *
     */
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.register_button:
                    mListener.goRegister();
                    break;
                case R.id.login_button:
                    /*
                    User has logged in or typed incorrect email/password.
                     */
                    mListener.loginManager();
                    break;
                case R.id.forgot_button:
                    /*
                    Go to password reset page.
                     */
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
                    + " must implement OnFragmentInterationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Calls corresponding methods from MainActivity.
     */
    public interface  OnFragmentInteractionListener {
        void goRegister();

        void loginManager();

    }

}
