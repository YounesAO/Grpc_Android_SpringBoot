package ma.ensa.bankgrpc.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import ma.ensa.bankgrpc.R
import ma.ensa.bankgrpc.adapter.CompteAdapter
import ma.ensa.bankgrpc.databinding.ActivityMainBinding
import ma.ensa.bankgrpc.modelview.CompteViewModel
import ma.ensa.bankgrpc.stubs.TypeCompte
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    // had binding bach nkhdmo b les views dyalna
    private lateinit var binding: ActivityMainBinding
    // adapter dyal list dyal les comptes
    private lateinit var comptesAdapter: CompteAdapter
    // viewModel bach n9ado n communicatiw m3a server
    private val viewModel: CompteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialisation dyal UI
        setupRecyclerView()
        setupSpinner()
        setupObservers()
        setupClickListeners()

        // jbed data men server
        viewModel.loadComptes()
        viewModel.loadStats()
    }

    // configuration dyal recyclerview bach naffchiw les comptes
    private fun setupRecyclerView() {
        comptesAdapter = CompteAdapter()
        binding.recyclerViewComptes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = comptesAdapter
        }
    }

    // configuration dyal dropdown menu dyal types
    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner_item,
            TypeCompte.values().map { it.name }
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        (binding.spinnerType as? MaterialAutoCompleteTextView)?.setAdapter(adapter)
    }

    // observers bach nchofo changes f data
    private fun setupObservers() {
        // observer dyal list dyal comptes
        viewModel.comptes.observe(this) { comptes ->
            comptesAdapter.submitList(comptes)
        }

        // observer dyal statistics
        viewModel.stats.observe(this) { stats ->
            binding.textViewTotal.text = "${stats.sum}€"
            binding.textViewAverage.text = "${stats.average}€"
            binding.textViewCount.text = "${stats.count}"
        }
    }

    // click listeners dyal buttons
    private fun setupClickListeners() {
        binding.buttonSave.setOnClickListener {
            // jbed values men UI w sifthom l server
            val solde = binding.editTextSolde.text.toString().toFloatOrNull() ?: 0f
            val type = TypeCompte.valueOf(binding.spinnerType.text.toString())
            val dateCreation = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date())

            viewModel.saveCompte(solde, dateCreation, type)
            binding.editTextSolde.text?.clear()
        }
    }
}