package com.zk.wms;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.zk.database.MyTask;
import com.zk.database.MyTaskInterface;
import com.zk.database.SqlUtil;
import com.zk.database.TAG;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private BarChart left_chart_1 , left_chart_2 , left_chart_3 ,left_chart_4 ;
    private BarChart right_chart_1,right_chart_2,right_chart_3,right_chart_4;
    private TextView tv_warehouse_id , tv_warehouse_xy , tv_notice_1 ,tv_notice_2 , tv_notice_3;

    protected String[] column = new String[] {
            "1列", "2列", "3列", "4列", "5列", "6列", "7列", "8列", "9列", "10列", "11列", "12列","13列","14列" , "15列" ,"16列" , "17列" , "18列","19列","20列","21列","22列","23列","24列"
    };
    protected String[] index  = new String[]{"第一层" , "第二层" , "第三层" , "第四层"};
    private Typeface mTf;
    private List<BarChart> chartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActionBar();
        initWidget();
        initChart();


        /*
         异步连接数据库
         */
        MyTask myTask = new MyTask(new MyTaskInterface() {
            @Override
            public String[] doBackGround() {

                SqlUtil.OnConn();
                return  null;
            }

            @Override
            public void doUi() {

                if (com.zk.database.TAG.MYSQL_CONNECT_FLAG) {
                    Log.w("SQL","连接成功");
                }else {
                    Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        myTask.execute("conn");
    }

    private void initWidget() {
        chartList = new ArrayList<BarChart>();
        left_chart_1 = (BarChart) findViewById(R.id.chart_left_1);
        left_chart_2 = (BarChart) findViewById(R.id.chart_left_2);
        left_chart_3 = (BarChart) findViewById(R.id.chart_left_3);
        left_chart_4 = (BarChart) findViewById(R.id.chart_left_4);

        right_chart_1 = (BarChart) findViewById(R.id.chart_right_1);
        right_chart_2 = (BarChart) findViewById(R.id.chart_right_2);
        right_chart_3 = (BarChart) findViewById(R.id.chart_right_3);
        right_chart_4 = (BarChart) findViewById(R.id.chart_right_4);


        chartList.add(left_chart_1);
        chartList.add(left_chart_2);
        chartList.add(left_chart_3);
        chartList.add(left_chart_4);
        chartList.add(right_chart_1);
        chartList.add(right_chart_2);
        chartList.add(right_chart_3);
        chartList.add(right_chart_4);



        tv_notice_1 = (TextView) findViewById(R.id.tv_notice1);
        tv_notice_2 = (TextView) findViewById(R.id.tv_notice2);
        tv_notice_3 = (TextView) findViewById(R.id.tv_notice3);
        tv_warehouse_id = (TextView) findViewById(R.id.tv_warehouse_id);
        tv_warehouse_xy = (TextView) findViewById(R.id.tv_warehouse_xy);

    }

    /*
    初始化图表
     */
    private void initChart() {

        for ( BarChart b:chartList
             ) {

            b.setOnChartValueSelectedListener(this);
            b.setDrawBarShadow(false);
            b.setDrawValueAboveBar(false);
            b.setScaleEnabled(false);
            b.setDescription("");

            b.setMaxVisibleValueCount(30);
            b.setPinchZoom(false);
            b.setNoDataText("空");
            b.setDrawGridBackground(false); //背景是否有格子

            Legend mLegend = b.getLegend();
            mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
            mLegend.setFormSize(5f);
        }
        BarData  data_left1 =   initChartContentleft(1);
        BarData  data_left2 =   initChartContentleft(2);
        BarData  data_left3 =   initChartContentleft(3);
        BarData  data_left4 =   initChartContentleft(4);
        BarData  data_right = initChartContentRight();

        chartList.get(0).setData(data_left1);
        chartList.get(1).setData(data_left2);
        chartList.get(2).setData(data_left3);
        chartList.get(3).setData(data_left4);
        chartList.get(4).setData(data_right);
        chartList.get(5).setData(data_right);
        chartList.get(6).setData(data_right);
        chartList.get(7).setData(data_right);
        for (int i = 0; i <8 ; i++) {
            chartList.get(i).animateX(2000);
        }
    }


    private BarData initChartContentleft(int index) {
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
         ArrayList<String> xValues = new ArrayList<String>();

        //初始化标签 x轴
        for (int i = 0; i < column.length ; i++) {
            xValues.add(column[i]);
        }

        //初始化y轴数据

        for (int i = 0; i < column.length; i++) {
            float value = (float) (Math.random() * 100/*100以内的随机数*/) + 3;

            yValues.add(new BarEntry(value, i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "第"+index+"层");
        //取整
        barDataSet.setValueFormatter(new MyvalueFomatter());

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues ,barDataSets);

        return barData;

    }


    private BarData initChartContentRight() {
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        ArrayList<String> xValues = new ArrayList<String>();
        //初始化标签 x轴
        for (int i = 0; i < column.length ; i++) {
            xValues.add(column[i]);
        }

        //初始化y轴数据

        for (int i = 0; i < column.length; i++) {
            float value = i + 10;
            yValues.add(new BarEntry(value, i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "第四层");

        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<IBarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues ,barDataSets);

        return barData;

    }

    /*
       初始化actionbar
     */
    private void initActionBar() {

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.titlebar);
        actionBar.setIcon(R.drawable.zklogo);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

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
}
