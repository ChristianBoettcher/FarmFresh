package group3.tcss450.uw.edu.farmfresh.util;

/**
 * Class with links to php requests.
 * Created by Doseon on 11/8/2017.
 */

public class Links {

    // API link to search markets by zipcode.
    public static final String API_LINK
            = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/zipSearch?zip=";

    // Web Service link to register a user.
    public static final String STORE_ACC_URL
            =  "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/register.php";
            //= "http://farmfresh.getenjoyment.net/register.php";

    // Web service link to verify if the username and password match.
    public static final String VERIFY_ACC_URL
            = "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/confirm_info.php";
            //= "http://farmfresh.getenjoyment.net/confirm_info.php";

    // Web service to check if the username exists in the database.
    public static final String CHECK_USER_URL
            = "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/check_user.php?user=";
            //= "http://farmfresh.getenjoyment.net/check_user.php?user=";

    // Web service to send email to user.
    public static final String SEND_EMAIL_URL
            = "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/send_email.php";

    // Web service to confirm the generated pin for the specific account.
    public static final String CONFIRM_PIN_URL
            = "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/confirm_pin.php";

    // Web service to change the password of a current account.
    public static final String CHANGE_PASS_URL
            = "http://cssgate.insttech.washington.edu/~doseon/FarmFresh/change_pass.php";

    // API link to get details of a specific market using the ID.
    public static final String API_DETAILS_LINK
            = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/mktDetail?id=";
}
