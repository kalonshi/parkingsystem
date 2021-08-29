package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		int inDay = ticket.getInTime().getDate();
		int outDay = ticket.getOutTime().getDate();
		int inHour = ticket.getInTime().getHours();
		int outHour = ticket.getOutTime().getHours();
		int inMinutes = ticket.getInTime().getMinutes();
		int outMinutes = ticket.getOutTime().getMinutes();

		// TODO: Some tests are failing here. Need to check if this logic is correct
		int duration = ((outDay * 24 * 60) + (outHour * 60) + outMinutes)
				- ((inDay * 24 * 60) + (inHour * 60) + inMinutes);

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration < 60) {
				ticket.setPrice(0.75 * Fare.CAR_RATE_PER_HOUR);

				break;
			} else if ((duration < 24 * 60) && (duration >= 60)) {
				ticket.setPrice((duration / 60) * Fare.CAR_RATE_PER_HOUR);

				break;
			} else if ((duration >= 24 * 60)) {
				System.out.println("duree>=24");
				System.out.println(duration);
				ticket.setPrice((duration / (24 * 60)) * 24 * Fare.CAR_RATE_PER_HOUR);

				break;
			}
		}
		case BIKE: {
			if (duration < 60) {
				ticket.setPrice(0.75 * Fare.BIKE_RATE_PER_HOUR);

				break;
			} else if ((duration < 24 * 60)) {
				ticket.setPrice((duration / 60) * Fare.BIKE_RATE_PER_HOUR);

				break;
			} else if ((duration >= 24 * 60)) {
				System.out.println("duree>=24");
				System.out.println(duration);
				ticket.setPrice((duration / 24) * 24 * Fare.BIKE_RATE_PER_HOUR);
				break;
			}
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}