package com.alfanthariq.cvd19info.base.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(manager: FragmentManager, lifecycle : Lifecycle, fragments : List<Fragment>) : FragmentStateAdapter(manager, lifecycle) {

    private val items = fragments

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = items[position]
}