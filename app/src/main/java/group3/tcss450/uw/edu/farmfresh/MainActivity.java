package group3.tcss450.uw.edu.farmfresh;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener, PinFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void goRegister() {
        RegisterFragment rf = new RegisterFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, rf)
                .addToBackStack(null);
        transaction.commit();

    }

    /*
    Submit registration and will return user to login page.
     */
    @Override
    public void goSubmit() {
        LoginFragment lf = new LoginFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, lf)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goPin() {
        PinFragment pf = new PinFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction().replace(R.id.fragmentContainer, pf)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
