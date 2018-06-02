package es.demokt.kotlindemoproject.modules.login

import android.support.v4.app.Fragment
import android.view.View
import es.demokt.kotlindemoproject.BuildConfig
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.main.LoginPresenter
import es.demokt.kotlindemoproject.modules.main.RegisterView
import kotlinx.android.synthetic.main.fragment_login.btOk
import kotlinx.android.synthetic.main.fragment_login.etEmail
import kotlinx.android.synthetic.main.fragment_login.etPassword

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment(), RegisterView {
  private val presenter: LoginPresenter by lazy { LoginPresenter(this) }
  override fun goToHome() {
    getNavigator().finishCurrent(true)
        .navigateToHome()
  }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_login
  }

  override fun viewCreated(view: View?) {
    btOk.setOnClickListener {
      //prefill()
      presenter.okClicked(etEmail.text.toString(), etPassword.text.toString())
    }

   /* btRegister.setOnClickListener {
      getNavigator()
          .navigateToRegister()
    }*/
  }

  private fun prefill() {
    if (BuildConfig.DEBUG && etEmail.text.toString().isEmpty()) {
      etEmail.setText("andrey@demokt.es")
      etPassword.setText("bEdt*766")
    }
  }

  override fun hasToolbar(): Boolean {
    return false
  }

}

