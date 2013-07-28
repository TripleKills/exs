package com.yqwireless.exs;

import java.util.Map;

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
	private TextView query_company;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.query_fragment, null);
	    query_company = (TextView) v.findViewById(R.id.query_company);
		EditText query_order = (EditText) v.findViewById(R.id.query_order);
		View query_query = v.findViewById(R.id.query_query);
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
		return v;
	}
}
