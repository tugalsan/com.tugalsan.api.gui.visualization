package com.tugalsan.api.gui.visualization.client;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.visualization.client.*;
import com.google.gwt.visualization.client.events.*;
import com.google.gwt.visualization.client.visualizations.*;

public class TGC_VisualOrgChart {

    public static OrgChart.Options createDefaultOptions() {
        var options = OrgChart.Options.create();
        options.setAllowHtml(true);
        options.setAllowCollapse(true);
//        options.setColor("#00FF00");
//        options.setSelectionColor("#FF0000");
        return options;
    }

    public static AbstractDataTable createDefaultDataTable() {
        var data = DataTable.create();
        data.addColumn(AbstractDataTable.ColumnType.STRING, "Name");
        data.addColumn(AbstractDataTable.ColumnType.STRING, "Manager");
        data.addRows(6);
        data.setValue(0, 0, "Ali<br/>Gel");

        data.setValue(1, 0, "Jim");
        data.setValue(1, 1, "Ali<br/>Gel");

        data.setValue(2, 0, "Alice");
        data.setValue(2, 1, "Ali<br/>Gel");

        data.setValue(3, 0, "Bob");
        data.setValue(3, 1, "Jim");

        data.setValue(4, 0, "Carol");
        data.setValue(4, 1, "Bob");

        data.setValue(5, 0, "Me");
        data.setValue(5, 1, "Bob");
        return data;
    }

    private final OrgChart chart;

    public OrgChart getWidget() {
        return chart;
    }

    public TGC_VisualOrgChart(AbstractDataTable dt, OrgChart.Options op) {
        chart = new OrgChart(dt, op);
        chart.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectHandler.SelectEvent e) {
                TGC_VisualOrgChart.this.onSelect();
            }
        });
    }

    public void onSelect() {

    }

    public void addToRootPanel() {
        RootPanel.get().add(chart);
    }

    public void removeFromRootPanel() {
        RootPanel.get().remove(chart);
    }
}
