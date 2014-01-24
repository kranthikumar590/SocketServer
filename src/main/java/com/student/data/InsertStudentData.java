package com.student.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.gps.data.Id;
import com.gps.data.Map;



public class InsertStudentData {

	private static final Logger logger = Logger.getLogger(InsertStudentData.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	
		ApplicationContext context;
		context = new ClassPathXmlApplicationContext(new String[] { "spring-config.xml" });
		MongoTemplate mongoTemplate=context.getBean("mongoTemplate",MongoTemplate.class);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String todayDate = dateFormat.format(date);
		// logger.info("Today's date --> "+todayDate);
		// BUS_101:09-01-2014
		String lat="100",Long="100",time=sdf.format(cal.getTime());
		
		Map map=context.getBean("map",Map.class);
		map.setArrived_time(time);
		map.setLat(lat);
		map.setLong(Long);
		Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
		Query query = new Query(c1);
		Update update=new Update().push("map_locations", map);
		Id _id=context.getBean("id",Id.class);
		_id.set_id("BUS_101:"+todayDate);
		if(mongoTemplate.collectionExists("oneday_bus_information"))
			mongoTemplate.insert(_id,"oneday_bus_information");
		mongoTemplate.updateFirst(query, update, "oneday_bus_information");
	}
	

}
