package es.demokt.kotlindemoproject.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.modules.base.BaseActivity
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.home.HomeFragment
import es.demokt.kotlindemoproject.modules.login.LoginFragment
import es.demokt.kotlindemoproject.modules.main.AddProductFragment
import es.demokt.kotlindemoproject.modules.main.AddSupplierFragment
import es.demokt.kotlindemoproject.modules.main.MainFragment
import es.demokt.kotlindemoproject.modules.main.RegisterFragment
import es.demokt.kotlindemoproject.modules.product.result.ProductResultsFragment
import es.demokt.kotlindemoproject.modules.scan.ScanProductFragment
import es.demokt.kotlindemoproject.modules.store.SearchStoreFragment
import es.demokt.kotlindemoproject.modules.store.SupplierFragment
import java.io.Serializable

/**
 * Created by aluengo on 31/1/18.
 */

class Navigator {

  private val LOG_TAG = Navigator::class.java.simpleName
  private var intentToLaunch: Intent? = null
  private lateinit var activity: BaseActivity
  private var extras: Bundle? = null
  private var finishCurrent: Boolean = false
  private var addBackStack: Boolean = false
  private var animated = true
  private var preserve: Boolean = false
  private var hasTransition = false
  private var configurator: BaseFragment.Configurator? = null
  private var newTaskFlag = false
  private var transition = Transition()
  private var targetFragment: BaseFragment? = null
  private var targetCode: Int = 0

  constructor(baseActivity: BaseActivity) {
    intentToLaunch = Intent()
    this.activity = baseActivity
  }

  constructor() {
    intentToLaunch = Intent()
  }

  fun getExtras(): Bundle? {
    Log.d(LOG_TAG, extras.toString())
    return extras
  }

  fun setExtras(extras: Bundle): Navigator {
    this.extras = extras
    addExtrasToIntent()
    return this
  }

  fun configurator(configurator: BaseFragment.Configurator): Navigator {
    this.configurator = configurator
    return this
  }

  fun clearExtras(): Navigator {
    if (extras != null) extras!!.clear()
    if (intentToLaunch != null) {
      intentToLaunch!!.replaceExtras(Bundle())
    }
    return this
  }

  private fun navigate(context: Context?) {

    if (!animated && context != null && context is Activity) {
      context.overridePendingTransition(0, 0)
    }

    if (context != null) {
      if (newTaskFlag) intentToLaunch!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      if (!animated) intentToLaunch!!.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
      context.startActivity(intentToLaunch)
    }

    if (finishCurrent && context != null && context is Activity) {

      context.finish()
    }
    extras = null
  }

  fun navigate(
    context: Context?,
    cls: Class<*>
  ) {
    if (context != null) {
      intentToLaunch!!.setClass(context, cls)
      navigate(context)
    }
  }

  private fun navigate(
    context: Context?,
    cleanStack: Boolean = false
  ) {

    if (!animated && context != null && context is Activity) {
      context.overridePendingTransition(0, 0)
    }

    if (context != null) {
      if (cleanStack) intentToLaunch!!.addFlags(
          Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      )
      if (newTaskFlag) intentToLaunch!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      if (!animated) intentToLaunch!!.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
      context.startActivity(intentToLaunch)
    }

    if (finishCurrent && context != null && context is Activity) {

      context.finish()
    }
    extras = null
  }

  private fun addExtrasToIntent() {
    if (extras != null && !extras!!.isEmpty) {
      intentToLaunch!!.putExtras(extras!!)
    }
  }

  fun finishCurrent(finishCurrent: Boolean): Navigator {
    this.finishCurrent = finishCurrent
    return this
  }

  fun addExtra(
    key: String,
    value: String
  ): Navigator {
    if (extras == null) {
      extras = Bundle()
    }
    extras!!.putString(key, value)
    addExtrasToIntent()
    return this
  }

  fun addExtra(
    key: String,
    value: Boolean
  ): Navigator {
    if (extras == null) {
      extras = Bundle()
    }
    extras!!.putBoolean(key, value)
    addExtrasToIntent()
    return this
  }

  fun addExtra(
    key: String,
    value: Serializable
  ): Navigator {
    if (extras == null) {
      extras = Bundle()
    }
    extras!!.putSerializable(key, value)
    addExtrasToIntent()
    return this
  }

  fun newTaskFlag(newTaskFlag: Boolean): Navigator {
    this.newTaskFlag = newTaskFlag
    return this
  }

  fun addExtra(
    key: String,
    value: Int
  ): Navigator {
    if (extras == null) {
      extras = Bundle()
    }
    extras!!.putInt(key, value)
    addExtrasToIntent()
    return this
  }

  /*
  fun addExtra(key: String, value: Int): Navigator {
        if (extras != null) {
            extras!!.putInt(key, value)
        } else {
            extras = Bundle()
            addExtra(key, value)
        }
        return this
    }
    */

  fun addBackStack(addBackStack: Boolean): Navigator {
    this.addBackStack = addBackStack
    return this
  }

  /**
   * Only works for Activities
   */
  fun animated(animated: Boolean): Navigator {
    this.animated = animated
    return this
  }

  fun preserve(preserve: Boolean): Navigator {
    this.preserve = preserve
    return this
  }

  fun navigate(
    activity: AppCompatActivity?,
    fragmentClass: Class<out BaseFragment>,
    params: Bundle?
  ) {
    if (activity != null) navigate(activity, fragmentClass, addBackStack)
  }

  fun navigate(
    activity: AppCompatActivity,
    clazz: Class<out BaseFragment>
  ) {
    navigate(activity, clazz, null)
  }

  fun goBack() {
    activity.supportFragmentManager.popBackStack()
  }

  fun navigate(clazz: Class<out BaseFragment>) {
    navigate(activity, clazz, extras)
  }

  fun hasTransition(hasTransition: Boolean): Navigator {
    this.hasTransition = hasTransition
    return this
  }

  fun navigateToHome() {
    navigate(activity, HomeFragment::class.java)
  }

  fun navigateToProductResult() {
    navigate(activity, ProductResultsFragment::class.java)
  }

  fun navigateToLogin() {
    navigate(activity, LoginFragment::class.java)
  }

  fun navigateToRegister() {
    navigate(activity, RegisterFragment::class.java, true)
  }

  fun navigateToStartView() {
    navigate(activity, MainFragment::class.java)
  }

  fun navigateToSearchStores() {
    navigate(activity, SearchStoreFragment::class.java)
  }

  fun navigateScanProduct() {
    navigate(activity, ScanProductFragment::class.java)
  }

  fun navigateToAddProduct() {
    navigate(activity, AddProductFragment::class.java)
  }

  fun navigateToAddSupplier() {
    navigate(activity, AddSupplierFragment::class.java)
  }

  fun navigateToSupplier() {
    navigate(activity, SupplierFragment::class.java)
  }

  fun navigate(
    context: Context?,
    cls: Class<*>,
    cleanStack: Boolean = false
  ) {
    if (context != null) {
      intentToLaunch!!.setClass(context, cls)
      if (cleanStack)
        navigate(context, cleanStack)
      else
        navigate(context)
    }
  }

  fun navigate(
    activity: AppCompatActivity,
    fragmentClass: Class<out BaseFragment>,
    addBackStack: Boolean
  ) {
    val fragmentManager = activity.supportFragmentManager
    val trans = fragmentManager.beginTransaction()
    try {
      var fragment: BaseFragment = fragmentClass.newInstance()
      val tag = fragment.getFragmentTag()
      val oldFragment = fragmentManager.findFragmentByTag(tag)

      if (oldFragment != null && !preserve) {
        fragment = oldFragment as BaseFragment
      } else {
        fragment.configurator = configurator
        fragment.arguments = extras
      }
      if (targetFragment != null) {
        fragment.setTargetFragment(targetFragment, targetCode)
      }
      trans.replace(R.id.fragment_container, fragment, tag)

      //activity.replaceFragment(fragment, R.id.fragment_container, tag)

      if (addBackStack) {
        trans.addToBackStack(null)
      }

      //trans.commit()
      trans.commitAllowingStateLoss()
      this.preserve = false
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  fun navigate(
    f: Fragment,
    cleanStack: Boolean = false,
    addBackStack: Boolean = false
  ) {
    val ft = activity.supportFragmentManager.beginTransaction()
    if (cleanStack) {
      clearBackStack()
    }
    setUpTransition(ft)
    ft.replace(R.id.fragment_container, f)
    if (addBackStack) {
      ft.addToBackStack(null)
    }
    ft.commit()
  }

  private fun findCurrentFragment(): Fragment? {
    val fragmentManager = activity.supportFragmentManager
    var currentFragment: Fragment? = fragmentManager.fragments[fragmentManager.fragments.size - 1]
    if (currentFragment == null) {
      for (fragment in fragmentManager.fragments) {
        if (fragment != null && fragment.isVisible) {
          currentFragment = fragment
          break
        }
      }
    }
    return currentFragment
  }

  /*  fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String) {
        supportFragmentManager.inTransaction { add(frameId, fragment, tag) }
    }*/

  fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    tag: String
  ) {
    supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }
  }

  inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func()
        .commit()
  }

  fun setUpTransition(transaction: FragmentTransaction) {
    if (hasTransition) {
      if (transition.transitionMode === Transition.TransitionMode.LEFT_TO_RIGHT) {
        transaction.setCustomAnimations(
            R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
            R.anim.exit_to_right
        )
      } else if (transition.transitionMode === Transition.TransitionMode.DOWN_TO_UP) {
        transaction.setCustomAnimations(
            R.anim.enter_from_down, R.anim.exit_to_down, R.anim.enter_from_up,
            R.anim.exit_to_up
        )
      }
    }
  }

  fun clearBackStack(): Navigator {
    val manager = activity.supportFragmentManager
    if (manager.backStackEntryCount > 0) {
      val first = manager.getBackStackEntryAt(0)
      manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
    return this
  }

  fun target(fragment: BaseFragment, code: Int): Navigator {
    this.targetFragment = fragment
    this.targetCode = code
    return this

  }

  fun clearTarget(): Navigator {
    this.targetCode = 0
    this.targetFragment = null
    return this
  }

}