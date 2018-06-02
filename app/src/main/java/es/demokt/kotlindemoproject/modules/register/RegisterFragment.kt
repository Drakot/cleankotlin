package es.demokt.kotlindemoproject.modules.main

import android.support.v4.app.Fragment
import android.view.View
import es.demokt.kotlindemoproject.BuildConfig
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.login.Register
import kotlinx.android.synthetic.main.fragment_login.btOk
import kotlinx.android.synthetic.main.fragment_login.etEmail
import kotlinx.android.synthetic.main.fragment_login.etPassword
import kotlinx.android.synthetic.main.fragment_register.etName
import kotlinx.android.synthetic.main.fragment_register.etPasswordConfirm
import kotlinx.android.synthetic.main.fragment_register.etSurName

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment(), RegisterView {
  private val presenter: RegisterPresenter by lazy { RegisterPresenter(this) }
  override fun goToHome() {
    getNavigator().navigateToHome()
  }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_register
  }

  override fun viewCreated(view: View?) {
    configurator?.title = getString(R.string.register)
    btOk.setOnClickListener {
      prefill()
      presenter.okClicked(retrieveData())
    }
  }

  override fun hasToolbarBackButton(): Boolean {
    return true
  }

  fun retrieveData(): Register {
    val register = Register()
    register.email = etEmail.text.toString()
    register.password = etPassword.text.toString()
    register.passwordConfirm = etPasswordConfirm.text.toString()
    register.name = etName.text.toString()
    register.surname = etSurName.text.toString()

    return register
  }

  private fun prefill() {
    if (BuildConfig.DEBUG && etEmail.text.toString().isEmpty()) {
      etName.setText("Alberto")
      etSurName.setText("Demo")
      etEmail.setText("dralsoft@gmail.com")
      etPassword.setText("admin")
      etPasswordConfirm.setText("admin")
    }
  }

}

