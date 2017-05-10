package com.homechart.app.picheader;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flexible.flexibleadapter.FlexibleAdapter;
import com.flexible.flexibleadapter.helpers.AnimatorHelper;
import com.flexible.viewholders.FlexibleViewHolder;
import com.homechart.app.R;

import java.util.List;


/**
 * Item dedicated to display which Layout is currently displayed.
 * This item is a Scrollable Header.
 */
public class ScrollableLayoutItem extends AbstractItem<ScrollableLayoutItem.LayoutViewHolder> {

	public ScrollableLayoutItem(String id) {
		super(id);
	}

	@Override
	public int getSpanSize(int spanCount, int position) {
		return spanCount;
	}

	@Override
	public int getLayoutRes() {
		return R.layout.recycler_scrollable_layout_item;
	}

	@Override
	public LayoutViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new LayoutViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@Override
	public void bindViewHolder(FlexibleAdapter adapter, LayoutViewHolder holder, int position, List payloads) {
		String name = Utils.fromHtmlCompat(getTitle()).toString();
		if(name.length() >5){
			name = name.substring(0,5)+"...";
		}
		holder.mTitle.setText(name);
		holder.mSubtitle.setText(Utils.fromHtmlCompat(getSubtitle()));

		//Support for StaggeredGridLayoutManager
		if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
			((StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams()).setFullSpan(true);
			Log.d("ScrollableLayoutItem", "LayoutItem configured fullSpan for StaggeredGridLayout");
		}
	}

	public static class LayoutViewHolder extends FlexibleViewHolder {

		public TextView mTitle;
		public TextView mSubtitle;

		public LayoutViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter, true);
			mTitle = (TextView) view.findViewById(R.id.title);
			mSubtitle = (TextView) view.findViewById(R.id.subtitle);
		}

		@Override
		public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
			AnimatorHelper.slideInFromTopAnimator(animators, itemView, mAdapter.getRecyclerView());
		}
	}

	@Override
	public String toString() {
		return "ScrollableLayoutItem[" + super.toString() + "]";
	}
}