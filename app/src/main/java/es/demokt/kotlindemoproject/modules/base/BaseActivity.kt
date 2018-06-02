package es.demokt.kotlindemoproject.modules.base

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.utils.Navigator
import es.demokt.kotlindemoproject.utils.errorManager
import es.demokt.kotlindemoproject.utils.message.MessageFactory
import kotlinx.android.synthetic.main.activity_main.main_toolbar
import kotlinx.android.synthetic.main.toolbar.toolbar_title

abstract class BaseActivity : AppCompatActivity(), MainMVP.Presenter {

  var LOG_TAG = BaseActivity::class.java.simpleName
  fun getTag(): String {
    return this.javaClass.simpleName
  }

  val navigator: Navigator by lazy { Navigator(this) }
  var toolbar: Toolbar? = null
  val messageFactory: MessageFactory by lazy { MessageFactory(this) }

  protected abstract fun onCreateViewId(): Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LOG_TAG = getTag()

    val layoutId = onCreateViewId()
    if (layoutId != 0) {
      setContentView(layoutId)
    }

    if (main_toolbar != null) {
      toolbar = main_toolbar as Toolbar?
      setSupportActionBar(toolbar)
      supportActionBar!!.setDisplayShowTitleEnabled(false)
      toolbar!!.setNavigationOnClickListener({ onBackPressed() })

      toolbar!!.title = ""
    }
  }

  fun setBackButtonColor() {
    toolbar?.navigationIcon?.setTint(resources.getColor(R.color.white))
  }

  fun setToolbarColor(toolbarColor: Int) {
    val actionBar = supportActionBar
    if (actionBar != null && toolbarColor != 0) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(toolbarColor)))
      } else {
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(toolbarColor)))
      }
    }
  }

  fun setToolbartTextColor(titleColor: Int) {
    toolbar?.setTitleTextColor(resources.getColor(titleColor))
  }

  fun enableToolbarBackButton(enable: Boolean) {
    val actionBar = supportActionBar
    if (enable) {
      actionBar!!.setDisplayHomeAsUpEnabled(true)
      actionBar.setDisplayShowHomeEnabled(true)
    } else {
      actionBar!!.setDisplayHomeAsUpEnabled(false)
      actionBar.setDisplayShowHomeEnabled(false)
    }
  }

  fun willConsumeBackPressed(): Boolean {
    var willConsumeBackPressed = false
    val fragments = supportFragmentManager.fragments
    if (fragments != null) {
      for (i in fragments.indices) {
        val fragment = fragments[i]
        if (fragment is BaseFragment) {
          if (fragment.willConsumeBackPressed()) {
            willConsumeBackPressed = true
            break
          }
        }
      }
    }

    return willConsumeBackPressed
  }

  override fun onBackPressed() {
    if (!willConsumeBackPressed()) {
      super.onBackPressed()
    }
  }

  override fun showMessage(message: String) {
    messageFactory.getDefaultMessage(message)
        .init()
        .show()
  }

  override fun showError(message: String) {
    messageFactory.getDefaultErrorMessage(message)
        .init()
        .show()
  }

  override fun goToLogin() {
    navigator
        .navigateToLogin()
  }

  override fun hideKeyBoard() {
    val view = this.currentFocus
    hideKeyboard(view)
  }

  fun hideKeyboard(view: View?) {
    if (view != null) {
      val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
  }

  fun error(error: ErrorReponse) {
    errorManager(error)
  }

  fun setToolbarText(title: String?) {
    if (toolbar_title != null && title != null) {
      toolbar_title.visibility = View.VISIBLE
      toolbar_title.text = title
    }
  }

  open fun onPermissionAccepted() {

  }

  fun checkPermissionCamera() {
    if (!Dexter.isRequestOngoing()) {
      Dexter.checkPermission(object : PermissionListener {
        override fun onPermissionGranted(response: PermissionGrantedResponse) {
          onPermissionAccepted()
        }

        override fun onPermissionDenied(response: PermissionDeniedResponse) {

        }

        override fun onPermissionRationaleShouldBeShown(
          permission: PermissionRequest,
          token: PermissionToken
        ) {
          AlertDialog.Builder(this@BaseActivity)
              .setTitle(
                  R.string.permission_rationale_title
              )
              .setMessage(R.string.permission_rationale_message)
              .setNegativeButton(android.R.string.cancel) { dialog, which ->
                dialog.dismiss()
                token.cancelPermissionRequest()
              }
              .setPositiveButton(android.R.string.ok) { dialog, which ->
                dialog.dismiss()
                token.continuePermissionRequest()
              }
              .setCancelable(false)
              .show()
        }
      }, Manifest.permission.CAMERA)
    }
  }
}
