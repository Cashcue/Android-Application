package com.cashcue.ui.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.cashcue.adapter.AnggaranAdapter
import com.cashcue.data.local.pref.user.UserModel
import com.cashcue.data.local.room.entity.Anggaran
import com.cashcue.databinding.FragmentHomeBinding
import com.cashcue.ui.ViewModelFactory
import com.cashcue.ui.main.MainViewModel
import com.cashcue.ui.main.budgeting.add.AddBudgetingActivity
import com.cashcue.utils.RupiahConverter

class HomeFragment : Fragment() {

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var _binding: FragmentHomeBinding? = null
    private lateinit var anggaranAdapter: AnggaranAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.rvBudgeting.apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager(requireContext(), 1)
            } else {
                GridLayoutManager(requireContext(), 2)
            }
            adapter = anggaranAdapter
        }

        homeViewModel.getAnggaran().observe(viewLifecycleOwner) {
            showRecyclerView(it)
        }

        mainViewModel.getSession().observe(viewLifecycleOwner) {
            setupShow(it)
        }

        binding.tvAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddBudgetingActivity::class.java))
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        anggaranAdapter = AnggaranAdapter(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setupShow(user: UserModel) {
        binding.tvUserName.text = user.nama+"!"
        binding.tvTotalSaldo.text = RupiahConverter.convert(user.saldo)
    }

    private fun showRecyclerView(anggaran: PagedList<Anggaran>) {
        binding.tvNoData.isVisible = anggaran.isEmpty()
        anggaranAdapter.submitList(anggaran)
    }
}