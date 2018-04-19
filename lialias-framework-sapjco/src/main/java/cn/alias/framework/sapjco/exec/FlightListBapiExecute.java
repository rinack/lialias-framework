package cn.alias.framework.sapjco.exec;

import java.util.List;

import org.hibersap.bapi.BapiRet2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cn.alias.framework.sapjco.bapi.FlightListBapi;
import cn.alias.framework.sapjco.service.FlightListBapiService;
import cn.alias.framework.sapjco.struc.Flight;

/**
 * SAP
 * 
 * @author Geely
 *
 */
@Component
public class FlightListBapiExecute {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightListBapiService flightListBapiService;

	/**
	 * 执行任务
	 */
	public List<Flight> execute() {

		try {

			FlightListBapi flightList = new FlightListBapi("DE", "Frankfurt", "DE", "Berlin", null, false, 10);

			flightList = flightListBapiService.execute(flightList);
			if (flightList == null || flightList.getFlightList() == null) {
				return Lists.newArrayList();
			}

			showResult(flightList);
			
			return flightList.getFlightList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return Lists.newArrayList();

	}

	private void showResult(FlightListBapi flightList) {

		System.out.println("AirlineId: " + flightList.getFromCountryKey());
		System.out.println("FromCity: " + flightList.getFromCity());
		System.out.println("ToCountryKey: " + flightList.getToCountryKey());
		System.out.println("ToCity: " + flightList.getToCity());
		System.out.println("AirlineCarrier: " + flightList.getAirlineCarrier());
		System.out.println("MaxRead: " + flightList.getMaxRead());

		System.out.println("\nFlightData");
		List<Flight> flights = flightList.getFlightList();
		for (Flight flight : flights) {
			System.out.print("\t" + flight.getAirportFrom());
			System.out.print("\t" + flight.getAirportTo());
			System.out.print("\t" + flight.getCarrierId());
			System.out.print("\t" + flight.getConnectionId());
			System.out.print("\t" + flight.getSeatsMax());
			System.out.print("\t" + flight.getSeatsOccupied());
			System.out.println("\t" + flight.getDepartureTime());
		}

		System.out.println("\nReturn");
		BapiRet2 returnStruct = flightList.getReturnData();
		System.out.println("\tMessage: " + returnStruct.getMessage());
		System.out.println("\tNumber: " + returnStruct.getNumber());
		System.out.println("\tType: " + returnStruct.getType());
		System.out.println("\tId: " + returnStruct.getId());
	}

}
