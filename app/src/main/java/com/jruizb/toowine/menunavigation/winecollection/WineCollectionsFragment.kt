package com.jruizb.toowine.menunavigation.winecollection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentLoginBinding
import com.jruizb.toowine.databinding.FragmentWineCollectionsBinding


class WineCollectionsFragment : Fragment() {
    private var _binding: FragmentWineCollectionsBinding? = null
    // Esta propiedad es sólo válida entre onCreateView y
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWineCollectionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageButton.setOnClickListener {
            binding.collectionsSearchView.visibility = View.VISIBLE
        }    }
}