package com.tugalsan.api.gui.visualization.server;

public class TS_VisualOrgChart_ConfigBalloon {

    private TS_VisualOrgChart_ConfigBalloon(String id, String parentId, String tooltip, String htmlHeader, String htmlText) {
        this.id = id;
        this.parentId = parentId;
        this.tooltip = tooltip;
        this.htmlHeader = htmlHeader;
        this.htmlText = htmlText;
    }
    public String id, parentId, tooltip, htmlHeader, htmlText;

    public TS_VisualOrgChart_ConfigBalloon cloneIt() {
        return of(id, parentId, tooltip, htmlHeader, htmlText);
    }

    public static TS_VisualOrgChart_ConfigBalloon of(String id, String parentId, String tooltip, String htmlHeader, String htmlText) {
        return new TS_VisualOrgChart_ConfigBalloon(id, parentId, tooltip, htmlHeader, htmlText);
    }

    @Override
    public String toString() {
        return TS_VisualOrgChart_ConfigBalloon.class.getSimpleName() + "{" + "id=" + id + ", parentId=" + parentId + ", tooltip=" + tooltip + ", htmlHeader=" + htmlHeader + ", htmlText=" + htmlText + '}';
    }
}
