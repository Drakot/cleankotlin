package es.demokt.kotlindemoproject.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateUtils {
  val DATE_FORMAT_SERVER = "yyy-MM-dd"
  val DATE_FORMAT_CHART = "dd"
  fun formatDate(date: Date): String? {
    return formatDate(date, DATE_FORMAT_SERVER)
  }

  fun formatDate(date: Date, format: String): String? {
    var dateFormatted: String? = null
    try {
      val formatter = SimpleDateFormat(format, Locale.getDefault())
      dateFormatted = formatter.format(date)
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return dateFormatted
  }

  fun formatDate(dateStr: String): Date? {
    val dateFormat = SimpleDateFormat(DATE_FORMAT_SERVER)
    val date: Date?
    try {
      date = dateFormat.parse(dateStr)
    } catch (e: ParseException) {
      e.printStackTrace()
      return null
    }
    return date
  }

  fun getDates(dateStart: String, dateEnd: String): List<Date> {
    val dates = ArrayList<Date>()
    val df1 = SimpleDateFormat(DATE_FORMAT_SERVER)

    var date1: Date? = null
    var date2: Date? = null

    try {
      date1 = df1.parse(dateStart)
      date2 = df1.parse(dateEnd)
    } catch (e: ParseException) {
      e.printStackTrace()
    }

    val cal1 = Calendar.getInstance()
    cal1.setTime(date1)

    val cal2 = Calendar.getInstance()
    cal2.setTime(date2)

    while (!cal1.after(cal2)) {
      dates.add(cal1.getTime())
      cal1.add(Calendar.DATE, 1)
    }
    return dates
  }

}