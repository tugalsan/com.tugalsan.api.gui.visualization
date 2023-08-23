package com.tugalsan.api.gui.visualization.server;

public class TS_VisualOrgChart_ConfigPlacement {

    public TS_VisualOrgChart_ConfigPlacement(int leftPx, int currentBalloonVerticalDown, int childerenTreeVerticalDown) {
        this.leftPx = leftPx;
        this.currentBalloonVerticalDown = currentBalloonVerticalDown;
        this.childerenTreeVerticalDown = childerenTreeVerticalDown;
    }

    public int leftPx, currentBalloonVerticalDown, childerenTreeVerticalDown;

    public TS_VisualOrgChart_ConfigPlacement cloneIt() {
        return of(leftPx, currentBalloonVerticalDown, childerenTreeVerticalDown);
    }

    public static TS_VisualOrgChart_ConfigPlacement of(int leftPx, int currentBalloonVerticalDown, int childerenTreeVerticalDown) {
        return new TS_VisualOrgChart_ConfigPlacement(leftPx, currentBalloonVerticalDown, childerenTreeVerticalDown);
    }

    @Override
    public String toString() {
        return TS_VisualOrgChart_ConfigPlacement.class.getSimpleName() + "{" + "leftPx=" + leftPx + ", currentBalloonVerticalDown=" + currentBalloonVerticalDown + ", childerenTreeVerticalDown=" + childerenTreeVerticalDown + '}';
    }
}
