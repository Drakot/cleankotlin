package es.demokt.kotlindemoproject.utils

import android.support.v7.app.AppCompatActivity
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.utils.message.MessageFactory

/**
 * errorManager
 * Created by aluengo on 18/2/18.
 */

fun AppCompatActivity.errorManager(error: ErrorReponse) {
  val messageFactory: MessageFactory by lazy { MessageFactory(this) }

  when (error) {
    ErrorReponse.NoConnection -> {
      messageFactory.getDefaultErrorMessage("No hay conexión a internet.").init().show()
    }
    ErrorReponse.NoData -> {
      messageFactory.getDefaultErrorMessage("No se ha podido obtener datos.").init().show()
    }
    ErrorReponse.NoResponse -> {
      messageFactory.getDefaultErrorMessage("No se ha podido obtener respuesta del servidor.").init().show()
    }
    ErrorReponse.InvalidToken -> {
      messageFactory.getDefaultErrorMessage("La sesión ha expirado.").init().show()
    }
    ErrorReponse.Error -> {
      messageFactory.getDefaultErrorMessage("¡Ups! Ha ocurrido un error.").init().show()
    }
    ErrorReponse.Unauthorized -> {
      messageFactory.getDefaultErrorMessage("Email o contraseña no válidos.").init().show()
    }
    ErrorReponse.Cancelled -> {
      messageFactory.getDefaultErrorMessage("La petición se ha cancelado.").init().show()
    }
    ErrorReponse.PasswordNotValid -> {
      messageFactory.getDefaultErrorMessage("La contraseña debe tener más de 6 carácteres.").init().show()
    }
    ErrorReponse.WithoutStores -> {
      messageFactory.getDefaultErrorMessage("No hay tiendas asignadas.").init().show()
    }
    ErrorReponse.StoreNotFound -> {
      messageFactory.getDefaultErrorMessage("No se han encontrado tiendas con ese nombre.").init().show()
    }
    ErrorReponse.SupplierNotFound -> {
      messageFactory.getDefaultErrorMessage("No se han encontrado proveedores con ese nombre.").init().show()
    }
    ErrorReponse.ServerError -> {
      messageFactory.getDefaultErrorMessage("Error del servidor.").init().show()
    }
    ErrorReponse.EmailOrPass -> {
      messageFactory.getDefaultErrorMessage("Email o contraseña no validos, reviselos por favor.").init().show()
    }
    ErrorReponse.MandatoryFields -> {
      messageFactory.getDefaultErrorMessage(messageFactory.activity.getString(R.string.error_mandatort_field)).init().show()
    }
  }

}

