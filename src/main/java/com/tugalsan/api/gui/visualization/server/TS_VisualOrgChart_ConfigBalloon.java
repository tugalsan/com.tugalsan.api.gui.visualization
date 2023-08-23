package com.tugalsan.api.gui.visualization.server;

public class TS_VisualOrgChart_ConfigBalloon {

    private TS_VisualOrgChart_ConfigBalloon(String id, String parentId, String tooltip, String htmlHeader, String htmlText) {
        this.id = id;
        this.parentId = parentId;
        this.balloonTooltip = tooltip;
        this.htmlHeader = htmlHeader;
        this.htmlText = htmlText;
    }
    public String id, parentId, balloonTooltip, htmlHeader, htmlText;

    public TS_VisualOrgChart_ConfigBalloon cloneIt() {
        return of(id, parentId, balloonTooltip, htmlHeader, htmlText);
    }

    public static TS_VisualOrgChart_ConfigBalloon of(String id, String parentId, String balloonTooltip, String htmlHeader, String htmlText) {
        return new TS_VisualOrgChart_ConfigBalloon(id, parentId, balloonTooltip, htmlHeader, htmlText);
    }

    @Override
    public String toString() {
        return TS_VisualOrgChart_ConfigBalloon.class.getSimpleName() + "{" + "id=" + id + ", parentId=" + parentId + ", balloonTooltip=" + balloonTooltip + ", htmlHeader=" + htmlHeader + ", htmlText=" + htmlText + '}';
    }
}
