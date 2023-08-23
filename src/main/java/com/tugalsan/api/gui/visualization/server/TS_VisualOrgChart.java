package com.tugalsan.api.gui.visualization.server;

import com.tugalsan.api.random.client.TGS_RandomUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TS_VisualOrgChart {

    public static String preScript() {
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

    public static StringBuilder balloonScript(TS_VisualOrgChart_ConfigBalloon balloonConfig, TS_VisualOrgChart_ConfigPlacement placementConfig) {
        balloonConfig = balloonConfig.cloneIt();
        var sb = new StringBuilder();
        List<TS_VisualOrgChart_ConfigBalloon> ballons = new ArrayList();
        if (placementConfig != null && placementConfig.currentBalloonVerticalDown > 0) {
            var hidemeParentIdPrevious = balloonConfig.balloonParentId;
            var hidemeParentIdCurrent = "";
            for (var i = 0; i < placementConfig.currentBalloonVerticalDown; i++) {
                hidemeParentIdCurrent = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
                ballons.add(TS_VisualOrgChart_ConfigBalloon.of(hidemeParentIdCurrent, hidemeParentIdPrevious, "left_" + placementConfig.leftPx + hidemeParentIdCurrent, "", ""));
                hidemeParentIdPrevious = hidemeParentIdCurrent;
            }
            balloonConfig.balloonParentId = hidemeParentIdCurrent;
            ballons.add(balloonConfig);
        } else {
            ballons.add(balloonConfig);
        }
        if (placementConfig != null && placementConfig.childerenTreeVerticalDown > 0) {

        }
        ballons.forEach(balloon -> sb.append(balloonScript(balloon)));
        return sb;
    }

    public static StringBuilder balloonScript(TS_VisualOrgChart_ConfigBalloon balloonConfig) {
        balloonConfig = balloonConfig.cloneIt();
        var sb = new StringBuilder();
        if (TGS_StringUtils.isNullOrEmpty(balloonConfig.balloonParentId) || Objects.equals(balloonConfig.balloonParentId.trim(), "0")) {
            balloonConfig.balloonParentId = "";
        }
        sb.append("        [");
        {
            sb.append("{");
            {
                {
                    sb.append("v:'");
                    sb.append(balloonConfig.balloonId);
                    sb.append("'");
                }
                sb.append(", ");
                {
                    sb.append("f:'");
                    {
                        {
                            sb.append("<div style=\"color: var(--colorTextPrimary);\"><B>");
                            sb.append(balloonConfig.balloonHtmlHeader);
                            sb.append("</B>");
                        }
                        sb.append("<br/>");
                        {
                            sb.append("<div style=\"color: var(--colorTextSecondary);\">");
                            sb.append(balloonConfig.balloonHtmlText);
                            sb.append("</div></div>");
                        }
                    }
                    sb.append("'");
                }
            }
            sb.append("}");
            sb.append(", '").append(balloonConfig.balloonParentId).append("'");
            sb.append(", '").append(balloonConfig.balloonTooltip).append("'");
        }
        sb.append("],");
        sb.append("\n");
        return sb;
    }

    public static String pstScript() {
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
