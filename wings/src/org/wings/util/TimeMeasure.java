/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */

package org.wings.util;

import java.util.*;
import java.text.MessageFormat;

/*
 * Ein Klasse, die Zeitmessungen aufnimmt und diese in Relation zueineander
 * setzt. Zum Start der Zeitmessung Methode start kurzer Beschreibung was
 * gemessen wird als Parameter aufrufen. Das Ende der Zeitmessung wird durch
 * stop angezeigt. Es kann nur eine Zeitmessung gleichzeitig stattfinden. Die
 * Ausgabe ist nicht sortiert, gibt aber die relativen Unterschiede in der
 * Dauer der einzelnen Messungen an. Die Messung mit der lÑngsten Dauer ist
 * der Referenzwert (1.0).
 */
/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class TimeMeasure
{
    /*
     * Hier werden alle Messungen abgelegt.
     */
    protected Vector measures = new Vector();

    /*
     * Die aktuelle Messung. Wird bei jedem Aufruf von start zurÅckgesetzt.
     */
    protected Measure actual;

    /**
     * Message formatter
     */
    protected MessageFormat formatter;

    /*
     * Erzeugt eine neue Messeinrichtung.
     */
    public TimeMeasure() {
        this (new MessageFormat ("{0}\t: {1}\t {2}\n"));
    }

    /**
     * A new TimeMeasure which reports in a specific format.
     * <ul>
     * <li><code>{0}</code> the measurement comment</li>
     * <li><code>{1}</code> the time it took</li>
     * <li><code>{2}</code> how many times this is faster than the
     *                      slowest measurement</li>
     * </ul>
     */
    public TimeMeasure (MessageFormat formatter) {
        this.formatter = formatter;
    }

    /*
     * Lˆscht alle vorangegangenen Messungen.
     */
    public void reset() {
        measures.removeAllElements();
    }

    /*
     * Startet eine neue Messsung.
     * @param comment  Die Beschreibung der Messung.
     */
    public void start(String comment) {
        actual = new Measure();
        actual.comment = comment;
        actual.start = System.currentTimeMillis();
    }

    /*
     * Startet eine neue Messsung.
     * @param comment  Die Beschreibung der Messung.
     */
    public Object generate(String comment) {
        actual = new Measure();
        actual.comment = comment;
        measures.addElement(actual);
        return actual;
    }

    /*
     * Addiert eine Messsung zu einer bestehenden.
     * @param comment  Die Beschreibung der Messung.
     */
    public void addToMeasure(Object measure) {
        int index = measures.indexOf(measure);
        if ( index<0 ) {
            System.err.println("Measure does not exists " + measure);
            actual = null;
            return;
        }

        actual = (Measure)measures.elementAt(index);
        measures.removeElementAt(index);

        actual.start = System.currentTimeMillis();
    }

    /*
     * Beendet die aktuelle Messsung und speichert diese.
     */
    public void stop() {
        if ( actual!=null ) {
            actual.stop = System.currentTimeMillis();
            actual.duration += actual.stop-actual.start;
            measures.addElement(actual);
            actual = null;
        }
    }

    /*
     * Errechnet die Zeitdauer (in Millisekunden) der lÑngsten Messung.
     */
    private long slowest() {
        Measure m;
        long slowest = -1;

        for ( int i=0; i<measures.size(); i++ ) {
            m = (Measure)measures.elementAt(i);
            slowest = Math.max(slowest, m.duration);
        }

        return slowest;
    }

    /*
     * Erzeugt eine formatierte Ausgaben alle Messergebnisse. Die
     * Ausgabe ist nicht sortiert, gibt aber die relativen Unterschiede in der
     * Dauer der einzelnen Messungen an. Die Messung mit der lÑngsten Dauer ist
     * der Referenzwert (1.0).
     */
    public String print() {
        StringBuffer erg = new StringBuffer();
        long slowest = slowest();
        for ( int i=0; i<measures.size(); i++ ) {
            Measure m = (Measure)measures.elementAt(i);
            Object multiple = null;
            if (m.duration > 0) {
                long zwerg = (long)(((double)slowest*100)/(m.duration));
                multiple = ((double) zwerg / 100) + " x";
            }
            else
                multiple = "";
            Object[] args = {m.comment, (m.duration + "ms"), multiple};
            erg.append (formatter.format (args));
        }
        return erg.toString();
    }

    public String toString() {
        return print();
    }

    /*
     * In dieser Klasse werden die einzelnen Messwerte einer Messung abgelegt.
     */
    class Measure {
        /*
         * Der Zeitpunkt des Starts der Messung (System.currentTimeMillis())
         */
        long start;

        /*
         * Der Zeitpunkt des Endes der Messung (System.currentTimeMillis())
         */
        long stop;

        /*
         * Die Gesamtdauer der Messung
         */
        long duration;

        /*
         * Die Beschreibung der Messung.
         */
        String comment;

        public String toString() {
            return comment;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * End:
 */
