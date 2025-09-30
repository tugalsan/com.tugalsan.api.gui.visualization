package com.tugalsan.api.gui.visualization.server;

import module com.tugalsan.api.log;
import module com.tugalsan.api.random;
import module com.tugalsan.api.stream;
import module com.tugalsan.api.string;
import module com.tugalsan.api.tuple;
import java.util.*;
import java.util.stream.*;

public class TS_VisualOrgChart {

    final private static TS_Log d = TS_Log.of(TS_VisualOrgChart.class);

    private TS_VisualOrgChart() {
    }

    public static TS_VisualOrgChart of() {
        return new TS_VisualOrgChart();
    }
    final private List<TGS_Tuple2<String, String>> kickParentId_from_to = new ArrayList();
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
        sortItemsByHierarchy();
        var sb = new StringBuilder();
        sb.append(preScript());
        items.forEach(item -> sb.append(balloonScript_should_sorted_first_to_kickChilderen(item.value0, item.value1)));
        sb.append(pstScript());
        return sb.toString();
    }

    private void sortItemsByHierarchy() {
        List<TGS_Tuple2<TS_VisualOrgChart_ConfigBalloon, TS_VisualOrgChart_ConfigPlacement>> itemsMoved = new ArrayList();
        itemsMoved.addAll(items);
        items.clear();
        var loopRunLimit = 1000;
        while (itemsMoved.size() > 1) {
            //DETECT CIRCULER ID RELATION
            loopRunLimit--;
            if (loopRunLimit == 0) {
                d.ce("sortItemsByHierarchy", "ERROR: loopRunLimit == 0 reached", "circuler related id'ed items will be skipped");
                itemsMoved.forEach(si -> d.ce("sortItemsByHierarchy", "(id, parentId)", si.value0.id, si.value0.parentId));
                break;
            }
            var item = itemsMoved.get(0);
            //IF ITEM ID AND PARENT_ID EQUALS, OMIT ITEM
            if (Objects.equals(item.value0.id, item.value0.parentId)) {
                itemsMoved.remove(item);
                d.ce("sortItemsByHierarchy", "ERROR: Objects.equals(item.value0.id, item.value0.parentId)", "item will be skipped", "(id, parentId)", item.value0.id, item.value0.parentId);
                continue;
            }
            {// IF PARENT_ID IS NOT FOUND ON NEXT ITEMS AS ID, ACCEPT THE ITEM AS NEXT HIERARCY ELEMENT AS MOVING
                var idx_superIdFound = IntStream.range(1, itemsMoved.size())
                        .filter(i -> Objects.equals(itemsMoved.get(i).value0.id, item.value0.parentId))
                        .findAny().orElse(-1);
                if (idx_superIdFound == -1) {
                    d.ci("sortItemsByHierarchy", "idx_superIdFound == -1", "(id, parentId)", item.value0.id, item.value0.parentId);
                    itemsMoved.remove(item);
                    items.add(item);
                    continue;
                }
            }
            {//PUT THE USELESS ITEM TO LAST TO DEAL LATER
                d.ci("sortItemsByHierarchy", "uselessItem", "(id, parentId)", item.value0.id, item.value0.parentId);
                itemsMoved.remove(item);
                itemsMoved.add(item);
            }
        }
    }

    private String preScript() {
        return """
                    <style>
                        table {
                            border-collapse: separate;
                        }
                    </style>
                    <script type='text/javascript' src='https://www.gstatic.com/charts/loader.js'></script><!-- old https://www.google.com/jsapi -->
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

    public String balloonScript_should_sorted_first_to_kickChilderen(TS_VisualOrgChart_ConfigBalloon balloonConfig, TS_VisualOrgChart_ConfigPlacement placementConfig) {
        //MUTABLE BALLON
        var balloonConfigMutable = balloonConfig.cloneIt();
        //KICK PARENT ID TO A HIDDEN ONE IF kickChildrenDownCount proccessed before
        if (placementConfig == null || placementConfig.kickable) {
            var kickParentId = kickParentId_from_to.stream()
                    .filter(ft -> Objects.equals(ft.value0, balloonConfigMutable.parentId))
                    .findAny().orElse(null);
            if (kickParentId != null) {
                balloonConfigMutable.parentId = kickParentId.value1;
            }
        }
        //CONSTRUCT ballonsPre
        List<TS_VisualOrgChart_ConfigBalloon> balloonConfigsPre = new ArrayList();
        if (placementConfig != null && placementConfig.pullSelfDownCount > 0) {
            var hidemeParentId = balloonConfigMutable.parentId;
            for (var i = 0; i < placementConfig.pullSelfDownCount; i++) {
                var hidemeId = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
                balloonConfigsPre.add(TS_VisualOrgChart_ConfigBalloon.of(
                        hidemeId,
                        hidemeParentId,
                        "left_" + placementConfig.leftPx + hidemeId,
                        "",
                        ""
                ));
                hidemeParentId = hidemeId;
            }
            balloonConfigMutable.parentId = hidemeParentId;
        }
        //CONSTRUCT ballonsPst
        List<TS_VisualOrgChart_ConfigBalloon> balloonConfigsPst = new ArrayList();
        if (placementConfig != null && placementConfig.kickChildrenDownCount > 0) {
            var hidemeParentId = balloonConfigMutable.id;
            for (var i = 0; i < placementConfig.kickChildrenDownCount; i++) {
                var hidemeId = "__hideme" + TGS_RandomUtils.nextString(10, true, true, true, false, null);
                balloonConfigsPst.add(TS_VisualOrgChart_ConfigBalloon.of(
                        hidemeId,
                        hidemeParentId,
                        "left_" + placementConfig.leftPx + hidemeId,
                        "",
                        ""
                ));
                hidemeParentId = hidemeId;
            }
            kickParentId_from_to.add(TGS_Tuple2.of(balloonConfigMutable.id, hidemeParentId));
        }
        //ADD STYLE DATA
        if (placementConfig != null && placementConfig.dotted) {
            balloonConfigMutable.tooltip += "_style_dotted";
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
        return TGS_StringUtils.cmn().concat(TGS_StreamUtils.toLst(
                ballonsAll.stream().map(b -> balloonScript(b).toString())
        ));
    }

    private StringBuilder balloonScript(TS_VisualOrgChart_ConfigBalloon balloonConfig) {
        balloonConfig = balloonConfig.cloneIt();
        var sb = new StringBuilder();
        if (TGS_StringUtils.cmn().isNullOrEmpty(balloonConfig.parentId) || Objects.equals(balloonConfig.parentId.trim(), "0")) {
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
//                        for (var i = 0; i< data.getNumberOfRows(); i++){
//                            data.setRowProperty(i, 'style', 'background-color:var(--widgetBackground);background-image:none');
//                            data.setRowProperty(i, 'selectedStyle', 'background-color:var(--widgetSelected);background-image:none');	
//                        }
                        rows.forEach((value, index) => {
                            data.setRowProperty(index, 'style', 'background-color:var(--widgetBackground);background-image:none');
                            data.setRowProperty(index, 'selectedStyle', 'background-color:var(--widgetSelected);background-image:none');	
                            let tooltip = value[2];
                            let hidden = tooltip.includes("__hideme");
                            if (hidden) {
                                let left = tooltip.split("_")[1];
                                data.setRowProperty(index, 'style', 'color: transparent; background: transparent; border: 0; border-left: 0.8px solid #3388dd; border-radius: 0; box-shadow: 0 0; position:relative; left: '+left+'px; ');
                                return;
                            }
                            let dotted = tooltip.includes("_style_dotted");
                            if (dotted) {
                                data.setRowProperty(index, 'style', 'background-color:var(--widgetBackground);background-image:none;border:2px dashed');
                                data.setRowProperty(index, 'selectedStyle', 'background-color:var(--widgetSelected);background-image:none;border:2px dashed');	
                            }
                        });
                        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
                        chart.draw(data,  {allowHtml:true, allowCollapse:true, size:'large'});
                    }
                    </script>
                    <div id='chart_div'></div>
                    """;
    }

}
