package com.jruizb.toowine.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.jruizb.toowine.home.HomeActivity
import com.jruizb.toowine.R
import com.jruizb.toowine.databinding.ActivityOnboardingBinding
import com.jruizb.toowine.preferences.PreferencesKey
import com.jruizb.toowine.preferences.PreferencesProvider

class Onboarding : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    //    private lateinit var indicatorsContainer: LinearLayout
//    private lateinit var viewModel: OnboardingViewModel
    private var currentPositionPager: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
//        Thread.sleep(1200)
//        setTheme(R.style.SplashTheme)

        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.onboardingButtonGetStarted.visibility = View.INVISIBLE
        setOnboardingItems()
        setupDotIndicators()
        setCurrentIndicatorPosition(0)
//        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)
    }


    private fun setOnboardingItems() {
        val onboardingItemsAdapter = OnboardingItemsAdapter(this,OnboardingData.onboardingItemsAdapter)
        binding.onboardingViewPager.adapter = onboardingItemsAdapter
//            OnboardingItemsAdapter(this, OnboardingData.onboardingItemsAdapter)

        with(binding) {
            onboardingViewPager.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        setCurrentIndicatorPosition(position)
                        currentPositionPager = position

//                        if (currentPositionPager == onboardingItemsAdapter.itemCount -1) {
//                            onboardingButtonGetStarted.visibility = View.VISIBLE
//                        } else {
//                            onboardingButtonGetStarted.visibility = View.INVISIBLE
//                        }
                        onboardingButtonGetStarted.isEnabled = currentPositionPager == onboardingItemsAdapter.itemCount -1
//                        onboardingButtonGetStarted.visibility = if (position < onboardingData.itemCount ) View.INVISIBLE else View.VISIBLE
                    }
                })
            (onboardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
            //Next ImageView Button
            onboardingImageVNext.setOnClickListener {
//                if (onboardingViewPager.currentItem + 1 < onboardingData.itemCount) {
//                    onboardingViewPager.currentItem += 1
//                } else {
//                    navigateTo()
//                }
                if (currentPositionPager  < onboardingItemsAdapter.itemCount - 1) {
                    currentPositionPager += 1
                    onboardingViewPager.setCurrentItem(currentPositionPager, true)
                }
            }
            //skip Text
            onboardingTextVSaltar.setOnClickListener {
                PreferencesProvider.set(this@Onboarding ,PreferencesKey.ONBOARDING,true)
                navigateTo()
                finish()
            }
            //Get started Button
            onboardingButtonGetStarted.setOnClickListener {
                if (onboardingViewPager.currentItem == onboardingItemsAdapter.itemCount -1) {
                    PreferencesProvider.set(this@Onboarding ,PreferencesKey.ONBOARDING,true)
                    navigateTo()
                    finish()
                } //else {
//                    navigateTo()
//                }
            }
        }
    }

    /**
     * Navega a la actividad inicial
     */
    private fun navigateTo() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }

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

    private fun setCurrentIndicatorPosition(position: Int) {
        val indicators = binding.indicatorsContainer
        val childCount = indicators.childCount
        for (i in 0 until childCount) {
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