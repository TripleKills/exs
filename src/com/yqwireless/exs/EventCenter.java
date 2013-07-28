package com.yqwireless.exs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventCenter {
	private static EventCenter _inst;
	private List<EventListenr> _lsnrs;

	private EventCenter() {
		_lsnrs = new ArrayList<EventCenter.EventListenr>();
	}

	public static EventCenter getInstance() {
		if (null == _inst) {
			_inst = new EventCenter();
		}
		return _inst;
	}

	public void regist(EventListenr lsnr) {
		if (null == lsnr || _lsnrs.contains(lsnr))
			return;
		_lsnrs.add(lsnr);
	}

	public void unregist(EventListenr lsnr) {
		_lsnrs.remove(lsnr);
	}

	public void onEvent(String name, Map<String, String> data) {
		for (EventListenr lsnr : _lsnrs) {
			if (lsnr.getName().equals(name)) {
				lsnr.onEvent(data);
			}
		}
	}

	public interface EventListenr {

		public String getName();

		public void onEvent(Map<String, String> _data);
	}
}
