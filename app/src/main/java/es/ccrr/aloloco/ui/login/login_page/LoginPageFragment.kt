package es.ccrr.aloloco.ui.login.login_page

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import es.ccrr.aloloco.R
import es.ccrr.aloloco.ui.base.BaseActivity
import es.ccrr.aloloco.ui.base.BaseFragment
import es.ccrr.aloloco.ui.login.LoginActivity
import es.ccrr.aloloco.util.Util
import kotlinx.android.synthetic.main.login_page.*

class LoginPageFragment : BaseFragment(), LoginPageContract.LoginPageView, LoginActivity.LoginPagePagerContract {

    private lateinit var presenter: LoginPagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = LoginPagePresenter(this, arguments, activity as BaseActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { activity ->
            // FireBase BugFixing: RuntimeException - ActivityThread.performLaunchActivity(Attempt to invoke interface method 'android.view.Display android.view.WindowManager.getDefaultDisplay()' on a null object reference Util.java:203; FilterItemListFragment.java:87)
            val screenWidth = presenter.getScreenWidth()
            if (screenWidth == 0)
                activity.onBackPressed()

            if (!presenter.isLoginLayoutShown())
                forgotPasswordView.visibility = View.GONE
                // Dependencies.trackScreen("join_screen")
            else
                forgotPasswordView.visibility = View.VISIBLE
                // Dependencies.trackScreen("login_screen")


            if (!TextUtils.isEmpty(presenter.getEmail()))
                emailView.setText(presenter.getEmail())

            loginBtn.setOnClickListener {
                if (Util.isOnline(activity)) {
                    showProgressDialog()
                    presenter.doAPICall(arrayOf(emailView.getText(), passwordView.getText()))
                } else {
                    showAlert("Hold Up", activity.getString(R.string.error_offline), "Ok", "", true)
                }
            }
        }
    }


    /* Listeners */
    override fun getData(): String {
        return presenter.getEmail()
    }

    override fun updateData(value: String) {

        if (!TextUtils.isEmpty(value)) {
            emailView.setText(value)
            presenter.setEmail(value)
        }
    }

    /* Getters & Setters */
    override fun getWindowManager(): WindowManager? {
        return activity?.windowManager
    }


    companion object {

        fun newInstance(page: String, email: String = ""): LoginPageFragment {
            val fragment = LoginPageFragment()

            val bundle = Bundle()
            bundle.putString("page", page)
            bundle.putString("email", email)
            fragment.arguments = bundle

            return fragment
        }
    }
}