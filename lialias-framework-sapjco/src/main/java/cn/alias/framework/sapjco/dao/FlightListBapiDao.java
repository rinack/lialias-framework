package cn.alias.framework.sapjco.dao;

import org.hibersap.session.Session;
import org.springframework.stereotype.Component;

import cn.alias.framework.sapjco.bapi.FlightListBapi;
import cn.alias.framework.sapjco.configuration.HibersapConnectorBase;

@Component
public class FlightListBapiDao extends HibersapConnectorBase {

	public FlightListBapi search(FlightListBapi flightListBapi) {
		Session session = dbContextSessionManager("ABAP_AS_WITHOUT_POOL").openSession();
		try {
			session.execute(flightListBapi);
			return flightListBapi;
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
	}
}
