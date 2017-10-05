package com.havrylyuk.dou.utils.chart.marker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.havrylyuk.dou.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Custom implementation of the MarkerView
 * Created by Igor Havrylyuk on 01.10.2017.
 */

@SuppressLint("ViewConstructor")
public class ExperienceMarker extends MarkerView {

    private Unbinder unbinder;
    @BindView(R.id.tvContent)
    TextView tvContent;


    public ExperienceMarker(Context context, int layoutResource) {
        super(context, layoutResource);
        unbinder = ButterKnife.bind(this);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(getContext().getString(R.string.salaries_format_salary,
                    (int) ce.getHigh()));
        } else {
            EntryMarkerData data = (EntryMarkerData) e.getData();
            tvContent.setText(getContext().getString(R.string.exp_format_marker,
                    String.valueOf(data.getValue()),
                    String.valueOf(data.getCount())
                    )
            );
        }
        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
        unbinder.unbind();
    }
}
