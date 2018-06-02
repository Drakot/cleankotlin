package es.demokt.kotlindemoproject.utils

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.Date

class AllAxisValueFormatter(private val mValues: Array<Date>) : IAxisValueFormatter {

  /**
   * this is only needed if numbers are returned, else return 0
   */
  val decimalDigits: Int
    get() = 0
  val dateUtils = DateUtils()
  override fun getFormattedValue(value: Float, axis: AxisBase): String {
    var value = value
    var label = ""

    if (value < mValues.size) label = dateUtils.formatDate(mValues[value.toInt()], "dd")!!

    return label
  }
}