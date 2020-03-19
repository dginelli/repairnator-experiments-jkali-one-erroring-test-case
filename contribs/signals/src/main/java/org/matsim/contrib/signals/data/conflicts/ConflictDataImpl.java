/*
 *  *********************************************************************** *
 *  * project: org.matsim.*
 *  *                                                                         *
 *  * *********************************************************************** *
 *  *                                                                         *
 *  * copyright       : (C) 2014 by the members listed in the COPYING, *
 *  *                   LICENSE and WARRANTY file.                            *
 *  * email           : info at matsim dot org                                *
 *  *                                                                         *
 *  * *********************************************************************** *
 *  *                                                                         *
 *  *   This program is free software; you can redistribute it and/or modify  *
 *  *   it under the terms of the GNU General Public License as published by  *
 *  *   the Free Software Foundation; either version 2 of the License, or     *
 *  *   (at your option) any later version.                                   *
 *  *   See also COPYING, LICENSE and WARRANTY file                           *
 *  *                                                                         *
 *  * ***********************************************************************
 */
package org.matsim.contrib.signals.data.conflicts;

import java.util.HashMap;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Node;
import org.matsim.contrib.signals.model.SignalSystem;

/**
 * @author tthunig
 */
public class ConflictDataImpl implements ConflictData {

	private ConflictDataFactory factory = new ConflictDataFactoryImpl();
	
	private Map<Id<SignalSystem>, ConflictingDirections> conflictsPerSignalSystem = new HashMap<>();
	private Map<Id<Node>, ConflictingDirections> conflictsPerNode = new HashMap<>();
	
	@Override
	public void addConflictingDirectionsForIntersection(Id<SignalSystem> signalSystemId, Id<Node> nodeId,
			ConflictingDirections conflictingDirections) {
		conflictsPerSignalSystem.put(signalSystemId, conflictingDirections);
		conflictsPerNode.put(nodeId, conflictingDirections);
	}

	@Override
	public Map<Id<SignalSystem>, ConflictingDirections> getConflictsPerSignalSystem() {
		return conflictsPerSignalSystem;
	}

	@Override
	public Map<Id<Node>, ConflictingDirections> getConflictsPerNode() {
		return conflictsPerNode;
	}

	@Override
	public ConflictDataFactory getFactory() {
		return factory;
	}

	@Override
	public void setFactory(ConflictDataFactory factory) {
		this.factory = factory;
	}

}
