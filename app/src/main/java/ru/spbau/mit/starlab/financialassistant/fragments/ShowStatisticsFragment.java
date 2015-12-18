package ru.spbau.mit.starlab.financialassistant.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ru.spbau.mit.starlab.financialassistant.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowStatisticsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowStatisticsFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowStatisticsFragment newInstance(String param1, String param2) {
        ShowStatisticsFragment fragment = new ShowStatisticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowStatisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ll = inflater.inflate(R.layout.fragment_show_statistics, container, false);

        Bundle args = getArguments();

        LineChart chart = (LineChart) ll.findViewById(R.id.chart);
        List<Entry> values = new ArrayList<>();
        List<String> xVals = new ArrayList<>();

        PieChart pieChart = (PieChart) ll.findViewById(R.id.pie_chart);
        List<Entry> values2 = new ArrayList<>();
        List<String> xVals2 = new ArrayList<>();

        String[] dateList = args.getStringArray("dateList");
        String[] categoryNameList = args.getStringArray("categoryNameList");
        double[] sumList = args.getDoubleArray("sumList");

        Set<String> categories = new HashSet<>();
        Collections.addAll(categories, categoryNameList);

        if (args.getBoolean("isStatistics")) {
            getDialog().setTitle("Статистика");

            String begin = args.getString("dateBegin");
            Calendar beginCal = Calendar.getInstance();

            try {
                beginCal.setTime(sdf.parse(begin));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String end = args.getString("dateEnd");
            Calendar endCal = Calendar.getInstance();
            try {
                endCal.setTime(sdf.parse(end));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int duration = endCal.get(Calendar.YEAR) * 12 * 30 + endCal.get(Calendar.MONTH) * 30 + endCal.get(Calendar.DATE) -
                    (beginCal.get(Calendar.YEAR) * 12 * 30 + beginCal.get(Calendar.MONTH) * 30 + beginCal.get(Calendar.DATE));

            if (duration < 90) {
                for (int i = 0; i < duration + 1; i++) {
                    values.add(new Entry(getSumOnDay(beginCal, i, dateList, sumList), i));
                    xVals.add(i, String.valueOf(getDaysAfter(beginCal, i)));
                }

                int i = 0;
                for (String category : categories) {
                    values2.add(new Entry(getSumCategoryOnPeriod(beginCal, endCal, category, categoryNameList, dateList, sumList), i++));
                    xVals2.add(category);
                }

                updateLineChart(chart, values, xVals, "расходы по дням");
                updatePieChart(pieChart, values2, xVals2, "");
            } else {
                for (int i = 0; i < endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH) +
                        (endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR)) * 12 + 1; i++) {
                    values.add(new Entry(getSumOnMonth(beginCal, i, dateList, sumList), i));
                    xVals.add(i, String.valueOf(getMonth(beginCal.get(Calendar.MONTH) + i)));
                }

                int i = 0;
                for (String category : categories) {
                    int sum = getSumCategoryOnPeriod(beginCal, endCal, category, categoryNameList, dateList, sumList);
                    if (sum > 0) {
                        values2.add(new Entry(sum, i++));
                        xVals2.add(category);
                    }
                }

                updateLineChart(chart, values, xVals, "расходы по месяцам");
                updatePieChart(pieChart, values2, xVals2, "");
            }
        } else {
            getDialog().setTitle("Прогнозы");

            List<Double> prevExpenses = new ArrayList<>();

            prevExpenses.add(0.0);

            Calendar curCal = findMinDate(dateList);
            curCal.set(Calendar.DATE, 1);

            Calendar todayCal = Calendar.getInstance();
            while (curCal.before(todayCal)) {
                prevExpenses.add(Math.max(1.0, getSumOnMonth(curCal, 0, dateList, sumList)));
                curCal.add(Calendar.MONTH, 1);
            }

            /*
            for (int i = 0; i < 50; i++) {
                if (i % 2 == 0) {
                    prevExpenses.add((double) i * i);
                } else {
                    prevExpenses.add((double) Math.max(10, 1000 - i * i));
                }
            }
*/

            List<List<Double>> categories1 = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<Double> list = new ArrayList<>();
                for (int j = 0; j < 100; j++) {
                    list.add((double) Math.max(100, 10 * i + j * (i % 2) - j * ((i + 1) % 2)));
                }
                categories1.add(list);
            }

            for (int i = 0; i < 12; i++) {
                values.add(new Entry(extrapolate(prevExpenses), i));
                xVals.add(i, getMonth(todayCal.get(Calendar.MONTH) + i));
            }

            for (int i = 0; i < prevExpenses.size(); i++) {
                System.err.println(prevExpenses.get(i));
            }
            for (int i = 0; i < 5; i++) {
                values2.add(new Entry(extrapolate(categories1.get(i)), i));
            }
            xVals2.add("еда");
            xVals2.add("кот");
            xVals2.add("железо");
            xVals2.add("транспорт");
            xVals2.add("что-то еще");

            updateLineChart(chart, values, xVals, "прогноз расходов по месяцам");
            updatePieChart(pieChart, values2, xVals2, "");
        }

        // Inflate the layout for this fragment
        return ll;
    }

    private Calendar findMinDate(String[] dates) {
        Calendar res = Calendar.getInstance();
        try {
            res.setTime(sdf.parse(dates[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < dates.length; i++) {
            Calendar calDate = Calendar.getInstance();
            try {
                calDate.setTime(sdf.parse(dates[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (calDate.before(res)) {
                res = calDate;
            }
        }
        return res;
    }

    private int getSumOnDay(Calendar cal, int x, String[] dates, double[] sums) {
        int res = 0;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(cal.getTime());
        cal1.add(Calendar.DATE, x);
        for (int i = 0; i < dates.length; i++) {
            Calendar calDate = Calendar.getInstance();
            try {
                calDate.setTime(sdf.parse(dates[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (calDate.equals(cal1)) {
                res += sums[i];
            }

        }
        return res;
    }

    private int getSumCategoryOnPeriod(Calendar beginCal, Calendar endCal, String category, String[] categories, String[] dates, double[] sums) {
        int res = 0;

        for (int i = 0; i < dates.length; i++) {
            if (categories[i].equals(category)) {
                System.err.println("WTF!!!");
                System.err.println(dates[i]);
                Calendar cal1 = Calendar.getInstance();
                try {
                    cal1.setTime(sdf.parse(dates[i]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.err.println(beginCal.before(cal1));
                System.err.println(cal1.before(endCal));

                if (beginCal.getTimeInMillis() <= cal1.getTimeInMillis() && cal1.getTimeInMillis() <= endCal.getTimeInMillis()) {
                    res += sums[i];
                }
            }
        }
        return res;
    }

    int getSumOnMonth(Calendar beginCal, int x, String[] dates, double[] sums) {
        int res = 0;
        Calendar startPeriodCal = Calendar.getInstance();
        startPeriodCal.setTime(beginCal.getTime());
        startPeriodCal.add(Calendar.MONTH, x);
        Calendar endPeriodCal = Calendar.getInstance();
        endPeriodCal.setTime(beginCal.getTime());
        endPeriodCal.add(Calendar.MONTH, x + 1);

        for (int i = 0; i < dates.length; i++) {
            Calendar cal1 = Calendar.getInstance();
            try {
                cal1.setTime(sdf.parse(dates[i]));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (startPeriodCal.getTimeInMillis() <= cal1.getTimeInMillis() && cal1.getTimeInMillis() < endPeriodCal.getTimeInMillis()) {
                res += sums[i];
            }
        }
        return res;
    }

    private void updateLineChart(LineChart chart, List<Entry> values, List<String> xValues, String name) {
        LineDataSet dataSet = new LineDataSet(values, name);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(xValues, dataSets);
        chart.setData(data);
        chart.setDescription("");

        chart.invalidate();
    }

    private void updatePieChart(PieChart chart, List<Entry> values, List<String> xValues, String name) {
        PieDataSet pieDataSet = new PieDataSet(values, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(xValues, pieDataSet);

        chart.setDescription(name);
        chart.setHoleRadius(40);
        chart.setData(pieData);

        chart.invalidate();
    }

    private int extrapolate(List<Double> list) {
        int x = list.size();
        double res;

        List<Double> prefSums = new ArrayList<>();
        prefSums.add(0.0);
        for (int i = 1; i < x; i++) {
            prefSums.add(prefSums.get(i - 1) + list.get(i));
        }

        if (x <= 1) {
            res = 0;
        } else {
            if (x <= 13) {
                res = (prefSums.get(x - 1)) / (x - 1);
            } else {
                if (x <= 26) {
                    res = (prefSums.get(x - 1) - prefSums.get(12)) * (list.get(x - 12)) / (prefSums.get(x - 13));
                } else {
                    if (x <= 39) {
                        res = (prefSums.get(x - 1) - prefSums.get(24)) *
                                ((list.get(x - 24)) / (prefSums.get(x - 25)) +
                                        (list.get(x - 12)) / (prefSums.get(x - 13) - prefSums.get(12))) / 2;
                    } else {
                        if (x <= 52) {
                            res = (prefSums.get(x - 1) - prefSums.get(36)) *
                                    ((list.get(x - 36)) / (prefSums.get(x - 37)) +
                                            (list.get(x - 24)) / (prefSums.get(x - 25) - prefSums.get(12)) +
                                            (list.get(x - 12)) / (prefSums.get(x - 13) - prefSums.get(24))) / 3;
                        } else {
                            if (x <= 65) {
                                res = (prefSums.get(x - 1) - prefSums.get(48)) *
                                        ((list.get(x - 48)) / (prefSums.get(x - 49)) +
                                                (list.get(x - 36)) / (prefSums.get(x - 37) - prefSums.get(12)) +
                                                (list.get(x - 24)) / (prefSums.get(x - 25) - prefSums.get(24)) +
                                                (list.get(x - 12)) / (prefSums.get(x - 13) - prefSums.get(36))) / 4;
                            } else {
                                res = (prefSums.get(x - 1) - prefSums.get(60)) *
                                        ((list.get(x - 60)) / (prefSums.get(x - 61)) +
                                                (list.get(x - 48)) / (prefSums.get(x - 49) - prefSums.get(12)) +
                                                (list.get(x - 36)) / (prefSums.get(x - 37) - prefSums.get(24)) +
                                                (list.get(x - 24)) / (prefSums.get(x - 25) - prefSums.get(36)) +
                                                (list.get(x - 12)) / (prefSums.get(x - 13) - prefSums.get(48))) / 5;
                            }
                        }
                    }
                }
            }
        }

        list.add(res);
        return (int) res;
    }

    private String getMonth(int x) {
        String[] months = {"дек", "янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя"};
        return months[x % 12];
    }

    private String getDaysAfter(Calendar beginCal, int x) {
        Calendar c = Calendar.getInstance();
        c.setTime(beginCal.getTime());
        c.add(Calendar.DATE, x);
        return String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + getMonth(c.get(Calendar.MONTH) + 1);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
