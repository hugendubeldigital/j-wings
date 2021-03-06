/*
 * $Id$
 * Copyright 2000,2005 wingS development team.
 *
 * This file is part of wingS (http://www.j-wings.org).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package wingset;

import org.wings.*;
import org.wings.event.SRenderEvent;
import org.wings.event.SRenderListener;
import org.wings.session.WingsStatistics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class MemUsageExample extends WingSetPane {
    private static final DecimalFormat megaByteFormatter = new DecimalFormat("#.###");

    public SComponent createExample() {
        final SButton gc = new SButton("gc"); // label overwritten via attribute in template
        final SButton refresh = new SButton("Refresh");
        final SProgressBar progressBar = new SProgressBar();
        final SLabel totalMemory = new SLabel();
        final SLabel freeMemory = new SLabel();
        final SLabel usedMemory = new SLabel();
        final SLabel overallSessions = new SLabel();
        final SLabel activeSessions = new SLabel();
        final SLabel uptime = new SLabel();
        final SLabel requestCount = new SLabel();
        final SForm panel = new SForm();

        // Action listener for GC button trigger
        gc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.gc();
            }
        });

        // This render listenre updates all lables when the page starts to render.
        SRenderListener renderListener = new SRenderListener() {
            public void startRendering(SRenderEvent e) {
                long free = Runtime.getRuntime().freeMemory();
                long total = Runtime.getRuntime().totalMemory();

                progressBar.setMaximum((int) total);
                progressBar.setValue((int) (total - free));
                progressBar.setString(getByteString(total - free));

                totalMemory.setText(getByteString(total));
                freeMemory.setText(getByteString(free));
                usedMemory.setText(getByteString(total - free));
                overallSessions.setText(Integer.toString(WingsStatistics.getStatistics().getOverallSessionCount()));
                activeSessions.setText(Integer.toString(WingsStatistics.getStatistics().getActiveSessionCount()));
                requestCount.setText(Integer.toString(WingsStatistics.getStatistics().getRequestCount()));
                uptime.setText(getUptimeString(WingsStatistics.getStatistics().getUptime()));
            }

            public void doneRendering(SRenderEvent e) {}
        };
        panel.addRenderListener(renderListener);

        // style progress bar
        progressBar.setUnfilledColor(java.awt.Color.gray);
        progressBar.setFilledColor(java.awt.Color.red);
        progressBar.setForeground(java.awt.Color.red);
        progressBar.setBorderColor(java.awt.Color.black);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new SDimension("200", null));

        try {
            java.net.URL templateURL = getSession().getServletContext().getResource("/templates/MemUsageExample.thtml");
            STemplateLayout layout = new STemplateLayout(templateURL);
            panel.setLayout(layout);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        panel.add(gc, "GC");
        panel.add(refresh, "refresh");
        panel.add(progressBar, "InfoBar");
        panel.add(totalMemory, "TotalMemory");
        panel.add(freeMemory, "FreeMemory");
        panel.add(usedMemory, "UsedMemory");
        panel.add(activeSessions, "ActiveSessions");
        panel.add(overallSessions, "OverallSessions");
        panel.add(requestCount, "RequestCount");
        panel.add(uptime, "Uptime");

        return panel;
    }

    private static final String getByteString(long bytes) {
        if (bytes < 1024) {
            return Long.toString(bytes);
        }
        if (bytes < 1024 * 1024) {
            return Long.toString(bytes / (1024)) + "kB";
        }
        return megaByteFormatter.format(new Double(((double) bytes) / (1024 * 1024))) + "MB";

    }

    private static final String getUptimeString(long uptime) {
        final long SECOND = 1000;
        final long MINUTE = 60 * SECOND;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;
        final StringBuffer result = new StringBuffer();
        boolean doAppend = false;

        if (uptime / DAY > 0) {
            result.append(uptime / DAY).append("d ");
            uptime %= DAY;
            doAppend = true;
        }
        if (uptime / HOUR > 0 || doAppend) {
            result.append(uptime / HOUR).append("h ");
            uptime %= HOUR;
            doAppend = true;
        }
        if (uptime / MINUTE > 0 || doAppend) {
            result.append(uptime / MINUTE).append("m ");
            uptime %= MINUTE;
            doAppend = true;
        }
        if (uptime / SECOND > 0 || doAppend) {
            result.append(uptime / SECOND).append("s ");
            uptime %= SECOND;
        }
        result.append(uptime).append("ms");

        return result.toString();
    }
}


