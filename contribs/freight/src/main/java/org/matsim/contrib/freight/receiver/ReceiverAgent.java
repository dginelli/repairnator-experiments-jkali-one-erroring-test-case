/**
 * 
 */
package org.matsim.contrib.freight.receiver;

import org.matsim.api.core.v01.Id;
import org.matsim.core.scoring.ScoringFunction;

/**
 * This keeps track of a single receiver during simulation.
 * 
 * @author wlbean
 *
 */

public class ReceiverAgent {

	private final Receiver receiver;
	private final ScoringFunction scorFunc;
	private Id<Receiver> id;
	

	ReceiverAgent(Receiver receiver, ScoringFunction receiverScorFunc) {
		this.receiver = receiver;
		this.scorFunc = receiverScorFunc;
		this.id = receiver.getId();		
	}


	/**
	 * Score the receiver agent's selected order. This score reflects the receiver cost and is currently determined as the carrier's delivery cost to that receiver (based on the proportion of this receiver's orders in all the orders delivered by the carrier). This is not really realistic, and will be changed in the future.
	 *
	 * @author wlbean
	 * 
	 */
	
	public void scoreSelectedOrder() {
		double cost = 0;
			
		if (receiver.getSelectedPlan() == null){
		return;
		}
		
		if(receiver.getSelectedPlan().getOrderCarrier().getSelectedPlan() == null){
			return;
		}
		
		
		cost = receiver.getSelectedPlan().getOrderCarrier().getSelectedPlan().getScore();
		scorFunc.addMoney(cost);
		scorFunc.finish();
		receiver.getSelectedPlan().setScore(scorFunc.getScore());
		return;
		
	}
	
	
	/**
	 * Returns the receiver agent's unique receiver id.
	 * @return
	 */
	
	public Id<Receiver> getId() {
		return id;
	}
	
	


}
