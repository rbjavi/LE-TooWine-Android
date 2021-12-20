package com.jruizb.toowine.menunavigation.winecollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.navigation.findNavController
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.FragmentWineCollectionsBinding
import com.jruizb.toowine.util.Constants


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

    }

    override fun onStart() {
        super.onStart()

//        checkVisibleSearchBar()
        view?.let {
            clickOnWineTypesCard(it)
        }

    }

//    private fun checkVisibleSearchBar () {
//        binding.searchImageButton.setOnClickListener {
//        if (binding.collectionsSearchView.visibility == View.GONE) {
//
//                binding.collectionsSearchView.visibility = View.VISIBLE
//
//        } else if (binding.collectionsSearchView.visibility == View.VISIBLE){
//
//                binding.collectionsSearchView.visibility = View.GONE
//            }
//        }
//    }

    private fun clickOnWineTypesCard(view: View) {
        //La vista de gridlayout
        val grid: GridLayout = requireView().findViewById(R.id.wineTypesGridLayoutWineCollection)
        val childCount: Int = grid.childCount  //El número total de hijos del grupo GridLayout

        for (i in 0 until childCount) {
            grid.getChildAt(i).setOnClickListener {
//                Toast.makeText(requireContext(),"position $i",Toast.LENGTH_SHORT).show()
                when (i) {
                    0 -> {
                        val uri = Constants.URI_RED_WINE
                        val t = Constants.TEXT_RED_WINE
                        val action = WineCollectionsFragmentDirections.actionWineCollectionFragmentToWineTypeList(uri,t)
                        view.findNavController().navigate(action)
                    }
                    1 -> {
                        val uri = Constants.URI_WHITE_WINE
                        val t = Constants.TEXT_WHITE_WINE
                        val action = WineCollectionsFragmentDirections.actionWineCollectionFragmentToWineTypeList(uri,t)
                        view.findNavController().navigate(action)
                    }
                    2 -> {
                        val uri = Constants.URI_ROSE_WINE
                        val t = Constants.TEXT_ROSE_WINE
                        val action = WineCollectionsFragmentDirections.actionWineCollectionFragmentToWineTypeList(uri,t)
                        view.findNavController().navigate(action)
                    }
                    3 -> {
                        val uri = Constants.URI_SPARKLING_WINE
                        val t = Constants.TEXT_SPARKLING_WINE
                        val action = WineCollectionsFragmentDirections.actionWineCollectionFragmentToWineTypeList(uri,t)
                        view.findNavController().navigate(action)
                    }
                }
            }
        }
    }
}