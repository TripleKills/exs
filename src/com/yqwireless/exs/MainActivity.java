package com.yqwireless.exs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {
	ViewPager mPager;
	SectionsPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTheme(R.style.Theme_Sherlock);
		mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.addView(getPagerTitle());
	}
	
	private PagerTitleStrip getPagerTitle() {
		PagerTabStrip pagerTabStrip = (PagerTabStrip) View.inflate(this,
				R.layout.pager_tab_title, null);
		android.support.v4.view.ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		lp.gravity = Gravity.TOP;
		pagerTabStrip.setLayoutParams(lp);
		return pagerTabStrip;
	}

	class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fg = new CompanyListFragment();
			Bundle args = new Bundle();
			args.putString("title", NavigationHelper.getTitles()[arg0]);
			fg.setArguments(args);
			return fg;
		}

		@Override
		public int getCount() {
			return NavigationHelper.getTitles().length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return NavigationHelper.getTitles()[position];
		}

	}

}
