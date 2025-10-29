package com.example.phonetracker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.phonetracker.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        // Set up payment method selection
        binding.btnCardPayment.setOnClickListener {
            selectPaymentMethod(CardPaymentFragment())
            binding.btnCardPayment.isSelected = true
            binding.btnSpeiPayment.isSelected = false
        }

        binding.btnSpeiPayment.setOnClickListener {
            selectPaymentMethod(SpeiPaymentFragment())
            binding.btnCardPayment.isSelected = false
            binding.btnSpeiPayment.isSelected = true
        }

        // Default to card payment
        binding.btnCardPayment.performClick()

        // Set up payment button
        binding.btnPay.setOnClickListener {
            // Simulate payment processing
            binding.progressBar.visibility = View.VISIBLE
            binding.btnPay.isEnabled = false

            // In a real app, this would process the payment through Stripe
            // For now, we'll simulate a successful payment after a delay
            binding.root.postDelayed({
                binding.progressBar.visibility = View.GONE
                
                // Set premium status
                val app = application as MyApplication
                app.locationDatabase.setPremium(true)
                
                // Show success message and finish
                Toast.makeText(
                    this@PaymentActivity,
                    getString(R.string.payment_success),
                    Toast.LENGTH_LONG
                ).show()
                
                finish()
            }, 2000)
        }
    }

    private fun selectPaymentMethod(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}

// Card Payment Fragment
class CardPaymentFragment : Fragment(R.layout.fragment_card_payment)

// SPEI Payment Fragment
class SpeiPaymentFragment : Fragment(R.layout.fragment_spei_payment)