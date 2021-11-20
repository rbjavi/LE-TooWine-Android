package com.jruizb.toowine.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.OnboardingItemContainerBinding
import com.jruizb.toowine.domain.OnboardingItems


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 07/10/2021
 */
class OnboardingItemsAdapter (val contexto: Context, private val onboardingList: List<OnboardingItems>)
    :RecyclerView.Adapter<OnboardingItemsAdapter.OnboardingDataViewHolder>() {
    //Declaracion de binding con el fin de inicalizarla posteriormente
    private lateinit var binding: OnboardingItemContainerBinding


    override fun getItemCount(): Int {
        return onboardingList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingDataViewHolder {
        return OnboardingDataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingDataViewHolder, position: Int) {
        holder.bindWalkthroughElementsFragment(onboardingList[position])
    }

    //    Clase Interna
    inner class OnboardingDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        private val im = view.findViewById<ImageView>(R.id.walkthroughImage)
//        private val t = view.findViewById<TextView>(R.id.textViewWalkthroughTitle)
//        private val d = view.findViewById<TextView>(R.id.textViewWalkthroughDescription)

        fun bindWalkthroughElementsFragment(onboardingData: OnboardingItems) {
            //inicializa el bindeo de los elementos que se quieren pintar en la vista
            binding = OnboardingItemContainerBinding.bind(itemView)

            with(binding) {
                onboardingImageView.setImageResource(onboardingData.image)
                onboardingItemTextVTitle.text = contexto.getString(onboardingData.title)
                onboardingItemTextVDescription.text = contexto.getString(onboardingData.description)

            }
        }
    }
}