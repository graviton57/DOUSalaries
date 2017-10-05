package com.havrylyuk.dou.ui.main.demographics;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.havrylyuk.dou.R;
import com.havrylyuk.dou.ui.base.BaseFragment;
import com.havrylyuk.dou.ui.main.SalariesAdapter;
import com.havrylyuk.dou.utils.chart.listviewitems.ChartItem;
import com.havrylyuk.dou.utils.AppUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * DemographicsType Charts Fragment
 * Created by Igor Havrylyuk on 21.09.2017.
 */

public class DemographicFragment extends BaseFragment implements DemographicMvpView,
        SwipeRefreshLayout.OnRefreshListener {

    @Inject
    DemographicMvpPresenter<DemographicMvpView> presenter;

    @Inject
    SalariesAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.txt_error)
    TextView txtError;

    private int periodIndex ;

    public static DemographicFragment newInstance() {
        Bundle args = new Bundle();
        DemographicFragment fragment = new DemographicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        periodIndex = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        getActivityFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.attachView(this);
        initUI();
        return view;
    }

    @Override
    public void showEmptyView() {
        showRecycler(false);
        txtError.setText(R.string.error_no_data);
    }

    @Override
    public void onError(String message) {
        showRecycler(false);
        txtError.setText(message);
    }

    @Override
    protected void updateData(){
        try {
            adapter.clear();
            showRecycler(true);
            final String[] periodKey = getResources().getStringArray(R.array.array_period_date);
            long period = AppUtils.convertDate(periodKey[periodIndex]);
            presenter.loadDemographic(period);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        updateData();
    }

    private void showRecycler(boolean visibility) {
        recyclerView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        txtError.setVisibility(visibility ? View.GONE : View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showSalaries(List<ChartItem> chartItems) {
        if (getActivity() != null && isAdded()) {
            adapter.clear();
            adapter.addChartItems(chartItems);
        }
    }

    @Override
    protected void initUI() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorWhite);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        adapter = new SalariesAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        updateData();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_salaries, menu);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(menu.findItem(R.id.action_category));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_period_date, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(periodIndex);//set prevision selected period
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                switch (spinner.getId()){
                    case R.id.action_category:
                        if (i != periodIndex) {
                            periodIndex = i;
                            updateData();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
