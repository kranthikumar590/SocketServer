package com.client.data;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
public class InsertRfidData {
	
	private static final Logger logger = Logger.getLogger(InsertRfidData.class);
	private String rfid;
	MongoTemplate mongoTemplate;
	public String getRfid() {
		return rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	public Rfid rfidExists(ApplicationContext context) {

		Rfid res=null;
		mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
		
		try{
			Rfid RFID=context.getBean("rfid",Rfid.class);
			RFID.setRfid_number(rfid);
			Query query = new Query(Criteria.where("rfid_number").is(rfid));
			res=mongoTemplate.findOne(query, Rfid.class,"rfid_data");
			//logger.info(" Result --> "+ res);
			
		}
		catch(Exception e){
			logger.info(" Exception while checking RFID exists or not --> "+e.getMessage());
			
		}
		return res;
	}
	public void insertTimeIntoStudent(String rfid_num,ApplicationContext context){
		
		logger.info("RFID [ " +rfid_num+"] exists. with RFID Type [ Student ]" );
		DriverRfid driverRfid=context.getBean("driverRfid",DriverRfid.class);
		OneDayBusInfo oneDayBusInfo=driverRfid.setMongoTemplate(mongoTemplate, context);
		if(oneDayBusInfo!=null){
			
			driver_info driver_infoObject=oneDayBusInfo.getDriver_info();
			morning mor=driver_infoObject.getMorning();
			
			String morning_in_bound=mor.getIn_bound();
			String morning_out_bound=mor.getOut_bound();
			logger.info("Morning out bound "+morning_out_bound);
			if(!morning_in_bound.equals("no")){
				
				if(!morning_out_bound.equals("no")){
					
					logger.info("Morning out bound  exists...");
				}
				else{
					logger.info("Morning out bound doesnot exists...");
				}
			}
			else{
				logger.info("MorningSession is null");
			}
		}
		else{
			logger.info("MorningSession is null");
		}
	}
	public void insertTimeIntoDriver(String rfid_num){
		logger.info("RFID [ " +rfid_num+"] exists. with RFID Type [ Driver ]");
	}
}
