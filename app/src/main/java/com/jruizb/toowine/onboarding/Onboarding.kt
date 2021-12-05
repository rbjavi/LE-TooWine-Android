package com.jruizb.toowine.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jruizb.toowine.main.HomeActivity
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityOnboardingBinding
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider

class Onboarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    private var currentPositionPager: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnboardingItems()
        setupDotIndicators()
        setCurrentIndicatorPosition(0)

    }


    private fun setOnboardingItems() {
        val onboardingItemsAdapter = OnboardingItemsAdapter(this,OnboardingData.onboardingItemsAdapter)
        binding.onboardingViewPager.adapter = onboardingItemsAdapter

        with(binding) {
            onboardingViewPager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    //Cuando una página del contenedor view pager sea seleccionada indica la posición
                    // en la que se encuentra del total
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        setCurrentIndicatorPosition(position)
                        currentPositionPager = position
                        //Botón getStarted será habilitado cuando sea la posición última de de los elementos del recyclerview
                        onboardingButtonGetStarted.isEnabled = currentPositionPager == onboardingItemsAdapter.itemCount -1
                    }
                }
            )
            (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER

            //Next ImageView Button
            onboardingImageVNext.setOnClickListener {
                if (currentPositionPager  < onboardingItemsAdapter.itemCount - 1) {
                    currentPositionPager += 1
                    onboardingViewPager.setCurrentItem(currentPositionPager, true)
                }
            }
            //skip Text Button
            //Guarda en shared preferences si se ha saltado el onboarding (true) y se navega directamente
            //a la actividad Home de la aplicación
            onboardingTextVSaltar.setOnClickListener {
                PreferencesProvider.set(this@Onboarding , PreferencesKey.ONBOARDING,true)
                navigateTo()
                finish()
            }
            //Get started Button
            //Guarda en shared preferences si se ha llegado al final de las vistas de onboarding (True)
            // y se navega directamente a la actividad Home de la aplicación
            onboardingButtonGetStarted.setOnClickListener {
                PreferencesProvider.set(this@Onboarding ,PreferencesKey.ONBOARDING,true)
                navigateTo()
                finish()
            }
        }
    }

    /**
     * Navega a la actividad inicial Home
     */
    private fun navigateTo() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }

    /**
     * Dibuja el diseño y situa programáticamente la posición de los puntos en la que estarán situados
     * onborading en la actividad onboarding
     */
    private fun setupDotIndicators() {
        val onboardingItemsAdapter = OnboardingData.onboardingItemsAdapter
        val indicators = arrayOfNulls<ImageView>(onboardingItemsAdapter.size)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(10, 0, 10, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let { it ->
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicators_inactive_background
                    )
                )
                it.layoutParams = layoutParams
                binding.indicatorsContainer.addView(it)

            }
        }
    }

    /**
     * Indica, mediante un color activo, en cuál posición de los puntos nos encontramos del total
     * de vistas definidas para el onboarding.
     */
    private fun setCurrentIndicatorPosition(position: Int) {
        val indicators = binding.indicatorsContainer
        val childCount = indicators.childCount
        for (i in 0 until childCount) {
            //almacena y obtiene la vista a la posición específica en el grupo
            val imageView = indicators.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.indicators_active_background
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext, R.drawable.indicators_inactive_background
                    )
                )
            }
        }
    }
}