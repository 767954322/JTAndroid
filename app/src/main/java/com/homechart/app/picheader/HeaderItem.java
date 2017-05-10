package com.homechart.app.picheader;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flexible.flexibleadapter.FlexibleAdapter;
import com.flexible.flexibleadapter.items.AbstractHeaderItem;
import com.flexible.flexibleadapter.items.IFilterable;
import com.flexible.flexibleadapter.items.ISectionable;
import com.flexible.viewholders.FlexibleViewHolder;
import com.homechart.app.R;

import java.util.List;


/**
 * This is a ic_default_head item with custom layout for section headers.
 * <p><b>Note:</b> THIS ITEM IS NOT A SCROLLABLE HEADER.</p>
 * A Section should not contain others Sections and headers are not Sectionable!
 */
public class HeaderItem extends AbstractHeaderItem<HeaderItem.HeaderViewHolder> implements IFilterable {

	private String id;
	private String title;
	private String subtitle;

	public HeaderItem(String id) {
		super();
		this.id = id;
		setDraggable(true);
	}

	@Override
	public boolean equals(Object inObject) {
		if (inObject instanceof HeaderItem) {
			HeaderItem inItem = (HeaderItem) inObject;
			return this.getId().equals(inItem.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public int getSpanSize(int spanCount, int position) {
		return spanCount;
	}

	@Override
	public int getLayoutRes() {
		return R.layout.recycler_header_item;
	}

	@Override
	public HeaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new HeaderViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List payloads) {
		if (payloads.size() > 0) {
			Log.d(this.getClass().getSimpleName(), "HeaderItem " + id + " Payload " + payloads);
		} else {
			holder.mTitle.setText(getTitle());
		}
		List<ISectionable> sectionableList = adapter.getSectionItems(this);
		String subTitle = (sectionableList.isEmpty() ? "Empty section" :
				sectionableList.size() + " section items");
		holder.mSubtitle.setText(subTitle);
	}

	@Override
	public boolean filter(String constraint) {
		return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint);
	}

	static class HeaderViewHolder extends FlexibleViewHolder {

		TextView mTitle;
		TextView mSubtitle;

		HeaderViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter, true);//True for sticky
			mTitle = (TextView) view.findViewById(R.id.title);
			mSubtitle = (TextView) view.findViewById(R.id.subtitle);
			mTitle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.d("HeaderTitle", "Registered internal click on Header TitleTextView! " + mTitle.getText() + " position=" + getFlexibleAdapterPosition());
				}
			});

			//Support for StaggeredGridLayoutManager
			if (itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
				((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
			}
		}
	}

	@Override
	public String toString() {
		return "HeaderItem[id=" + id +
				", title=" + title + "]";
	}

}