package com.tugalsan.api.gui.visualization.server;

public class TS_VisualOrgChart_ConfigBalloon {

    private TS_VisualOrgChart_ConfigBalloon(String balloonId, String balloonParentId, String balloonTooltip, String balloonHtmlHeader, String balloonHtmlText) {
        this.balloonId = balloonId;
        this.balloonParentId = balloonParentId;
        this.balloonTooltip = balloonTooltip;
        this.balloonHtmlHeader = balloonHtmlHeader;
        this.balloonHtmlText = balloonHtmlText;
    }
    public String balloonId, balloonParentId, balloonTooltip, balloonHtmlHeader, balloonHtmlText;

    public TS_VisualOrgChart_ConfigBalloon cloneIt() {
        return of(balloonId, balloonParentId, balloonTooltip, balloonHtmlHeader, balloonHtmlText);
    }

    public static TS_VisualOrgChart_ConfigBalloon of(String balloonId, String balloonParentId, String balloonTooltip, String balloonHtmlHeader, String balloonHtmlText) {
        return new TS_VisualOrgChart_ConfigBalloon(balloonId, balloonParentId, balloonTooltip, balloonHtmlHeader, balloonHtmlText);
    }

    @Override
    public String toString() {
        return TS_VisualOrgChart_ConfigBalloon.class.getSimpleName() + "{" + "balloonId=" + balloonId + ", balloonParentId=" + balloonParentId + ", balloonTooltip=" + balloonTooltip + ", balloonHtmlHeader=" + balloonHtmlHeader + ", balloonHtmlText=" + balloonHtmlText + '}';
    }
}
