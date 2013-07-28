package com.yqwireless.exs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBar extends View {
	private char[] l;
	private SectionIndexer sectionIndexter = null;
	private ListView list;
	private TextView mDialogText;
	private int current_position = 0;
	private  int m_nItemHeight = 50;
	private Paint paint = new Paint();

	public SideBar(Context context) {
		super(context);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setListView(ListView _list) {
		list = _list;
		sectionIndexter = (SectionIndexer) _list.getAdapter();
		String[] indexers = (String[])sectionIndexter.getSections();
		l = new char[indexers.length];
		for (int i = 0; i < indexers.length; i++) {
			l[i] = indexers[i].charAt(0);
		}
		list.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int cur_position = sectionIndexter.getSectionForPosition(firstVisibleItem);
				if (cur_position != current_position) {
					current_position = cur_position;
					postInvalidate();
				}
			}
		});
	}

	public void setTextView(TextView mDialogText) {
		this.mDialogText = mDialogText;
	}

	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		int i = (int) event.getY();
		int idx = i / m_nItemHeight;
		if (idx >= l.length) {
			idx = l.length - 1;
		} else if (idx < 0) {
			idx = 0;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			
			if (sectionIndexter == null) {
				sectionIndexter = (SectionIndexer) list.getAdapter();
			}
			int position = sectionIndexter.getPositionForSection(l[idx]);
			if (position == -1) {
				return true;
			}
			mDialogText.setVisibility(View.VISIBLE);
			mDialogText.setText("" + l[idx]);
			current_position = idx;
			list.setSelection(position);
			postInvalidate();
		} else {
			postDelayed(new Runnable() {

				@Override
				public void run() {
					mDialogText.setVisibility(View.INVISIBLE);
				}
			}, 500);
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		m_nItemHeight = (int)(list.getHeight() / 26);
		paint.setColor(0xff595c61);
		paint.setAntiAlias(true);
		paint.setTextSize( 2 * m_nItemHeight / 3);
		paint.setTextAlign(Paint.Align.CENTER);
		float widthCenter = getMeasuredWidth() / 2;
		for (int i = 0; i < l.length; i++) {
			System.out.println("current_position: " + current_position + ", i " + i);
			if (i == current_position) {
				paint.setColor(list.getResources().getColor(android.R.color.white));
			}
			canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight
					+ (i * m_nItemHeight), paint);
			paint.setColor(0xff595c61);
		}
		super.onDraw(canvas);
	}
}
