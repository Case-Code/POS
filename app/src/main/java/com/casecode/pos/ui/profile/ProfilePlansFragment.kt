package com.casecode.pos.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.casecode.pos.databinding.FragmentProfilePlansBinding


class ProfilePlansFragment : Fragment() {

    private lateinit var binding: FragmentProfilePlansBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfilePlansBinding.inflate(inflater, container, false)

        return binding.root
    }

}