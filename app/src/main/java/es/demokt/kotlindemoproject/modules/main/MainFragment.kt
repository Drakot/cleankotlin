package es.demokt.kotlindemoproject.modules.main


import android.support.v4.app.Fragment
import android.view.View
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.base.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class MainFragment : BaseFragment() {


  override fun onCreateViewId(): Int {
    return R.layout.fragment_start
  }

  override fun viewCreated(view: View?) {

  }

}

