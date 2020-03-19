/*
 * Copyright (c) 2017 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */
package de.neemann.digital.testing.parser;

/**
 * A context with a variable
 */
public class ContextWithVar extends Context {

    private final Context parent;
    private final String var;
    private long value;

    /**
     * Creates a new instance
     *
     * @param parent the parent
     * @param var    the variable
     */
    public ContextWithVar(Context parent, String var) {
        this.parent = parent;
        this.var = var;
    }

    /**
     * Creates a new instance
     *
     * @param var the variable
     */
    public ContextWithVar(String var) {
        this.parent = new Context();
        this.var = var;
    }

    /**
     * Sets a value to this variable
     *
     * @param value the value
     * @return this for chained calls
     */
    public ContextWithVar setValue(long value) {
        this.value = value;
        return this;
    }

    @Override
    public long getVar(String name) throws ParserException {
        if (name.equals(var))
            return value;
        else
            return parent.getVar(name);
    }

}
