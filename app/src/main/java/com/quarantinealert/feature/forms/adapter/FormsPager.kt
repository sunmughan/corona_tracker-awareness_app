package com.quarantinealert.feature.media.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FormsPager(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> =
        ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }
}