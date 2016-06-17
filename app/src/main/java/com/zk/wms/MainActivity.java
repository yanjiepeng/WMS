package com.zk.wms;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zk.bean.BinInfo;
import com.zk.database.MyTask;
import com.zk.database.MyTaskInterface;
import com.zk.database.SqlUtil;
import com.zk.database.TAG;
import com.zk.event.EventAA;
import com.zk.service.UpdateService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BarChart left_chart_1, left_chart_2, left_chart_3;
    private BarChart right_chart_1, right_chart_2, right_chart_3;
    private TextView tv_warehouse_id, tv_warehouse_xy, tv_notice_1, tv_notice_2, tv_notice_3;

    protected String[] column = new String[]{
            "1列", "2列", "3列", "4列", "5列", "6列", "7列", "8列"
    };
    protected String[] index = new String[]{"第一层", "第二层", "第三层", "第四层"};
    private Typeface mTf;
    private List<BarChart> chartList;
    private Gson gson = new Gson();
    private List<BinInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initWidget();
        Intent intent = new Intent(MainActivity.this, UpdateService.class);
        startService(intent);
        EventBus.getDefault().register(this);
        data = new ArrayList<BinInfo>();





    }

    /*
      eventBus回调 主线程
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventAA eventAA) {
        if (eventAA.getActionType() == EventAA.ACTION_SEND_MSG) {

            //此处为eventbus回调 解析数据并更新界面
            String result = eventAA.getMessage();
            if (result.equals("error")) {
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else if (!result.isEmpty()) {
                //解析并更新界面
                data.clear();
                data = gson.fromJson(result, new TypeToken<List<BinInfo>>() {
                }.getType());
                for (int i = 0 ; i < data.size(); i ++) {
                    data.get(i).setId(i);
                }
                Log.w("data size", data.size() + "");
                initChart();
            }
        }
    }

    private void initWidget() {
        chartList = new ArrayList<BarChart>();
        left_chart_1 = (BarChart) findViewById(R.id.chart_left_1);
        left_chart_2 = (BarChart) findViewById(R.id.chart_left_2);
        left_chart_3 = (BarChart) findViewById(R.id.chart_left_3);

        right_chart_1 = (BarChart) findViewById(R.id.chart_right_1);
        right_chart_2 = (BarChart) findViewById(R.id.chart_right_2);
        right_chart_3 = (BarChart) findViewById(R.id.chart_right_3);


        chartList.add(left_chart_1);
        chartList.add(left_chart_2);
        chartList.add(left_chart_3);
        chartList.add(right_chart_1);
        chartList.add(right_chart_2);
        chartList.add(right_chart_3);


        tv_notice_1 = (TextView) findViewById(R.id.tv_notice1);
        tv_notice_2 = (TextView) findViewById(R.id.tv_notice2);
        tv_notice_3 = (TextView) findViewById(R.id.tv_notice3);
        tv_warehouse_id = (TextView) findViewById(R.id.tv_warehouse_id);
        tv_warehouse_xy = (TextView) findViewById(R.id.tv_warehouse_xy);
        tv_notice_1.setTextSize(18f);
        tv_notice_1.setText("点击可查看每个位置的详细情况");

    }

    /*
    初始化图表
     */
    private void initChart() {

        int id = 0;
        for (BarChart b : chartList
                ) {

            b.setOnChartValueSelectedListener(new MyOnChartValueSelectedListener(id++));
            b.setDrawBarShadow(false);
            b.setDrawValueAboveBar(false);
            b.setScaleEnabled(false);
            b.setDescription("");

            b.setMaxVisibleValueCount(30);
            b.setPinchZoom(false);
            b.setNoDataText("空");
            b.setDrawGridBackground(true); //背景是否有格子
            b.getAxisLeft().setDrawGridLines(false);
            b.getXAxis().setDrawGridLines(false);
            b.getAxisRight().setEnabled(false);
            b.getAxisLeft().setEnabled(false);
            //设置y最大值为1
            b.getAxisLeft().setAxisMaxValue(1f);

            Legend mLegend = b.getLegend();
            mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
            mLegend.setFormSize(5f);
        }
        if (data!=null && !data.isEmpty()) {
            BarData data_left1 = initChartContentleft(1);
            BarData data_left2 = initChartContentleft(2);
            BarData data_left3 = initChartContentleft(3);
            BarData data_right1 = initChartContentRight(1);
            BarData data_right2 = initChartContentRight(2);
            BarData data_right3 = initChartContentRight(3);

            chartList.get(0).setData(data_left1);
            chartList.get(1).setData(data_left2);
            chartList.get(2).setData(data_left3);
            chartList.get(3).setData(data_right1);
            chartList.get(4).setData(data_right2);
            chartList.get(5).setData(data_right3);
            for (int i = 0; i < chartList.size(); i++) {
                chartList.get(i).animateX(0);
            }
        }
    }

    /*
     初始化柱状图数据
     */
    private BarData initChartContentleft(int index) {
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        ArrayList<String> xValues = new ArrayList<String>();

        //初始化标签 x轴
        for (int i = 0; i < column.length; i++) {
            xValues.add(column[i]);
        }

        //初始化y轴数据

        if (index == 1) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }
        if (index == 2) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(8+i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }
        if (index == 3) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(16+i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "第" + index + "层[左]");
        //取整
        barDataSet.setValueFormatter(new MyvalueFomatter());

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSet.setDrawValues(false);
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;

    }


    private BarData initChartContentRight(int index ) {
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        ArrayList<String> xValues = new ArrayList<String>();
        //初始化标签 x轴
        for (int i = 0; i < column.length; i++) {
            xValues.add(column[i]);
        }

        //初始化y轴数据

        if (index == 1) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(24+i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }
        if (index ==2) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(32+i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }
        if (index ==3) {
            for (int i = 0; i < column.length; i++) {
                if (!data.get(40+i).isEmpty()) {
                    float value = 1f;
                    yValues.add(new BarEntry(value, i));
                }else {
                    yValues.add(new BarEntry(0f , i));
                }
            }
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "第"+index % 4+"层[右]");
        barDataSet.setValueFormatter(new MyvalueFomatter());
        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(xValues, barDataSets);
        return barData;

    }

    /*
       初始化actionbar
     */
    private void initActionBar() {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.titlebar);
        actionBar.setIcon(R.mipmap.mjlogo);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    }


    /*
        格式化数据
     */
    class MyvalueFomatter implements ValueFormatter {


        DecimalFormat mFormat;

        public MyvalueFomatter() {
            mFormat = new DecimalFormat("0");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value);
        }
    }

    /*
      图表点击监听
     */
    class MyOnChartValueSelectedListener implements OnChartValueSelectedListener {

        int chartid;

        public MyOnChartValueSelectedListener(int chartid) {

            this.chartid = chartid;
        }

        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            Log.w("点击", e.getXIndex() + "列" + chartid + "行");

            ShowInfoDialog(chartid, e.getXIndex());


        }

        @Override
        public void onNothingSelected() {

            //无选中时

        }
    }

    /*
      弹出展示详细信息的对话框
     */
    private void ShowInfoDialog(int chartid, int index) {

        DetailInfoThread thread = new DetailInfoThread();
        thread.setIndex(index);
        thread.setChartId(chartid);
        thread.start();

    }

    /*
      查询详情线程
     */
    class DetailInfoThread extends Thread {

        int chartId, index;

        public DetailInfoThread() {
        }

        public void setChartId(int chartId) {
            this.chartId = chartId;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            super.run();
            //此处根据立体库编号和列数请求详细信息

            runOnUiThread(new Runnable() {
                              String[] row = {"第1行[左]", "第2行[左]", "第3行[左]"
                              ,"第6行[右]","第5行[右]","第4行[右]"};
                    String colum = String.valueOf(index +1);
                    String info = "库位编号:1 \t " + "货位编号"+data.get((chartId) * 8 + index).getId() +
                             "\t"+"行:" + row[chartId] + " \t" +
                            "列:" + colum  + " \t" +
                            "类型：" + data.get((chartId) * 8 + index).getDesc()+" \t";

                @Override
                public void run() {
                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.dialog_layout);
                    TextView tv_title = (TextView) window.findViewById(R.id.tv_dialog_title);
                    tv_title.setText("详细信息");
                    TextView tv_message = (TextView) window.findViewById(R.id.tv_dialog_message);
                    tv_message.setText(info);
                    TextView btn_dismiss = (TextView) window.findViewById(R.id.btn_dismiss);
                    btn_dismiss.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent();
        intent.setAction("AAAA");
        intent.putExtra("cmd", com.zk.database.TAG.CMD_STOP_SERVICE);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Intent intent = new Intent();
        intent.setAction("AAAA");
        intent.putExtra("cmd", com.zk.database.TAG.CMD_STOP_SERVICE);
    }
}
