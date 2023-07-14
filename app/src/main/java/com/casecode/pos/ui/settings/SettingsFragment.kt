package com.casecode.pos.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.casecode.pos.databinding.FragmentSettingsBinding
import com.casecode.pos.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel
    private var binding : FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSettingsBinding.inflate(inflater, container, false)
        return  binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
    }
}