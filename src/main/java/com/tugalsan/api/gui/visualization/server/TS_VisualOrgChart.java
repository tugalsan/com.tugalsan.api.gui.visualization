package com.tugalsan.api.gui.visualization.server;

import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.random.client.TGS_RandomUtils;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.tuple.client.TGS_Tuple2;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TS_VisualOrgChart {

    final private static TS_Log d = TS_Log.of(true, TS_VisualOrgChart.class);

    private TS_VisualOrgChart() {
    }

    public static TS_VisualOrgChart of() {
        return new TS_VisualOrgChart();
    }
    final private List<TGS_Tuple2<String, String>> swapParentId_from_to = new ArrayList();
    final private List<TGS_Tuple2<TS_VisualOrgChart_ConfigBalloon, TS_VisualOrgChart_ConfigPlacement>> items = new ArrayList();

    public TS_VisualOrgChart add(TS_VisualOrgChart_ConfigBalloon balloonConfig) {
        return add(balloonConfig, null);
    }

    public TS_VisualOrgChart add(TS_VisualOrgChart_ConfigBalloon balloonConfig, TS_VisualOrgChart_ConfigPlacement placementConfig) {
        items.add(TGS_Tuple2.of(balloonConfig, placementConfig));
        return this;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(preScript());
        items.forEach(item -> sb.append(balloonScript(item.value0, item.value1)));
        sb.append(pstScript());
        return sb.toString();
    }

    private String preScript() {
        return """
                    <style>
                        table {
                            border-collapse: separate;
                        }
                    </style>
                    <script type='text/javascript' src='https://www.google.com/jsapi'></script>
                    <script type='text/javascript'>
                    google.load('visualization', '1', {packages:['orgchart']});
                    google.setOnLoadCallback(drawChart);
                    function drawChart() {
                        var data = new google.visualization.DataTable();
                        data.addColumn('string', 'Name');
                        data.addColumn('string', 'Manager');
                        data.addColumn('string', 'ToolTip');
                        const rows = [
                    """;
    }

    //TODO: swapParentId_from_to NOT WORKUNG 
    public String balloonScript(TS_VisualOrgChart_ConfigBalloon balloonConfig, TS_VisualOrgChart_ConfigPlacement placementConfig) {
        //MUTABLE BALLON
        var balloonConfigMutable = balloonConfig.cloneIt();
        //SWAP PARENT ID TO A HIDDEN ONE IF childerenTreeVerticalDown proccessed before
        var swapParentId = swapParentId_from_to.stream()
                .filter(ft -> Objects.equals(ft.value0, balloonConfigMutable.parentId))
                .findAny().orElse(null);
        if (swapParentId != null) {
            balloonConfigMutable.parentId = swapParentId.value1;
        }
        //CONSTRUCT ballonsPre
        List<TS_VisualOrgChart_ConfigBalloon> balloonConfigsPre = new ArrayList();
        if (placementConfig != null && placementConfig.currentBalloonVerticalDown > 0) {
            var hidemeParentId = balloonConfigMutable.parentId;
            var hidemeId = "";
            for (var i = 0; i < placementConfig.currentBalloonVerticalDown; i++) {
                hidemeId = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
                balloonConfigsPre.add(TS_VisualOrgChart_ConfigBalloon.of(
                        hidemeId, 
                        hidemeParentId, 
                        "left_" + placementConfig.leftPx + hidemeId, 
                        "", 
                        ""
                ));
                hidemeParentId = hidemeId;
            }
            balloonConfigMutable.parentId = hidemeId;
        }
        //CONSTRUCT ballonsPst
        List<TS_VisualOrgChart_ConfigBalloon> balloonConfigsPst = new ArrayList();
        if (placementConfig != null && placementConfig.childerenTreeVerticalDown > 0) {
            var hidemeParentId = balloonConfigMutable.id;
            var hidemeId = "";
            for (var i = 0; i < placementConfig.currentBalloonVerticalDown; i++) {
                hidemeId = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
                balloonConfigsPst.add(TS_VisualOrgChart_ConfigBalloon.of(
                        hidemeId, 
                        hidemeParentId, 
                        "left_" + placementConfig.leftPx + hidemeId, 
                        "", 
                        ""
                ));
                hidemeParentId = hidemeId;
            }
            swapParentId_from_to.add(TGS_Tuple2.of(balloonConfig.id, hidemeParentId));
        }
        //CONSTRUCT ballonsAll
        List<TS_VisualOrgChart_ConfigBalloon> ballonsAll = new ArrayList();
        ballonsAll.addAll(balloonConfigsPre);
        ballonsAll.add(balloonConfigMutable);
        ballonsAll.addAll(balloonConfigsPst);
        ballonsAll.forEach(b -> {
            d.ci("balloonScript", b.id, b.parentId, b.tooltip, b.htmlHeader);
        });
        //POST AS STR
        return TGS_StringUtils.concat(TGS_StreamUtils.toLst(
                ballonsAll.stream().map(b -> balloonScript(b).toString())
        ));
    }

    private StringBuilder balloonScript(TS_VisualOrgChart_ConfigBalloon balloonConfig) {
        balloonConfig = balloonConfig.cloneIt();
        var sb = new StringBuilder();
        if (TGS_StringUtils.isNullOrEmpty(balloonConfig.parentId) || Objects.equals(balloonConfig.parentId.trim(), "0")) {
            balloonConfig.parentId = "";
        }
        sb.append("        [");
        {
            sb.append("{");
            {
                {
                    sb.append("v:'");
                    sb.append(balloonConfig.id);
                    sb.append("'");
                }
                sb.append(", ");
                {
                    sb.append("f:'");
                    {
                        {
                            sb.append("<div style=\"color: var(--colorTextPrimary);\"><B>");
                            sb.append(balloonConfig.htmlHeader);
                            sb.append("</B>");
                        }
                        sb.append("<br/>");
                        {
                            sb.append("<div style=\"color: var(--colorTextSecondary);\">");
                            sb.append(balloonConfig.htmlText);
                            sb.append("</div></div>");
                        }
                    }
                    sb.append("'");
                }
            }
            sb.append("}");
            sb.append(", '").append(balloonConfig.parentId).append("'");
            sb.append(", '").append(balloonConfig.tooltip).append("'");
        }
        sb.append("],");
        sb.append("\n");
        return sb;
    }

    public String pstScript() {
        return """
                        ];
                        data.addRows(rows);
                        for (var i = 0; i< data.getNumberOfRows(); i++){
                            data.setRowProperty(i, 'style', 'background-color:var(--widgetBackground);background-image:none');
                            data.setRowProperty(i, 'selectedStyle', 'background-color:var(--widgetSelected);background-image:none');	
                        }
                        rows.forEach((value, index) => {
                            let tooltip = value[2];
                            if (!tooltip.includes("__hideme")){
                                return;
                            }
                            let left = tooltip.split("_")[1];
                            data.setRowProperty(index, 'style', 'color: transparent; background: transparent; border: 0; border-left: 0.8px solid #3388dd; border-radius: 0; box-shadow: 0 0; position:relative; left: '+left+'px; ');
                        });
                        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
                        chart.draw(data,  {allowHtml:true, allowCollapse:true, size:'large'});
                    }
                    </script>
                    <div id='chart_div'></div>
                    """;
    }

}
