package es.demokt.kotlindemoproject.modules.main

import android.os.Bundle
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.base.BaseActivity

/**
 * MainActivity
 * Created by aluengo on 31/1/18.
 */
class MainActivity : BaseActivity(), MainView {
  private val presenter: MainPresenter by lazy { MainPresenter(this) }

  override fun onCreateViewId(): Int {
    return R.layout.activity_main
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    presenter.init()
  }

  override fun goToHome() {
    navigator.finishCurrent(true)
        .navigateToHome()
  }

}
