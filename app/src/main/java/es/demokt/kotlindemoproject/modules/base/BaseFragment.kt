package es.demokt.kotlindemoproject.modules.base

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import es.demokt.kotlindemoproject.utils.ProgressDialogFragment

/**
 * BaseFragment
 * Created by aluengo on 1/2/18.
 */
abstract class BaseFragment : Fragment(), MainMVP.View {
  val LOG_TAG = BaseFragment::class.java.simpleName
  val progress: ProgressDialogFragment by lazy {
    ProgressDialogFragment.newInstance(
        R.string.loading, true, false
    )
  }

  protected abstract fun onCreateViewId(): Int
  protected abstract fun viewCreated(view: View?)

  class Configurator {
    var toolbarColor: Int = 0
      internal set
    var isShowOnlyToolbarTitle: Boolean = false
      internal set
    var titleColor: Int = 0
      internal set
    var title: String? = null
      internal set
    var isHasToolbar: Boolean = false
      internal set
    var isHasToolbarBackButton: Boolean = false
      internal set
    var hasOwnToolbar: Boolean = false

    fun toolbarColor(toolbarColor: Int): Configurator {
      this.toolbarColor = toolbarColor
      return this
    }

    fun showOnlyToolbarTitle(showOnlyToolbarTitle: Boolean): Configurator {
      this.isShowOnlyToolbarTitle = showOnlyToolbarTitle
      return this
    }

    fun titleColor(titleColor: Int): Configurator {
      this.titleColor = titleColor
      return this
    }

    fun title(title: String?): Configurator {
      this.title = title
      return this
    }

    fun hasToolbar(hasToolbar: Boolean): Configurator {
      this.isHasToolbar = hasToolbar
      return this
    }

    fun hasToolbarBackButton(hasToolbarBackButton: Boolean): Configurator {
      this.isHasToolbarBackButton = hasToolbarBackButton
      return this
    }

    fun hasOwnToolbar(hasOwnToolbar: Boolean): Configurator {
      this.hasOwnToolbar = hasOwnToolbar
      return this
    }

    fun checkDefaults() {

      if (toolbarColor == 0) {
        toolbarColor = R.color.colorPrimary
      }

      if (titleColor == 0) {
        titleColor = R.color.white
      }
    }

    companion object {

      val instance: Configurator
        get() = Configurator()
    }
  }

  var configurator: Configurator? = null

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    viewCreated(view)
  }

  override fun onResume() {
    super.onResume()
    setUpToolbar()
  }

  fun setToolbarTitle(title: String?) {
    getBaseActivity().setToolbarText(title)
  }

  open fun hasToolbar(): Boolean {
    return true
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (configurator == null) {
      configurator = Configurator.instance
      configurator!!.hasToolbar(hasToolbar())
      configurator!!.hasToolbarBackButton(hasToolbarBackButton())
      configurator!!.showOnlyToolbarTitle(showOnlyToolbarTitle())
      configurator!!.hasOwnToolbar(false)
    }
  }

  fun showOnlyToolbarTitle(): Boolean {
    return false
  }

  fun setUpToolbar() {
    val actionBar = getBaseActivity().supportActionBar
    if (actionBar != null) {
      setToolbarTitle(configurator!!.title)
      configurator!!.checkDefaults()
      getBaseActivity().setToolbartTextColor(configurator!!.titleColor)
      getBaseActivity().setToolbarColor(configurator!!.toolbarColor)
      getBaseActivity().enableToolbarBackButton(configurator!!.isHasToolbarBackButton)
      getBaseActivity().setBackButtonColor()
      if (!configurator!!.isHasToolbar && !configurator!!.hasOwnToolbar) {
        actionBar.hide()
      } else if (!actionBar.isShowing) {
        actionBar.show()
      }
    }
  }

  open fun hasToolbarBackButton(): Boolean {
    return false
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(onCreateViewId(), container, false)
  }

  fun getFragmentTag(): String {
    return this.javaClass.simpleName
  }

  fun getBaseActivity(): BaseActivity {
    return super.getActivity() as BaseActivity
  }

  fun getNavigator(): Navigator {
    return getBaseActivity().navigator
  }

  fun willConsumeBackPressed(): Boolean {
    return false
  }

  override fun checkError(error: ErrorReponse) {
    getBaseActivity().error(error)
  }

  override fun onError(s: String) {
    Log.e(LOG_TAG, s)
  }

  open fun onPermissionAccepted() {

  }

  override fun showProgress(show: Boolean) {
    if (show) {
      this.progress.show(fragmentManager, "progress")
    } else {
      this.progress.dismiss()
    }
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
          AlertDialog.Builder(this@BaseFragment.activity)
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