package com.yqwireless.exs;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.yqwireless.exs.EventCenter.EventListenr;

public class QueryFragment extends SherlockFragment {
	private TextView query_company, query_result;
	private View query_query;
	private EditText query_order;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.query_fragment, null);
	    query_company = (TextView) v.findViewById(R.id.query_company);
		query_order = (EditText) v.findViewById(R.id.query_order);
		query_query = v.findViewById(R.id.query_query);
		query_result = (TextView) v.findViewById(R.id.query_result);
		query_company.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EventCenter center = EventCenter.getInstance();
				center.onEvent("jump_company_select");
			}
		});
		EventCenter.getInstance().regist(new EventListenr() {
			
			@Override
			public void onEvent(Map<String, String> _data) {
				String company = _data.get("company");
				query_company.setText(company);
			}
			
			@Override
			public String getName() {
				return "company_select";
			}
		});
		initQueryBtn();
		return v;
	}
	
	private void initQueryBtn() {
		query_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isNetworkConnected()) {
					Toast.makeText(getActivity(), R.string.no_network,
					        Toast.LENGTH_SHORT).show();
					return;
				}
				String orderNumber = query_order.getText().toString()
						.trim();
				String company_cn = query_company.getText().toString();
				if (company_cn == null) {
					Toast.makeText(getActivity(), R.string.company_empty,
							Toast.LENGTH_SHORT).show();
					return;
				} else if (orderNumber == null
						|| orderNumber.equalsIgnoreCase("")) {
					Toast.makeText(getActivity(),
							R.string.order_number_empty,
							Toast.LENGTH_SHORT).show();
					return;
				}
				DBOperate dbOperate = new DBOperate(getActivity());
				Map<String, String> map = dbOperate.getCompanyInfo(company_cn);
				String company_en = map.get("company_en");
				search(company_en, orderNumber);
			}
		});
	}
	
	/**
	 * 判断网络状态。
	 */
	public boolean isNetworkConnected() {
		boolean flag = false;
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnectedOrConnecting()) {
			flag = true;
		}
		return flag;
	}
	
	private void search(final String company_en, final String orderNumber) {
		Runnable r = new Runnable() {
			
			public void run() {
				String result = SearchUtil.search(company_en, orderNumber);
				String json_result = null;
				if (null != result) {
					try {
						JSONObject json = new JSONObject(result);
						json_result = json.getString("message");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				final String search_result = null != json_result ? json_result : getActivity().getString(R.string.order_unknown);
				Runnable r2 = new Runnable() {
					
					@Override
					public void run() {
						query_result.setText(search_result);
					}
				};
				getActivity().runOnUiThread(r2);
			}
		};
			new Thread(r).start();
				

	}
}
