/*
 * Copyright (c) 2018 Helmut Neemann.
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.core.extern;

import de.neemann.digital.core.Bits;

/**
 * A port for external access
 */
public class Port {
    private final int bits;
    private final String name;

    /**
     * Creates a new port
     *
     * @param port the port
     */
    public Port(String port) {
        int p = port.indexOf(':');
        if (p < 0) {
            name = port;
            bits = 1;
        } else {
            name = port.substring(0, p);
            int b = 1;
            try {
                b = (int) Bits.decode(port.substring(p + 1));
            } catch (Bits.NumberFormatException e) {
                b = 1;
            }
            bits = b;
        }
    }

    /**
     * @return the number of bits
     */
    public int getBits() {
        return bits;
    }

    /**
     * @return the name of the port
     */
    public String getName() {
        return name;
    }
}
