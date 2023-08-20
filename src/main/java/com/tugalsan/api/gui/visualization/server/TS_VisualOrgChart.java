package com.tugalsan.api.gui.visualization.server;

import com.tugalsan.api.random.client.TGS_RandomUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import java.util.Objects;

public class TS_VisualOrgChart {

    public static StringBuilder preScript(StringBuilder sb) {
        return sb.append("""
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
                    """);
    }

    public static StringBuilder balloonScript(StringBuilder sb, String balloonId, String balloonParentId, String balloonTooltip, String balloonHtmlHeader, String balloonHtmlText, int skipVerticalCount) {
        if (skipVerticalCount == 0) {
            return TS_VisualOrgChart.balloonScript(sb, balloonId, balloonParentId, balloonTooltip, balloonHtmlHeader, balloonHtmlText);
        }
        String hidemePerv = balloonParentId;
        String hidemeCurrent = null;
        for (int i = 0; i < skipVerticalCount; i++) {
            hidemeCurrent = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
            TS_VisualOrgChart.balloonScript(sb, hidemeCurrent, hidemePerv, hidemeCurrent, "", "");
            hidemePerv = hidemeCurrent;
        }
        return TS_VisualOrgChart.balloonScript(sb, balloonId, hidemeCurrent, balloonTooltip, balloonHtmlHeader, balloonHtmlText);
    }

    private static StringBuilder balloonScript(StringBuilder sb, String balloonId, String balloonParentId, String balloonTooltip, String balloonHtmlHeader, String balloonHtmlText) {
        if (TGS_StringUtils.isNullOrEmpty(balloonParentId) || Objects.equals(balloonParentId.trim(), "0")) {
            balloonParentId = "";
        }
        sb.append("        [");
        {
            sb.append("{");
            {
                {
                    sb.append("v:'");
                    sb.append(balloonId);
                    sb.append("'");
                }
                sb.append(", ");
                {
                    sb.append("f:'");
                    {
                        {
                            sb.append("<div style=\"color: var(--colorTextPrimary);\"><B>");
                            sb.append(balloonHtmlHeader);
                            sb.append("</B>");
                        }
                        sb.append("<br/>");
                        {
                            sb.append("<div style=\"color: var(--colorTextSecondary);\">");
                            sb.append(balloonHtmlText);
                            sb.append("</div></div>");
                        }
                    }
                    sb.append("'");
                }
            }
            sb.append("}");
            sb.append(", '").append(balloonParentId).append("'");
            sb.append(", '").append(balloonTooltip).append("'");
        }
        sb.append("],");
        sb.append("\n");
        return sb;
    }

    public static StringBuilder pstScript(StringBuilder sb) {
        return sb.append("""
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
                            data.setRowProperty(index, 'style', 'color: transparent; background: transparent; border: 0; border-left: 0.8px solid #3388dd; border-radius: 0; box-shadow: 0 0; position:relative; left: 41px; ');
                        });
                        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
                        chart.draw(data,  {allowHtml:true, allowCollapse:true, size:'large'});
                    }
                    </script>
                    <div id='chart_div'></div>
                    """);
    }

}
