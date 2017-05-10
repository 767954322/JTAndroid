package com.homechart.app.picheader;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flexible.flexibleadapter.FlexibleAdapter;
import com.flexible.flexibleadapter.helpers.AnimatorHelper;
import com.flexible.flexibleadapter.items.IFilterable;
import com.flexible.flexibleadapter.items.ISectionable;
import com.flexible.flexibleadapter.utils.DrawableUtils;
import com.flexible.viewholders.FlexibleViewHolder;
import com.homechart.app.R;

import java.io.Serializable;
import java.util.List;

/**
 * You should extend directly from
 * {@link com.flexible.flexibleadapter.items.AbstractFlexibleItem} to benefit of the already
 * implemented methods (getter and setters).
 */
public class SimpleItem extends AbstractItem<SimpleItem.SimpleViewHolder>
        implements ISectionable<SimpleItem.SimpleViewHolder, HeaderItem>, IFilterable, Serializable {

    /* The ic_default_head of this item */
    HeaderItem header;
    private SimpleItem.CityOnClick onClick;


    private SimpleItem(String id) {
        super(id);
        setDraggable(true);
        setSwipeable(true);
    }

    public SimpleItem(String id, HeaderItem header, SimpleItem.CityOnClick onClick) {
        this(id);
        this.header = header;
        this.onClick = onClick;
    }

    @Override
    public String getSubtitle() {
        return getId();
    }

    @Override
    public HeaderItem getHeader() {
        return header;
    }

    @Override
    public void setHeader(HeaderItem header) {
        this.header = header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recycler_simple_item;
    }

    @Override
    public SimpleViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new SimpleViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public void bindViewHolder(final FlexibleAdapter adapter, final SimpleViewHolder holder, int position, List payloads) {
        Context context = holder.itemView.getContext();
        holder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.cityOnClick(holder.mSubtitle.getText().toString(), holder.mTitle.getText().toString());
            }
        });
        // Background, when bound the first time
        if (payloads.size() == 0) {
            Drawable drawable = DrawableUtils.getSelectableBackgroundCompat(
                    Color.WHITE, Color.parseColor("#dddddd"), //Same color of divider
                    DrawableUtils.getColorControlHighlight(context));
            DrawableUtils.setBackgroundCompat(holder.itemView, drawable);
            DrawableUtils.setBackgroundCompat(holder.frontView, drawable);
        }

        // DemoApp: INNER ANIMATION EXAMPLE! ImageView - Handle Flip Animation
//		if (adapter.isSelectAll() || adapter.isLastItemInActionMode()) {
//			// Consume the Animation
//			holder.mFlipView.flip(adapter.isSelected(position), 200L);
//		} else {
        // Display the current flip status
//		}

        // In case of searchText matches with Title or with a field this will be highlighted
        if (adapter.hasSearchText()) {
            com.flexible.flexibleadapter.utils.Utils.highlightText(holder.mTitle, getTitle(), adapter.getSearchText());
            com.flexible.flexibleadapter.utils.Utils.highlightText(holder.mSubtitle, getSubtitle(), adapter.getSearchText());
        } else {
            holder.mTitle.setText(getTitle());
            holder.mSubtitle.setText(getSubtitle());
        }
    }

    @Override
    public boolean filter(String constraint) {
        return getTitle() != null && getTitle().toLowerCase().trim().contains(constraint) ||
                getSubtitle() != null && getSubtitle().toLowerCase().trim().contains(constraint);
    }

    final class SimpleViewHolder extends FlexibleViewHolder {

        Button mTitle;
        TextView mSubtitle;
        Context mContext;
        View frontView;
        View rearLeftView;
        View rearRightView;

        public boolean swiped = false;

        SimpleViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            this.mContext = view.getContext();
            this.mTitle = (Button) view.findViewById(R.id.title);
            this.mSubtitle = (TextView) view.findViewById(R.id.subtitle);
            this.mSubtitle.setVisibility(View.GONE);

            this.frontView = view.findViewById(R.id.front_view);
            this.rearLeftView = view.findViewById(R.id.rear_left_view);
            this.rearRightView = view.findViewById(R.id.rear_right_view);
        }

        @Override
        protected void setDragHandleView(@NonNull View view) {
            if (mAdapter.isHandleDragEnabled()) {
                view.setVisibility(View.VISIBLE);
                super.setDragHandleView(view);
            } else {
                view.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {

            super.onClick(view);
        }

        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(mContext, "LongClick on " + mTitle.getText() + " position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            return super.onLongClick(view);
        }

        @Override
        public void toggleActivation() {
            super.toggleActivation();
            // Here we use a custom Animation inside the ItemView
        }

        @Override
        public float getActivationElevation() {
            return com.homechart.app.picheader.Utils.dpToPx(itemView.getContext(), 4f);
        }

        @Override
        protected boolean shouldActivateViewWhileSwiping() {
            return false;//default=false
        }

        @Override
        protected boolean shouldAddSelectionInActionMode() {
            return false;//default=false
        }

        @Override
        public View getFrontView() {
            return frontView;
        }

        @Override
        public View getRearLeftView() {
            return rearLeftView;
        }

        @Override
        public View getRearRightView() {
            return rearRightView;
        }

        @Override
        public void scrollAnimators(@NonNull List<Animator> animators, int position, boolean isForward) {
            if (mAdapter.getRecyclerView().getLayoutManager() instanceof GridLayoutManager ||
                    mAdapter.getRecyclerView().getLayoutManager() instanceof StaggeredGridLayoutManager) {
                if (position % 2 != 0)
                    AnimatorHelper.slideInFromRightAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
                else
                    AnimatorHelper.slideInFromLeftAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
            } else {
                //Linear layout
                if (mAdapter.isSelected(position))
                    AnimatorHelper.slideInFromRightAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
                else
                    AnimatorHelper.slideInFromLeftAnimator(animators, itemView, mAdapter.getRecyclerView(), 0.5f);
            }
        }

        @Override
        public void onItemReleased(int position) {
            swiped = (mActionState == ItemTouchHelper.ACTION_STATE_SWIPE);
            super.onItemReleased(position);
        }
    }

    @Override
    public String toString() {
        return "SimpleItem[" + super.toString() + "]";
    }

    public interface CityOnClick {
        void cityOnClick(String position, String name);
    }

}