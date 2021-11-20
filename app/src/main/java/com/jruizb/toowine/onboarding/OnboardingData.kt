package com.jruizb.toowine.onboarding

import com.jruizb.toowine.R
import com.jruizb.toowine.domain.OnboardingItems


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 11/10/2021
 */
class OnboardingData {

    companion object{
       var onboardingItemsAdapter = arrayListOf(
           OnboardingItems(
               R.drawable.onboarding_cheers_ext,
               R.string.onboarding_pag0_title,
               R.string.onboarding_pag0_description
           ),
           OnboardingItems(
               R.drawable.onboarding_wines_variety,
               R.string.onboarding_pag1_title,
               R.string.onboarding_pag1_description
           ),
           OnboardingItems(
               R.drawable.onboarding_onlystar_80_1_10,
               R.string.onboarding_pag2_title,
               R.string.onboarding_pag2_description
           ),
           OnboardingItems(
               R.drawable.onboarding_wine_news,
               R.string.onboarding_pag3_title,
               R.string.onboarding_pag3_description
           )
       )
    }
}