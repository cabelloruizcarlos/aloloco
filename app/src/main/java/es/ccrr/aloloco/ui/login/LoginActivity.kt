package es.ccrr.aloloco.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import es.ccrr.aloloco.R
import es.ccrr.aloloco.ui.main.MainActivity
import es.ccrr.aloloco.ui.base.BaseActivity
import es.ccrr.aloloco.ui.login.login_page.LoginPageFragment
import es.ccrr.aloloco.engine.util.Constants
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginActivityContract.LoginActivityView {

    private lateinit var presenter: LoginActivityPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Carlos: To avoid the keyboard to be shown automatically because of the EditTexts
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        setContentView(R.layout.activity_login)

        presenter = LoginActivityPresenter(this, intent)
    }


    override fun setLayout(isLoginLayoutShown: Boolean) {

        val adapter = LoginTabsAdapter(supportFragmentManager)
        mPager.adapter = adapter

//        val typeFace = Typeface.createFromAsset(assets, "fonts/Jaapokki-Regular.otf")
//        mTabs.setTypeface(typeFace, 0)
        mTabs.setAllCaps(true)
        mTabs.setTextSize(applicationContext.resources.getDimension(R.dimen.login_tab_text_size).toInt())
        mTabs.setShouldExpand(true)
        mTabs.setViewPager(mPager, -1.0f, -1.0f)

        if (isLoginLayoutShown)
            mPager.currentItem = 1
        else
            mPager.currentItem = 0

        mPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                val joinFragment = adapter.instantiateItem(mPager, 0) as LoginPagePagerContract
                val loginFragment = adapter.instantiateItem(mPager, 1) as LoginPagePagerContract
                when (position) {
                    0 -> {
                        val userData = loginFragment.getData()
                        joinFragment.updateData(userData)
                    }
                    1 -> {
                        val userData = joinFragment.getData()
                        loginFragment.updateData(userData)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

//        mCloseBtn.setOnClickListener { presenter.onClicked(R.id.mCloseBtn) }
        mTerms.setOnClickListener { presenter.onClicked(R.id.mTerms) }
        mPolicy.setOnClickListener { presenter.onClicked(R.id.mPolicy) }
    }


    override fun onBackPressed() {
        goToNextScreen()
    }


    override fun loadUrl(url: String) {
        launchBrowser(url)
    }


    override fun goToNextScreen() {

        // TODO: I need to add a Progress Dialog into BaseActivity
        // dismissProgressDialog()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)
        finish()
    }


    class LoginTabsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getPageTitle(position: Int): CharSequence? {
            return Constants.LOGIN_TABS[position]
        }

        override fun getCount(): Int {
            return Constants.LOGIN_TABS.size
        }

        override fun getItem(position: Int): Fragment {
            return LoginPageFragment.newInstance(Constants.LOGIN_TABS[position])
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }
    }


    interface LoginPagePagerContract {
        fun getData(): String
        fun updateData(value: String)
    }
}
