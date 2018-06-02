package es.demokt.kotlindemoproject.data.api

import es.demokt.kotlindemoproject.data.api.ErrorReponse.Cancelled
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Error
import es.demokt.kotlindemoproject.data.api.ErrorReponse.InvalidToken
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoConnection
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoData
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoResponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.PasswordNotValid
import es.demokt.kotlindemoproject.data.api.ErrorReponse.StoreNotFound
import es.demokt.kotlindemoproject.data.api.ErrorReponse.SupplierNotFound
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Unauthorized
import es.demokt.kotlindemoproject.data.api.ErrorReponse.WithoutStores

/**
 * Created by aluengo on 12/2/18.
 */
enum class ErrorReponse {
  Error,
  NoData,
  NoConnection,
  InvalidToken,
  NoResponse,
  ServerError,
  Unauthorized,
  Cancelled,
  PasswordNotValid,
  WithoutStores,
  StoreNotFound,
  SupplierNotFound,
  MandatoryFields,
  EmailOrPass
}

object ErrorManager {

  fun getError(errorCode: String): ErrorReponse {
    when (errorCode) {
      Unauthorized.toString() ->
        return Unauthorized
      NoConnection.toString() ->
        return NoConnection
      InvalidToken.toString() ->
        return InvalidToken
      NoResponse.toString() ->
        return NoResponse
      Cancelled.toString() ->
        return Cancelled
      PasswordNotValid.toString() ->
        return PasswordNotValid
      WithoutStores.toString() ->
        return WithoutStores
      StoreNotFound.toString() ->
        return StoreNotFound
      SupplierNotFound.toString() ->
        return SupplierNotFound
      NoData.toString() ->
        return NoData
      else ->
        return Error
    }

  }

}