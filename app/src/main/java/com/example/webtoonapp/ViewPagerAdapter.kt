package com.example.webtoonapp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(private val mainActivity: MainActivity) : FragmentStateAdapter(mainActivity){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                WebViewFragment(position, "https://comic.naver.com/webtoon/weekdayList?week=mon").apply {
                    listener = mainActivity
                }
            }
            1 -> {
                WebViewFragment(position, "https://comic.naver.com/webtoon/weekdayList?week=tue").apply {
                    listener = mainActivity
                }
            }
            else -> {
                WebViewFragment(position, "https://comic.naver.com/webtoon/weekdayList?week=wed").apply {
                    listener = mainActivity
                }
            }
        }
    }
}