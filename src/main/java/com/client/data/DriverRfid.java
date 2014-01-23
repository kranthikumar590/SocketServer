package com.client.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;

public class DriverRfid {
	private static final Logger logger = Logger.getLogger(DriverRfid.class);
	static MongoTemplate mongoTemplate;
	static ApplicationContext context;

	public OneDayBusInfo setMongoTemplate(MongoTemplate mongoTemplate,
			ApplicationContext context) {
		this.mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
		this.context = context;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String todayDate = dateFormat.format(date);
		// logger.info("Today's date --> "+todayDate);
		// BUS_101:09-01-2014
		Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
		
		BasicQuery query = new BasicQuery(c1.getCriteriaObject());
		logger.info(query);
		// MorningSession
		// morningSession=context.getBean("morningSession",MorningSession.class);
		OneDayBusInfo oneDayBusInfo = mongoTemplate.findOne(query,OneDayBusInfo.class, "oneday_bus_information");
		logger.info("reply ==> "+mongoTemplate.findOne(query,OneDayBusInfo.class, "oneday_bus_information"));
		return oneDayBusInfo;
	}

}
