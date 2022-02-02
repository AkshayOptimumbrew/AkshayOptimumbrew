package askaquestion.com.app.core.constants;

public class ConstantsAPI {

    private static final String BASE_URL = "http://192.168.0.106/ask_question_poll/api/public/api/";
    private static final String BASE_URL2 = "http://192.168.0.104/ask_question_poll/api/public/api/";

    /*API NAME*/

    public static final String URL_LOGIN = BASE_URL + "loginForUser";
    public static final String URL_LOGIN2 = BASE_URL2 + "signIn";
    public static final String URL_VIEW_QUESTION = BASE_URL + "viewQuestions";
    public static final String URL_GET_QUESTION = BASE_URL + "getAllQuestionByUser";
    public static final String URL_UPLOAD_IMAGE = BASE_URL + "uploadImage";
    public static final String URL_ADD_QUESTION = BASE_URL + "addQuestion";
    public static final String URL_CATEGORIES = BASE_URL + "viewCategories";
    public static final String URL_EDIT_PROFILE = BASE_URL + "editProfileForUser";
    public static final String URL_RESET_PASSWORD = BASE_URL + "resetPasswordForUser";
    public static final String URL_REPORT_QUESTION = BASE_URL + "manageReportQuestion";
    public static final String URL_NEW_PASSWORD = BASE_URL + "generateNewPasswordForUser";
    public final static String URL_FORGOT = BASE_URL + "forgotPasswordForUser";
    public final static String URL_VERIFY_OTP = BASE_URL + "verifyOtpForUser";
    public static final String URL_ADD_ANSWER = BASE_URL + "addAnswer";
    public static final String URL_NONE = BASE_URL + "viewQuestionAnalysisNone";
    public static final String URL_SIGNUP = BASE_URL + "signup";
    public static final String URL_SIGNUP2 = BASE_URL2 + "signup";
    public static final String URL_OTP = BASE_URL + "verifyUser";
    public static final String URL_WEBVIEW = "https://stackoverflow.com/";
    public static final String URL_COUNTRY = BASE_URL + "viewQuestionAnalysisCountry";
    public static final String URL_GENDER = BASE_URL + "viewQuestionAnalysisGender";

}
