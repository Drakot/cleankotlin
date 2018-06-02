package es.demokt.kotlindemoproject.modules.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.main.HomeView
import es.demokt.kotlindemoproject.utils.AllAxisValueFormatter
import kotlinx.android.synthetic.main.fragment_home.btn_scanner
import kotlinx.android.synthetic.main.fragment_home.chart
import kotlinx.android.synthetic.main.fragment_home.llChange
import kotlinx.android.synthetic.main.fragment_home.tvAddress
import kotlinx.android.synthetic.main.fragment_home.tvCreatedDay
import kotlinx.android.synthetic.main.fragment_home.tvCreatedToday
import kotlinx.android.synthetic.main.fragment_home.tvEditedDay
import kotlinx.android.synthetic.main.fragment_home.tvEditedToday
import kotlinx.android.synthetic.main.fragment_home.tvName
import kotlinx.android.synthetic.main.fragment_home.tvScannedDay
import kotlinx.android.synthetic.main.fragment_home.tvScannedToday
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), HomeView {
  private val presenter: HomePresenter by lazy { HomePresenter(this) }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_home
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun viewCreated(view: View?) {
    configurator?.title = getString(R.string.home)
    initViews()
    presenter.init()
  }

  private fun initViews() {
    btn_scanner.setOnClickListener({
      Log.d(LOG_TAG, "scanner")
      getNavigator().finishCurrent(false)
          .preserve(true)
          .addBackStack(true)
          .navigateScanProduct()
    })


    llChange.setOnClickListener {
      goToStoreSelection()
    }
    initChart()
  }

  private fun initChart() {
    val x = chart.getXAxis()

    chart.legend.isEnabled = false
    chart.description.isEnabled = false

    chart.getAxisRight().setEnabled(false)
    chart.getAxisLeft().setEnabled(true)
    chart.getAxisRight().setDrawGridLines(false)
    chart.getAxisLeft().setDrawGridLines(true)

    chart.getAxisLeft().setDrawLabels(true)
    chart.getAxisRight().setDrawLabels(false)
    chart.getXAxis().setDrawLabels(true)

    chart.setDrawGridBackground(false)
    chart.setHighlightPerTapEnabled(false)
    chart.setHighlightPerDragEnabled(false)

    x.setDrawLimitLinesBehindData(false)
    x.setDrawGridLines(false)
    x.setDrawAxisLine(true)
    x.setEnabled(true)
    x.setPosition(XAxis.XAxisPosition.BOTTOM)
    chart.setNoDataText(getString(R.string.no_records))
    val leftAxis = chart.getAxisLeft()

    leftAxis.setDrawAxisLine(false)
    leftAxis.setCenterAxisLabels(false)
    leftAxis.axisMinimum = 0f
  }

  override fun setChartData(result: HomeModel) {

    chart.data = generateLineData(result.items!!)

    val count = result.itemDate!!.size

    val x = chart.getXAxis()
    x.setLabelCount(count, true)
    val xAxisFormatter = AllAxisValueFormatter(result.itemDate!!.toTypedArray())
    x.setValueFormatter(xAxisFormatter)

    chart.invalidate()
  }

  private fun generateLineData(consumption: List<Float>): LineData {
    val set1 = getLineDataSet(consumption)
    setGeneralSettings(set1)
    set1.setDrawCircles(true)
    set1.color = resources.getColor(R.color.chart_line)
    set1.setCircleColors(resources.getColor(R.color.chart_line))
    set1.setCircleRadius(3f)

    val dataSets = ArrayList<ILineDataSet>()
    dataSets.add(set1)

    return LineData(dataSets)
  }

  fun getLineDataSet(line: List<Float>): LineDataSet {
    val lineEntries = ArrayList<Entry>()
    lineEntries.clear()

    for (i in line.indices) {
      val value = line[i]
      val entry = Entry(i.toFloat(), value)
      lineEntries.add(entry)
    }
    val lineDataSet = LineDataSet(lineEntries, "")

    return lineDataSet
  }

  fun setGeneralSettings(set: LineDataSet) {
    set.setDrawCircles(false)
    set.setDrawCircleHole(false)
    set.setCircleColors()
    set.setDrawFilled(false)

    set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    set.cubicIntensity = 0.2f
    set.lineWidth = 2f
    set.setDrawValues(false)
    set.setDrawHorizontalHighlightIndicator(false)
    set.setDrawVerticalHighlightIndicator(true)
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    inflater: MenuInflater
  ) {
    inflater.inflate(R.menu.toolbar, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    return when (item.itemId) {
      R.id.menu_action_logout -> {
        Log.d(LOG_TAG, "logout")
        DataManager.logout()
        getNavigator().navigateToLogin()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun setStore(result: UserStore) {
    tvName.text = result.name
    tvAddress.text = result.location
  }

  override fun goToStoreSelection() {
    getNavigator().addBackStack(true)
        .navigateToSearchStores()
  }

  override fun setAverage(status: Status?) {
    tvScannedDay.text = status?.scanned.toString()
    tvCreatedDay.text = status?.added.toString()
    tvEditedDay.text = status?.updated.toString()
  }

  override fun setToday(status: Status?) {
    tvScannedToday.text = status?.scanned.toString()
    tvCreatedToday.text = status?.added.toString()
    tvEditedToday.text = status?.updated.toString()
  }
}

