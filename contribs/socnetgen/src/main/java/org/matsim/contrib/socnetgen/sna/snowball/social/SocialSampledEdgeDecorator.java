/* *********************************************************************** *
 * project: org.matsim.*
 * SocialSampledEdgeDecorator.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2010 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package org.matsim.contrib.socnetgen.sna.snowball.social;

import org.matsim.contrib.socnetgen.sna.graph.Vertex;
import org.matsim.contrib.socnetgen.sna.graph.social.SocialEdge;
import org.matsim.contrib.socnetgen.sna.snowball.SampledEdgeDecorator;
import org.matsim.core.utils.collections.Tuple;

/**
 * @author illenberger
 *
 */
public class SocialSampledEdgeDecorator<E extends SocialEdge> extends SampledEdgeDecorator<E> implements SocialEdge {

	protected SocialSampledEdgeDecorator(E delegate) {
		super(delegate);
		}

	@Override
	public SocialSampledVertexDecorator<?> getOpposite(Vertex vertex) {
		return (SocialSampledVertexDecorator<?>) super.getOpposite(vertex);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Tuple<? extends SocialSampledVertexDecorator<?>, ? extends SocialSampledVertexDecorator<?>> getVertices() {
		return (Tuple<? extends SocialSampledVertexDecorator<?>, ? extends SocialSampledVertexDecorator<?>>) super.getVertices();
	}

	@Override
	public double length() {
		return getDelegate().length();
	}

	@Override
	public double getFrequency() {
		return getDelegate().getFrequency();
	}

	@Override
	public String getType() {
		return getDelegate().getType();
	}

}
