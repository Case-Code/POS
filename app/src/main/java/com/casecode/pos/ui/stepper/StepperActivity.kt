package com.casecode.pos.ui.stepper

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.aceinteract.android.stepper.StepperNavListener
import com.casecode.pos.R
import com.casecode.pos.databinding.ActivityStepperBinding

class StepperActivity : AppCompatActivity(), StepperNavListener {


    private var _binding: ActivityStepperBinding? = null
    private val binding: ActivityStepperBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStepperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stepper.setupWithNavController(findNavController(R.id.frame_stepper))
    }

    fun getNextStep() {
        binding.stepper.goToNextStep()
    }

    fun getPreviousStep() {
        binding.stepper.goToPreviousStep()
    }


    override fun onCompleted() {

        Toast.makeText(
            this,
            "Step changed to: ${binding.stepper.goToNextStep()}",
            Toast.LENGTH_SHORT
        ).show()


    }

    override fun onStepChanged(step: Int) {
        Toast.makeText(this, "Stepper completed", Toast.LENGTH_SHORT).show()

    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.frame_stepper).navigateUp()
}