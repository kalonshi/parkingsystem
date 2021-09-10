package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import junit.framework.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

@MockitoSettings(strictness = Strictness.LENIENT)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		try {
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			/* when(ticketDAO.getNbTicketsByVehicleRegNumber("ABCDEF")).thenReturn(5); */
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
			parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicleTest() {
		parkingService.processExitingVehicle();
		verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
	}

	// TEST Check if discount required

	@Test
	public void getDiscountWhenReccurentCustomer() throws Exception {

		when(ticketDAO.getNbTicketsByVehicleRegNumber("ABCDEF")).thenReturn(5);
		boolean hasDiscount = parkingService.checkVehichleRegNumber("ABCDEF");
		Assert.assertTrue(hasDiscount);

	}
	// TEST calcul discount

	@Test
	public void getFareDiscountWhenReccurentCustomer() throws Exception {

		when(ticketDAO.getNbTicketsByVehicleRegNumber("ABCDEF")).thenReturn(5);
		parkingService.processExitingVehicle();
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		Assert.assertEquals(1.5 * 0.95, ticket.getPrice());

	}

	/*
	 * @Test public void getNextParkingNumberIfAvailableExecptionsTest() throws
	 * Exception {
	 * 
	 * when(inputReaderUtil.readSelection()).thenReturn(1);
	 * 
	 * when(parkingSpotDAO.getNextAvailableSlot(any())).thenReturn(-1);
	 * 
	 * ParkingSpot result = parkingService.getNextParkingNumberIfAvailable();
	 * Assert.assertNull(result);
	 * 
	 * 
	 * assertThrows(Exception.class, () ->
	 * parkingService.getNextParkingNumberIfAvailable());
	 * 
	 * }
	 */

}
