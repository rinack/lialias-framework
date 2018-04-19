package cn.alias.framework.sapjco.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.alias.framework.sapjco.bapi.FlightListBapi;
import cn.alias.framework.sapjco.dao.FlightListBapiDao;
import cn.alias.framework.sapjco.service.FlightListBapiService;

@Service("materialStockDataQueryBapiService")
public class FlightListBapiImpl implements FlightListBapiService {

	@Autowired
	private FlightListBapiDao flightListBapiDao;

	@Override
	public FlightListBapi execute(FlightListBapi flightListBapi) {
		if (flightListBapi != null) {
			return flightListBapiDao.search(flightListBapi);
		}
		return null;
	}

}
