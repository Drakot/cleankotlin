package es.demokt.kotlindemoproject.data.network.login

import es.demokt.kotlindemoproject.data.network.login.LoginRequest.LoginType.EMAIL

/**
 * Created by aluengo on 1/2/18.
 */
class LoginRequest() {

  var email: String = ""
  var password: String = ""
  var type: LoginType = LoginType.EMAIL

  enum class LoginType {
    EMAIL,
    GOOGLE,
    FACEBOOK
  }

  constructor(
    email: String,
    password: String,
    type: LoginType = EMAIL
  ) : this() {
    this.email = email
    this.password = password
    this.type = type
  }

}

