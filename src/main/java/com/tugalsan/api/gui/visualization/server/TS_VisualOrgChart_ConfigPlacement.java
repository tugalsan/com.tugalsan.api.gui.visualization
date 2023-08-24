package com.tugalsan.api.gui.visualization.server;

public class TS_VisualOrgChart_ConfigPlacement {

    public TS_VisualOrgChart_ConfigPlacement(int leftPx, int pullSelfDownCount, int kickChildrenDownCount, boolean kickable, boolean dotted) {
        this.leftPx = leftPx;
        this.pullSelfDownCount = pullSelfDownCount;
        this.kickChildrenDownCount = kickChildrenDownCount;
        this.kickable = kickable;
        this.dotted = dotted;
    }

    public int leftPx, pullSelfDownCount, kickChildrenDownCount;
    public boolean kickable, dotted;

    public TS_VisualOrgChart_ConfigPlacement cloneIt() {
        return of(leftPx, pullSelfDownCount, kickChildrenDownCount, kickable, dotted);
    }

    public static TS_VisualOrgChart_ConfigPlacement of() {
        return of(0, 0, 0, true, false);
    }

    public static TS_VisualOrgChart_ConfigPlacement of(int leftPx, int pullSelfDownCount, int kickChildrenDownCount, boolean kickable, boolean dotted) {
        return new TS_VisualOrgChart_ConfigPlacement(leftPx, pullSelfDownCount, kickChildrenDownCount, kickable, dotted);
    }

    @Override
    public String toString() {
        return TS_VisualOrgChart_ConfigPlacement.class.getSimpleName() + "{" + "leftPx=" + leftPx + ", pullSelfDownCount=" + pullSelfDownCount + ", kickChildrenDownCount=" + kickChildrenDownCount + ", kickable=" + kickable + ", dotted=" + dotted + '}';
    }
}
