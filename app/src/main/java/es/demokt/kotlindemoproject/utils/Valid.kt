package es.demokt.kotlindemoproject.utils

import java.util.Calendar
import java.util.regex.Pattern

/**
 *
 * Created by aluengo on 17/2/18.
 */
object Valid {
    var EMAIL_REGEX = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}"

    fun isEmailValid(string: String): Boolean {
        return isValid(string, EMAIL_REGEX)
    }

    fun isPasswordValid(string: String): Boolean {
        return string.trim().length >= 5
    }

    fun isNotNullOrEmpty(vararg strings: String): Boolean {
        var isValid = false
        if (!strings.isEmpty()) {
            for (data in strings) {
                isValid = !data.isEmpty() && !data.isEmpty()
                if (!isValid) {
                    break
                }
            }
        }
        return isValid
    }

    fun isValid(string: String, regex: String): Boolean {
        var isValid = false
        if (isNotNullOrEmpty(string)) {
            isValid = checkRegex(string, regex)
        }
        return isValid
    }

    fun checkRegex(field: String, regex: String): Boolean {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(field)
        return matcher.matches()
    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(year, month, day)

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    fun checkValidAge(year: Int, month: Int, day: Int): Boolean {
        val years = getAge(year, month, day)

        if (years < 14 && year > 100) {
            return false
        }
        return true
    }

    fun checkValidKilograms(string: String): Boolean {
        val data = Integer.parseInt(string)

        if (data in 100 downTo 20)
            return true

        return false
    }

    fun checkValidHeight(string: String): Boolean {
        val data = Integer.parseInt(string)

        if (data in 230 downTo 100)
            return true

        return false
    }
}