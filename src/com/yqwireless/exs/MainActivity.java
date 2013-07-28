package com.yqwireless.exs;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.yqwireless.exs.EventCenter.EventListenr;

public class MainActivity extends SherlockFragmentActivity {
	ViewPager mPager;
	SectionsPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Theme_Sherlock);
		setContentView(R.layout.activity_main);
		mAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.addView(getPagerTitle());
		EventCenter.getInstance().regist(new EventListenr() {
			
			@Override
			public void onEvent(Map<String, String> _data) {
				String company = _data.get("company");
				Toast.makeText(MainActivity.this, company, Toast.LENGTH_LONG).show();
				mPager.setCurrentItem(0);
			}
			
			@Override
			public String getName() {
				return "company_select";
			}
		});
		EventCenter.getInstance().regist(new EventListenr() {
			
			@Override
			public void onEvent(Map<String, String> _data) {
				mPager.setCurrentItem(1);
			}
			
			@Override
			public String getName() {
				return "jump_company_select";
			}
		});
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 0) {
					EventCenter.getInstance().onEvent("scroll_to_query");
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
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
			Fragment fg;
			Bundle args = new Bundle();
			args.putString("title", NavigationHelper.getTitles()[arg0]);
			switch(arg0) {
			case 0:
				fg = new QueryFragment();
				break;
			case 1:
				fg = new CompanyListFragment();
				break;
			default:
				return new Fragment();
			}
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
