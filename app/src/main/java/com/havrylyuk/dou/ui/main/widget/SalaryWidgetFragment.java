package com.havrylyuk.dou.ui.main.widget;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.local.model.SalaryDataForWidget;
import com.havrylyuk.dou.ui.base.BaseFragment;
import com.havrylyuk.dou.utils.AppUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Igor Havrylyuk on 21.09.2017.
 */

public class SalaryWidgetFragment extends BaseFragment implements SalaryWidgetMvpView {

    @Inject
    SalaryWidgetMvpPresenter<SalaryWidgetMvpView> presenter;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.spinner_city)
    Spinner citySpinner;
    @BindView(R.id.spinner_job_title)
    Spinner posSpinner;
    @BindView(R.id.spinner_lang)
    Spinner langSpinner;
    @BindView(R.id.q1_salary)
    TextView q1Salary;
    @BindView(R.id.median_salary)
    TextView medianSalary;
    @BindView(R.id.q3_salary)
    TextView q3Salary;
    @BindView(R.id.salaries_count)
    TextView countSalaries;
    @BindView(R.id.experience)
    TextView expTextView;
    @BindView(R.id.seek_bar_exp)
    SeekBar seekExp;
    /*@BindView(R.id.ad_view)
    AdView adView;*/
    @BindView(R.id.salary_surveys)
    TextView salarySurveys;
    @BindView(R.id.salary_details)
    TextView salaryDetails;
    @BindView(R.id.salary_description)
    TextView salaryDescription;

    //default
    private int periodIndex;
    private String city ;
    private String jobTitle ;
    private String language ;
    private int experience;

    public static SalaryWidgetFragment newInstance() {
        Bundle args = new Bundle();
        SalaryWidgetFragment fragment = new SalaryWidgetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        periodIndex = 0;
        city = getString(R.string.city_kiev);
        jobTitle = getString(R.string.software_engineer);
        language = getString(R.string.language_java);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_widget, container, false);
        getActivityFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.attachView(this);
        initUI();
        return view;
    }

    @Override
    public void showSalaries(SalaryDataForWidget data) {
        if (getActivity() != null && isAdded()) {
            updateSalariesUI(data.getQ1(), data.getMedian(), data.getQ3(), data.getSalariesCount());
            showRefreshView(false);
        }
    }

    @Override
    public void showEmptyView() {
        updateSalariesUI(0, 0, 0, 0);
    }

    @Override
    protected void initUI() {
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(1);//set Kiev default city
        citySpinner.setOnItemSelectedListener(onItemSelectedListener);
        ArrayAdapter<CharSequence> langAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.prog_lang, android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSpinner.setAdapter(langAdapter);
        langSpinner.setSelection(1);//set Java default language
        langSpinner.setOnItemSelectedListener(onItemSelectedListener);
        ArrayAdapter<CharSequence> posAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_job_title, android.R.layout.simple_spinner_item);
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        posSpinner.setAdapter(posAdapter);//set Software engineer default job title
        posSpinner.setOnItemSelectedListener(onItemSelectedListener);
        seekExp.setOnSeekBarChangeListener(onSeekBarChangeListener);
        updateExperience();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
        salaryDetails.setText(AppUtils.fromHtml(getString(R.string.link_more_details)));
        salarySurveys.setText(AppUtils.fromHtml(getString(R.string.link_salary_surveys)));
        salaryDescription.setText(AppUtils.fromHtml(getString(R.string.link_max_email)));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorWhite);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        updateData();//load data
    }

    private void showRefreshView(boolean value){
         swipeRefreshLayout.setRefreshing(value);
         if (value){
            updateSalariesUI(0, 0, 0, 0);
        }
    }

    private void updateSalariesUI(int q1, int m, int q3, int count){
        q1Salary.setText(getString(R.string.salaries_format_salary, q1));
        medianSalary.setText(getString(R.string.salaries_format_salary, m));
        q3Salary.setText(getString(R.string.salaries_format_salary, q3));
        countSalaries.setText(getString(R.string.salaries_format_count, count));
    }

    @Override
    protected void updateData(){
        try {
            showRefreshView(true);
            final String[] periodKey = getResources().getStringArray(R.array.array_period_date);
            long period = AppUtils.convertDate(periodKey[periodIndex]);
            presenter.loadSalaries(period, language, city, jobTitle, experience);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    private void updateExperience(){
        if (experience == 0){
            expTextView.setText(getString(R.string.salaries_format_experience,
                    getString(R.string.experience_less_than_year)));
        } else if (experience >=10 ){
            expTextView.setText(getString(R.string.salaries_format_experience, ""));
        } else{
            expTextView.setText(getString(R.string.salaries_format_experience,
                    String.valueOf(experience)));
        }
    }

    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    experience = i;
                    updateExperience();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    updateData();
                }
            };

    private final AdapterView.OnItemSelectedListener onItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
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
                        case R.id.spinner_city:
                            if (!adapterView.getItemAtPosition(i).toString().equals(city)){
                                city = adapterView.getItemAtPosition(i).toString();
                                updateData();
                            }
                            break;
                        case R.id.spinner_lang:
                            if (!adapterView.getItemAtPosition(i).toString().equals(language)){
                                language = adapterView.getItemAtPosition(i).toString();
                                updateData();
                            }
                            break;
                        case R.id.spinner_job_title:
                            if (!adapterView.getItemAtPosition(i).toString().equals(jobTitle)){
                                jobTitle = adapterView.getItemAtPosition(i).toString();
                                if (AppUtils.checkProgramming(jobTitle)){
                                    langSpinner.setSelection(0);//No programming language for QA or Management
                                }
                                updateData();
                            }
                            break;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {}
            };

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
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

}
