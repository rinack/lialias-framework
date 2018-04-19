package cn.alias.framework.sapjco.bapi;

import java.util.List;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.bapi.BapiRet2;
import org.hibersap.conversion.BooleanConverter;

import cn.alias.framework.sapjco.struc.Flight;

@Bapi("BAPI_SFLIGHT_GETLIST")
public class FlightListBapi {

	@Import
	@Parameter("FROMCOUNTRYKEY")
	private final String fromCountryKey;

	@Import
	@Parameter("FROMCITY")
	private final String fromCity;

	@Import
	@Parameter("TOCOUNTRYKEY")
	private final String toCountryKey;

	@Import
	@Parameter("TOCITY")
	private final String toCity;

	@Import
	@Parameter("AIRLINECARRIER")
	private final String airlineCarrier;

	@Import
	@Parameter("AFTERNOON")
	@Convert(converter = BooleanConverter.class)
	private final boolean afternoon;

	@Import
	@Parameter("MAXREAD")
	private final int maxRead;

	@Export
	@Parameter(value = "RETURN", type = ParameterType.STRUCTURE)
	private BapiRet2 returnData;

	@Table
	@Parameter("FLIGHTLIST")
	private List<Flight> flightList;

	public BapiRet2 getReturnData() {
		return returnData;
	}

	public void setReturnData(BapiRet2 returnData) {
		this.returnData = returnData;
	}

	public List<Flight> getFlightList() {
		return flightList;
	}

	public void setFlightList(List<Flight> flightList) {
		this.flightList = flightList;
	}

	public String getFromCountryKey() {
		return fromCountryKey;
	}

	public String getFromCity() {
		return fromCity;
	}

	public String getToCountryKey() {
		return toCountryKey;
	}

	public String getToCity() {
		return toCity;
	}

	public String getAirlineCarrier() {
		return airlineCarrier;
	}

	public boolean isAfternoon() {
		return afternoon;
	}

	public int getMaxRead() {
		return maxRead;
	}

	public FlightListBapi(String fromCountryKey, String fromCity, String toCountryKey, String toCity,
			String airlineCarrier, boolean afternoon, int maxRead) {

		this.fromCountryKey = fromCountryKey;
		this.fromCity = fromCity;
		this.toCountryKey = toCountryKey;
		this.toCity = toCity;
		this.airlineCarrier = airlineCarrier;
		this.afternoon = afternoon;
		this.maxRead = maxRead;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "FlightListBapi [fromCountryKey=" + fromCountryKey + ", fromCity=" + fromCity + ", toCountryKey="
				+ toCountryKey + ", toCity=" + toCity + ", airlineCarrier=" + airlineCarrier + ", afternoon="
				+ afternoon + ", maxRead=" + maxRead + ", returnData=" + returnData + ", flightList="
				+ (flightList != null ? flightList.subList(0, Math.min(flightList.size(), maxLen)) : null) + "]";
	}

}
