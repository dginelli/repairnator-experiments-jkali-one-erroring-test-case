/*
 * Copyright (c) 2017 Helmut Neemann
 * Use of this source code is governed by the GPL v3 license
 * that can be found in the LICENSE file.
 */

/**
 * Model to create a hierarchical hdl file.
 * In difference to the model used by the simulator ({@link de.neemann.digital.core.Model})
 * this model is not "flat" All the sub circuits are still existing. So the can be represented
 * as entities in the HDL file. If you use the simulation model to create a hdl file, the hdl model
 * will become also flat.
 */
package de.neemann.digital.hdl.model;
