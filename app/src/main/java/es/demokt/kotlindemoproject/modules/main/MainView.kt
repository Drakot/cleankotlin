package es.demokt.kotlindemoproject.modules.main

import es.demokt.kotlindemoproject.modules.base.main.MainMVP

/**
 * Created by aluengo on 12/4/18.
 */
interface MainView : MainMVP.View {
  fun goToHome()
  fun goToLogin()
}