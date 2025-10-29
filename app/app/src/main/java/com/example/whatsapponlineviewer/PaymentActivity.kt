package com.example.whatsapponlineviewer

import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.whatsapponlineviewer.databinding.ActivityPaymentBinding
import com.example.whatsapponlineviewer.viewmodel.StatusViewModel
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private var _binding: ActivityPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: StatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            _binding = ActivityPaymentBinding.inflate(layoutInflater)
            setContentView(binding.root)

            viewModel = ViewModelProvider(this)[StatusViewModel::class.java]

            setupPaymentMethodSelection()
            
            // Show card payment by default
            showCardPaymentFragment()
        } catch (e: Exception) {
            Log.e("PaymentActivity", "Error initializing: ${e.message}", e)
            Toast.makeText(this, "Error initializing payment screen", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupPaymentMethodSelection() {
        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbCard -> showCardPaymentFragment()
                R.id.rbSpei -> showSpeiPaymentFragment()
            }
        }
    }

    private fun showCardPaymentFragment() {
        val fragment = CardPaymentFragment()
        replaceFragment(fragment)
    }

    private fun showSpeiPaymentFragment() {
        val fragment = SpeiPaymentFragment()
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.paymentContainer, fragment)
            .commit()
    }

    fun onPaymentSuccess() {
        viewModel.setPremium()
        Toast.makeText(this, getString(R.string.payment_success), Toast.LENGTH_LONG).show()
        finish()
    }
}

class CardPaymentFragment : Fragment(R.layout.fragment_card_payment) {
    
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        view.findViewById<android.widget.Button>(R.id.btnPay).setOnClickListener {
            // Simulate payment processing
            (activity as? PaymentActivity)?.onPaymentSuccess()
        }
    }
}

class SpeiPaymentFragment : Fragment(R.layout.fragment_spei_payment) {
    
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Generate a random reference number
        val reference = UUID.randomUUID().toString().substring(0, 8).uppercase()
        view.findViewById<android.widget.TextView>(R.id.tvSpeiReference).text = 
            getString(R.string.spei_reference, reference)
        
        view.findViewById<android.widget.Button>(R.id.btnConfirmSpei).setOnClickListener {
            // Simulate payment confirmation
            (activity as? PaymentActivity)?.onPaymentSuccess()
        }
    }
}