package com.havrylyuk.dou.data;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.havrylyuk.dou.Config;
import com.havrylyuk.dou.DouApp;
import com.havrylyuk.dou.R;
import com.havrylyuk.dou.data.local.db.DouContract;
import com.havrylyuk.dou.data.local.db.DouContract.SalaryEntry;
import com.havrylyuk.dou.data.remote.SalaryApiClient;
import com.havrylyuk.dou.data.remote.SalaryApiService;
import com.havrylyuk.dou.injection.component.DaggerServiceComponent;
import com.havrylyuk.dou.injection.component.ServiceComponent;
import com.havrylyuk.dou.utils.AppUtils;
import com.havrylyuk.dou.utils.events.SyncEvent;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import timber.log.Timber;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class SyncService extends IntentService {

    @Inject
    IDataManager dataManager;

    public SyncService() {
        super("SyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.d("SyncService onHandleIntent()");
        if (intent != null) {
            long lStartTime = System.nanoTime();
            String[] files = getResources().getStringArray(R.array.array_period_date);
            EventBus.getDefault().postSticky(
                    new SyncEvent(SyncEvent.START_SYNC, getString(R.string.sync_start)));
            int deleted = dataManager.deleteAllSalaries();
            Timber.d("SyncService dataManager.deleteAllSalaries()=%d", deleted);
            for (String file : files) {
                /*if (AppUtils.isNetworkAvailable(this)) {
                    saveSalariesFromNetwork(file);//from network repository  load time   30 sec
                }*/
                insetSalariesFromAssets(file);//from assets repository load time  28 sec
            }
            long lEndTime = System.nanoTime();
            long timeElapsed = lEndTime - lStartTime;
            EventBus.getDefault().postSticky(
                    new SyncEvent(SyncEvent.END_SYNC, getString(R.string.sync_complete)));
            Timber.d("End loading content: Duration=  %d  sec.", timeElapsed / 1000000000);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(DouApp.getApplicationComponent())
                .build();
        component.inject(this);
    }

    private void insetSalariesFromAssets(String date){
        try {
            String fileName = AppUtils.getFileNameFromDate(date);
            ContentValues[] cv = parseLocalCsvFile(fileName, date);
            long inserted = dataManager.saveSalaries(cv);
            Timber.d("From period %s inserted salaries count=%d", date, inserted);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveSalariesFromNetwork(String date){
        long inserted = 0;
        SalaryApiService service = SalaryApiClient.getClient().create(SalaryApiService.class);
        try {
            String fileName = AppUtils.getFileNameFromDate(date);
            Call<ResponseBody> responseCall = service.downloadDouFile(fileName);
            ResponseBody responseBody = responseCall.execute().body();
            ContentValues[] cv = parseNetworkCsvData(responseBody.string(), date);
            inserted = dataManager.saveSalaries(cv);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        Timber.d("saveSalariesFromNetwork End inserted data for %s , %d", date ,inserted );
    }

    private ContentValues[] parseNetworkCsvData(String data, String period) throws IOException {
        List<ContentValues> result = new ArrayList<>();
        CSVParser parser = CSVParser.parse(data, CSVFormat.EXCEL.withFirstRecordAsHeader());
        List<CSVRecord> list = parser.getRecords();
        Map<String, Integer> header = parser.getHeaderMap();
        for (CSVRecord record : list) {
            ContentValues cv = new ContentValues();
            for (String s : header.keySet()) {
                for (Map.Entry<String, String> entry : Config.HEADER_SET.entrySet()) {
                    if(entry.getKey().contains(s)){
                        try {
                            if (entry.getValue().equals(SalaryEntry.SALARIES_EXP) ||
                                    entry.getValue().equals(SalaryEntry.SALARIES_CUR_JOB_EXP)){
                                cv.put(entry.getValue(),
                                        AppUtils.calcExperienceOfString(this, record.get(s)));
                            } else if (entry.getValue().equals(SalaryEntry.SALARIES_CITY)){
                                cv.put(entry.getValue(), AppUtils.renameCity(this, record.get(s)));
                            } else  {
                                cv.put(entry.getValue(), record.get(s));
                            }
                        } catch (RuntimeException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                cv.put(DouContract.SalaryEntry.SALARIES_PERIOD, AppUtils.convertDate(period));
            } catch (IllegalArgumentException | UnsupportedOperationException e) {
                e.printStackTrace();
            }
            result.add(cv);
        }
        return result.toArray(new ContentValues[result.size()]);
    }

    private ContentValues[] parseLocalCsvFile(String fileName, String period) throws IOException {
        List<ContentValues> result = new ArrayList<>();
        CSVParser parser = new  CSVParser(new InputStreamReader(getAssets().open("data/"+fileName)),
                CSVFormat.EXCEL.withFirstRecordAsHeader());
        List<CSVRecord> list = parser.getRecords();
        Map<String, Integer> header = parser.getHeaderMap();
        for (CSVRecord record : list) {
            ContentValues cv = new ContentValues();
            for (String s : header.keySet()) {
                for (Map.Entry<String, String> entry : Config.HEADER_SET.entrySet()) {
                    if(entry.getKey().contains(s)){
                        try {
                            cv.put(entry.getValue(), record.get(s));
                        } catch (RuntimeException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                cv.put(DouContract.SalaryEntry.SALARIES_PERIOD, AppUtils.convertDate(period));
            } catch (IllegalArgumentException | UnsupportedOperationException e) {
                e.printStackTrace();
            }
            result.add(cv);
        }
        return result.toArray(new ContentValues[result.size()]);
    }

}
