/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 */
package org.wings.plaf;

import org.wings.SComponent;

import java.util.*;
import java.io.Serializable;

/**
 * Keeps track of element ids written during code generation.
 * Not thread safe.
 *
 * @author <a href="mailto:hengels@mercatis.de">Holger Engels</a>
 * @version $Revision$
 */
final public class SuffixManager
    implements Serializable
{
    private Map journal = new HashMap();

    public String currentSuffix(String id, Object epoch) {
        return currentSuffix(id, "_", epoch);
    }

    public String currentSuffix(String id, String delimiter, Object epoch) {
        Counter counter = (Counter)journal.get(id);
        if (counter == null)
            throw new IllegalStateException("net yet defined in this epoch");

        int value = counter.current(epoch);
        if (value > 0)
            return delimiter + value;
        return "";
    }

    public String nextSuffix(String id, Object epoch) {
        return nextSuffix(id, "_", epoch);
    }

    public String nextSuffix(String id, String delimiter, Object epoch) {
        Counter counter = (Counter)journal.get(id);
        if (counter == null) {
            counter = new Counter(epoch);
            journal.put(id, counter);
        }
        int value = counter.next(epoch);
        if (value > 0)
            return delimiter + value;
        return "";
    }

    final class Counter {
        private Object epoch;
        private int value = 0;

        public Counter(Object epoch) {
            this.epoch = epoch;
        }
        public int current(Object epoch) {
            if (epoch != this.epoch)
                throw new IllegalStateException("net yet defined in this epoch");
            return value;
        }

        public int next(Object epoch) {
            if (epoch != this.epoch) {
                this.epoch = epoch;
                value = 0;
            }
            return value++;
        }
    }
}
