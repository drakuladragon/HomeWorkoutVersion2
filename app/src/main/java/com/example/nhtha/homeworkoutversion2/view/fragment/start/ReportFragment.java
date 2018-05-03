package com.example.nhtha.homeworkoutversion2.view.fragment.start;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nhtha.homeworkoutversion2.R;
import com.example.nhtha.homeworkoutversion2.view.activity.StartActivity;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

/**
 * Created by nhtha on 05-Mar-18.
 */

public class ReportFragment extends Fragment {

    private LinearLayout chartConsumeContainer;
    private ImageView imgMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        addControls();
        addEvents();
    }

    private void init() {
        imgMenu = getView().findViewById(R.id.img_menu);

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StartActivity) getActivity()).openDrawer();
            }
        });
    }

    private void addEvents() {
    }

    private void addControls() {

        int[] x_value = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] y_value = {10, 20, 10, 30, 45, 20, 40, 30};

        chartConsumeContainer = getView().findViewById(R.id.chart_consume);
        XYMultipleSeriesDataset xyConsumeSeriesDataset = makeSeriesData(x_value, y_value);
        XYMultipleSeriesRenderer consumeRendered = makeSeriesRender("Biểu đồ lượng calo tiêu thụ", "Thời gian", "Kcal");
        View chartConsume = ChartFactory.getLineChartView(this.getContext(),
                xyConsumeSeriesDataset,
                consumeRendered);
        chartConsumeContainer.addView(chartConsume);

    }

    private XYMultipleSeriesRenderer makeSeriesRender(String chartTitle, String xTitle, String yTitle) {
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
//        multiRenderer.setXLabels(100);
        //tiêu đề
        multiRenderer.setChartTitle(chartTitle);
        multiRenderer.setXTitle(xTitle);
        multiRenderer.setXLabels(10);
        multiRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        multiRenderer.setYLabels(10);
        multiRenderer.setYTitle(yTitle);
        //cỡ chữ
        multiRenderer.setAxisTitleTextSize(30);
        multiRenderer.setChartTitleTextSize(30);
        multiRenderer.setLabelsTextSize(30);
        multiRenderer.setLegendTextSize(50);
        //màu sẵc
        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setMarginsColor(Color.WHITE);
        multiRenderer.setGridColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0, Color.BLACK);
        //hiển thị
        multiRenderer.setZoomEnabled(true);
        multiRenderer.setShowGrid(true);
        // multiRenderer.setLabelsTextSize(40);
        multiRenderer.setZoomButtonsVisible(true);


        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.BLACK);
        renderer.setPointStyle(PointStyle.DIAMOND);
        renderer.setFillPoints(true);
        renderer.setLineWidth(3);
//        renderer.setDisplayChartValues(true);
        multiRenderer.addSeriesRenderer(renderer);
        return multiRenderer;
    }

    private XYMultipleSeriesDataset makeSeriesData(int[] x_value, int[] y_value) {
        XYMultipleSeriesDataset xyMultipleSeriesDataset = new XYMultipleSeriesDataset();
        XYSeries expenseSeries = new XYSeries("");
        for (int i = 0; i < x_value.length; i++) {
            expenseSeries.add(x_value[i], y_value[i]);
        }
        xyMultipleSeriesDataset.addSeries(expenseSeries);
        return xyMultipleSeriesDataset;
    }

}
