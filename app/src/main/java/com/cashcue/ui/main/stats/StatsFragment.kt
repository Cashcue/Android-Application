package com.cashcue.ui.main.stats

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.cashcue.R
import com.cashcue.adapter.RiwayatKeuanganAdapter
import com.cashcue.data.local.room.entity.RiwayatKeuanganDanAnggaran
import com.cashcue.databinding.FragmentStatsBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.utils.DateConverter
import com.cashcue.utils.RupiahConverter
import com.cashcue.utils.TasksFilterType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class StatsFragment : Fragment() {
    private val statsViewModel by viewModels<StatsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentStatsBinding? = null
    private lateinit var lineChart: LineChart
    private lateinit var riwayatKeuanganAdapter: RiwayatKeuanganAdapter
    private var chartData = arrayListOf<Entry>()
    private var days = arrayListOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvHistory.apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(requireContext(), 1)
            } else {
                GridLayoutManager(requireContext(), 2)
            }
            adapter = riwayatKeuanganAdapter
        }

        val ago = System.currentTimeMillis() - 604800000
        val current = System.currentTimeMillis()
        for (i in 1..7) {
            val dayInMillis = 86400000 * i
            days.add(DateConverter.convertToDayOfWeek(ago + dayInMillis))
        }
        statsViewModel.getCurrentRiwayatKeuangan(ago, current).observe(viewLifecycleOwner) {
            var pemasukan = 0
            var pengeluaran = 0
            var counter = 0

            days.toSet().forEach { _ ->
                var jumlah = 0

                it.forEach { data ->
                    if (DateConverter.convertToDayOfWeek(data.riwayatKeuangan.tanggal) == days[counter]) {
                        if (data.anggaran?.jenis == 0) {
                            pemasukan += data.riwayatKeuangan.saldo
                            jumlah += data.riwayatKeuangan.saldo
                        } else {
                            pengeluaran += data.riwayatKeuangan.saldo
                            jumlah -= data.riwayatKeuangan.saldo
                        }
                    }
                }

                chartData.add(Entry(counter.toFloat(), jumlah.toFloat()))
                counter += 1
            }

            binding.tvPemasukan.text = RupiahConverter.compress(pemasukan)
            binding.tvPengeluaran.text = RupiahConverter.compress(pengeluaran)


            val saldo = pemasukan + pengeluaran
            val percent = pengeluaran.toFloat() / saldo.toFloat()

            val klasifikasi: String = if (pemasukan == 0 && pengeluaran == 0) {
                "-"
            } else if (percent < 0.16) {
                getString(R.string.hemat)
            } else if (percent in 0.16..0.23) {
                getString(R.string.normal)
            } else {
                getString(R.string.boros)
            }
            binding.tvKlasifikasi.text = klasifikasi

            setChartData(chartData)
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                statsViewModel.filter(
                    when (p2) {
                        0 -> TasksFilterType.TERBARU
                        1 -> TasksFilterType.TERLAMA
                        else -> TasksFilterType.TERBARU
                    }
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Do nothing
            }

        }
        statsViewModel.riwayatKeuangan.observe(viewLifecycleOwner) {
            showRecyclerView(it)
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        riwayatKeuanganAdapter = RiwayatKeuanganAdapter(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setChartData(list: ArrayList<Entry>) {
        lineChart = binding.lineChart

        lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.backgroundChart))
        lineChart.legend.textColor = ContextCompat.getColor(requireContext(), R.color.colorChart)

        val description = Description()
        description.text = ""
        lineChart.description = description

        val xAxis = lineChart.xAxis
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.colorChart)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(days.toSet())
        xAxis.labelCount = 7
        xAxis.granularity = 1f

        lineChart.axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.colorChart)
        lineChart.axisRight.textColor = ContextCompat.getColor(requireContext(), R.color.colorChart)

        val lineDataSet = LineDataSet(list, "Rupiah")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.primary)
        lineDataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.primary))
        lineDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.colorChart)
        lineDataSet.lineWidth = 2f

        val lineData = LineData(lineDataSet)
        lineChart.setExtraOffsets(10f, 10f, 10f, 10f)
        lineChart.data = lineData
        lineChart.data.notifyDataChanged()
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }

    private fun showRecyclerView(riwayatKeuangan: PagedList<RiwayatKeuanganDanAnggaran>) {
        binding.tvNoData.isVisible = riwayatKeuangan.isEmpty()
        riwayatKeuanganAdapter.submitList(riwayatKeuangan)
    }
}