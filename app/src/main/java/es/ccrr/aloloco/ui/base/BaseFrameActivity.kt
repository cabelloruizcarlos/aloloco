package es.ccrr.aloloco.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import es.ccrr.aloloco.R

public abstract class BaseFrameActivity<FirstFragment: Fragment>: BaseActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        if (savedInstanceState != null) {
            return
        }
        loadFirstFragment()
    }

    protected fun getLayout(): Int {
        return R.layout.frame
    }

    protected fun loadFirstFragment() {
        val firstFragment = getFirstFragment() ?: return

        supportFragmentManager.beginTransaction().add(getFrameId(), firstFragment).commit()
    }

    protected abstract fun getFirstFragment(): FirstFragment

    private fun getFrameId(): Int { return R.id.frame }

    override fun onBackPressed() {
        if (performFragmentBackButton()) {
            return
        }
        super.onBackPressed()
    }

    private fun performFragmentBackButton(): Boolean {
        val fragment = supportFragmentManager.findFragmentById(getFrameId())
        if (fragment is BackButtonOverride) {
            if ((fragment as BackButtonOverride).onBackPressed()) {
                return true
            }
        }
        return false
    }
}