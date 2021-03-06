package com.yqwireless.exs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class CompanyListFragment extends SherlockFragment {

	private ListView lvContact;
	private SideBar indexBar;
	private WindowManager mWindowManager;
	private TextView mDialogText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		copyDB();
		DBOperate dao = new DBOperate(getActivity());
		List<String> companys = dao.getAllCompany();
		nicks = new String[companys.size()];
		for (int i = 0; i < companys.size(); i++)
			nicks[i] = companys.get(i);
		View v = inflater.inflate(R.layout.fragment_company, null);
		mWindowManager = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		findView(v);
		return v;
	}

	private void copyDB() {
		String dbDir = "data/data/" + getActivity().getPackageName()
				+ "/databases";
		File dir = new File(dbDir);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdir();
		}
		File dbFile = new File(dir, "company.db");
		if (!dbFile.exists()) {
			try {
				FileUtil.loadDBFile(R.raw.company, getResources(), dbFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void findView(View v) {
		lvContact = (ListView) v.findViewById(R.id.lvContact);
		new Thread(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// 排序(实现了中英文混排)
				Arrays.sort(nicks, new PinyinComparator());
				PingYinUtil.clearCache();
				Runnable r = new Runnable() {

					@Override
					public void run() {
						lvContact.setAdapter(new ContactAdapter(getActivity()));
						indexBar.setListView(lvContact);
						mDialogText = (TextView) LayoutInflater.from(
								getActivity()).inflate(R.layout.list_position,
								null);
						mDialogText.setVisibility(View.INVISIBLE);
						WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT,
								WindowManager.LayoutParams.TYPE_APPLICATION,
								WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
										| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
								PixelFormat.TRANSLUCENT);
						mWindowManager.addView(mDialogText, lp);
						indexBar.setTextView(mDialogText);
						lvContact.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								EventCenter center = EventCenter.getInstance();
								Map<String, String> data = new HashMap<String, String>();
								data.put("company", lvContact.getAdapter().getItem(arg2).toString());
								center.onEvent("company_select", data);
							}
						});
					}
				};
				getActivity().runOnUiThread(r);
			}
		}).start();
		indexBar = (SideBar) v.findViewById(R.id.sideBar);
	}

	class ContactAdapter extends BaseAdapter implements SectionIndexer {
		private Context mContext;
		private String[] mNicks;

		public ContactAdapter(Context mContext) {
			this.mContext = mContext;
			this.mNicks = nicks;
		}

		@Override
		public int getCount() {
			return mNicks.length;
		}

		@Override
		public Object getItem(int position) {
			return mNicks[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String nickName = mNicks[position];
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.contact_item, null);
				viewHolder = new ViewHolder();
				viewHolder.tvCatalog = (TextView) convertView
						.findViewById(R.id.contactitem_catalog);
				viewHolder.ivAvatar = (ImageView) convertView
						.findViewById(R.id.contactitem_avatar_iv);
				viewHolder.tvNick = (TextView) convertView
						.findViewById(R.id.contactitem_nick);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String catalog = converterToFirstSpell(nickName).substring(0, 1);
			if (position == 0) {
				viewHolder.tvCatalog.setVisibility(View.VISIBLE);
				viewHolder.tvCatalog.setText(catalog);
			} else {
				String lastCatalog = converterToFirstSpell(mNicks[position - 1])
						.substring(0, 1);
				if (catalog.equals(lastCatalog)) {
					viewHolder.tvCatalog.setVisibility(View.GONE);
				} else {
					viewHolder.tvCatalog.setVisibility(View.VISIBLE);
					viewHolder.tvCatalog.setText(catalog);
				}
			}

			viewHolder.ivAvatar.setImageResource(R.drawable.default_avatar);
			viewHolder.tvNick.setText(nickName);
			return convertView;
		}

		class ViewHolder {
			TextView tvCatalog;// 目录
			ImageView ivAvatar;// 头像
			TextView tvNick;// 昵称
		}

		@Override
		public int getPositionForSection(int section) {
			for (int i = 0; i < mNicks.length; i++) {
				String l = converterToFirstSpell(mNicks[i]).substring(0, 1);
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
			return -1;
		}

		@Override
		public int getSectionForPosition(int position) {
			String l = converterToFirstSpell(mNicks[position]).substring(0, 1);
			for (int i = 0; i < getSections().length; i++) {
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == ((String) getSections()[i]).charAt(0)) {
					return i;
				}
			}
			return 0;
		}

		@Override
		public Object[] getSections() {
			String[] arys = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
					"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
					"W", "X", "Y", "Z" };
			return arys;
		}
	}

	/**
	 * 昵称
	 */
	private static String[] nicks = { "阿雅", "北风", "张山", "李四", "欧阳锋", "郭靖",
			"黄蓉", "杨过", "凤姐", "芙蓉姐姐", "移联网", "樱木花道", "风清扬", "张三丰", "梅超风" };

	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
}
