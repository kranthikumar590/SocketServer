package com.client.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.student.data.MainRfidInfo;
import com.student.data.StudentMorningSession;
import com.student.data.rfid_info;

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

		Rfid res = null;
		mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");

		try {
			Rfid RFID = context.getBean("rfid", Rfid.class);
			RFID.setRfid_number(rfid);
			Query query = new Query(Criteria.where("rfid_number").is(rfid));
			res = mongoTemplate.findOne(query, Rfid.class, "rfid_data");
			// logger.info(" Result --> "+ res);

		} catch (Exception e) {
			logger.info(" Exception while checking RFID exists or not --> "
					+ e.getMessage());

		}
		return res;
	}

	public void insertTimeIntoStudent(String rfid_num,
			ApplicationContext context) {

		logger.info("RFID [ " + rfid_num
				+ "] exists. with RFID Type [ Student ]");
		DriverRfid driverRfid = context.getBean("driverRfid", DriverRfid.class);
		OneDayBusInfo oneDayBusInfo = driverRfid.setMongoTemplate(
				mongoTemplate, context);
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String todayDate = dateFormat.format(date);
		if (oneDayBusInfo != null) {

			driver_info driver_infoObject = oneDayBusInfo.getDriver_info();
			try {
				morning mor = driver_infoObject.getMorning();

				String morning_in_bound = mor.getIn_bound();
				String morning_out_bound = mor.getOut_bound();
				// logger.info("Morning out bound "+morning_out_bound);
				// String evening_in_bound=eve.getIn_bound();

				// logger.info("Evening out bound "+evening_out_bound);
				if (!morning_in_bound.equals("no")) {

					if (!morning_out_bound.equals("no")) {

						//logger.info("Morning out bound  exists...");
						// Morning Session closed
						evening eve = driver_infoObject.getEvening();
						String evening_out_bound = eve.getOut_bound();
						if (eve != null) {

							if (!evening_out_bound.equals("no")) {
								logger.info("Today's session is closed...");
							} else {
								// push data into student morning in bound and
								// morning out bound
								StudentMorningSession studentMorningSession = context
										.getBean("studentMorningSession",
												StudentMorningSession.class);
								MainRfidInfo mainRfidInfo=studentMorningSession.set(context, mongoTemplate);
								rfid_info student_rfid_info[]=mainRfidInfo.getRfid_info();
								//logger.info("Student rfid info ==> "+student_rfid_info);
								
								//String student_rfid=student_rfid_info.getRfid();
								if(student_rfid_info!=null){
									
									//Insert into student morning out bound
									//logger.info("RFID_info  exists in collection ....");
									rfid_info current_student_rfid=null;
									for(int i=0;i<student_rfid_info.length;i++){
										
										rfid_info single=student_rfid_info[i];
										if(single.getRfid().equals(rfid_num)){
											//logger.info("Single student info --> "+single);
											current_student_rfid=single;
										}
									}
									if(current_student_rfid!=null){
										
										//logger.info("Student morning in bound exists...");
										if(!current_student_rfid.getEvening_in_bound().equals("no")){
											
											//logger.info("Student session cannot be inserted beacuse [ multiple RFID tapping ]");
											if(!current_student_rfid.getEvening_out_bound().equals("no")){
												
												logger.info("Student Evening session cannot be inserted beacuse [ multiple RFID tapping ]");
												
											}
											else{
												//Insert into student evening out bound
												Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
												
												
												// MorningSession
												// morningSession=context.getBean("morningSession",MorningSession.class);
												rfid_info rf=context.getBean("rfid_info",rfid_info.class);
												rf.setMorning_in_bound(sdf.format(cal.getTime()));
												Query query = new Query(c1.andOperator(
														 
														  Criteria.where("rfid_info.rfid").is(rfid_num))
														);
												//logger.info("Query --> "+query);
												Update update = new Update().set("rfid_info.$.evening_out_bound", rf.getMorning_in_bound());

												mongoTemplate.updateMulti(query, update, "oneday_bus_information");
												logger.info("created evening outbound for " +rfid_num);
											}
										}
										else{
											
											//Insert into student evening out bound
											Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
											
											
											// MorningSession
											// morningSession=context.getBean("morningSession",MorningSession.class);
											rfid_info rf=context.getBean("rfid_info",rfid_info.class);
											rf.setMorning_in_bound(sdf.format(cal.getTime()));
											Query query = new Query(c1.andOperator(
													 
													  Criteria.where("rfid_info.rfid").is(rfid_num))
													);
											//logger.info("Query --> "+query);
											Update update = new Update().set("rfid_info.$.evening_in_bound", rf.getMorning_in_bound());

											mongoTemplate.updateMulti(query, update, "oneday_bus_information");
											logger.info("created evening inbound for " +rfid_num);
										}
									}
									else{
										//logger.info("Student morning in bound doesnot exists...");
										//Insert into student evening in bound
										Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
										
										
										// MorningSession
										// morningSession=context.getBean("morningSession",MorningSession.class);
										rfid_info rf=context.getBean("rfid_info",rfid_info.class);
										rf.setMorning_in_bound(sdf.format(cal.getTime()));
										Query query = new Query(c1.andOperator(
												 
												  Criteria.where("rfid_info.rfid").is(rfid_num))
												);
										//logger.info("Query --> "+query);
										Update update = new Update().set("rfid_info.$.evening_in_bound", rf.getMorning_in_bound());

										mongoTemplate.updateMulti(query, update, "oneday_bus_information");
										logger.info("created evening inbound for " +rfid_num);
									}
								}
								else{
									
									logger.info("Student morning session doesnot exists....");
									
								}
								
								//logger.info("Driver Morning out bound doesnot exists... so push data into morning session");
								//logger.info("Driver Evening in bound  exists... but not evening out bound so insert student data");
							}
						}

					} else {
						// push data into student morning in bound and morning out bound
						StudentMorningSession studentMorningSession = context
								.getBean("studentMorningSession",
										StudentMorningSession.class);
						MainRfidInfo mainRfidInfo=studentMorningSession.set(context, mongoTemplate);
						rfid_info student_rfid_info[]=mainRfidInfo.getRfid_info();
						//logger.info("Student rfid info ==> "+student_rfid_info);
						
						//String student_rfid=student_rfid_info.getRfid();
						if(student_rfid_info!=null){
							
							
							//logger.info("RFID_info  exists in collection ....");
							rfid_info current_student_rfid=null;
							for(int i=0;i<student_rfid_info.length;i++){
								
								rfid_info single=student_rfid_info[i];
								if(single.getRfid().equals(rfid_num)){
									//logger.info("Single student info --> "+single);
									current_student_rfid=single;
								}
							}
							if(current_student_rfid!=null){
								
								logger.info("Student morning in bound exists...");
								if(!current_student_rfid.getMorning_out_bound().equals("no")){
									
									logger.info("Student Morning session cannot be inserted beacuse [ multiple RFID tapping ]");
								}
								else{
									
									//Insert into student morning out bound
									Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
									
									
									// MorningSession
									// morningSession=context.getBean("morningSession",MorningSession.class);
									rfid_info rf=context.getBean("rfid_info",rfid_info.class);
									rf.setMorning_in_bound(sdf.format(cal.getTime()));
									Query query = new Query(c1.andOperator(
											 
											  Criteria.where("rfid_info.rfid").is(rfid_num))
											);
									//logger.info("Query --> "+query);
									Update update = new Update().set("rfid_info.$.morning_out_bound", rf.getMorning_in_bound());

									mongoTemplate.updateMulti(query, update, "oneday_bus_information");
									logger.info("created morning outbound for " +rfid_num);
								}
							}
							else{
								//logger.info("Student morning in bound doesnot exists...");
								//Insert into student morning in bound
								Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
								
								
								// MorningSession
								// morningSession=context.getBean("morningSession",MorningSession.class);
								rfid_info rf=context.getBean("rfid_info",rfid_info.class);
								rf.setMorning_in_bound(sdf.format(cal.getTime()));
								Query query = new Query(c1.andOperator(
										 
										  Criteria.where("rfid_info.rfid").is(rfid_num))
										);
								//logger.info("Query --> "+query);
								Update update = new Update().set("rfid_info.$.morning_in_bound", rf.getMorning_in_bound());

								mongoTemplate.updateMulti(query, update, "oneday_bus_information");
								logger.info("created morning outbound for " +rfid_num);
							}
						}
						else{
							//logger.info("Student morning in bound doesnot exists...");
							//Insert into student morning in bound
							Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
							Query query=new Query(c1);
							rfid_info rf=context.getBean("rfid_info",rfid_info.class);
							rf.setRfid(rfid_num);
							rf.setEvening_in_bound("no");
							rf.setEvening_out_bound("no");
							rf.setMorning_out_bound("no");
							rf.setMorning_in_bound(sdf.format(cal.getTime()));
							Update update=new Update().push("rfid_info", rf);
							mongoTemplate.updateFirst(query, update, "oneday_bus_information");
							logger.info("created morning inbound for " +rfid_num);
						}
						
						//logger.info("Driver Morning out bound doesnot exists... so push data into morning session");
					}
				} else {
					logger.info("MorningSession is null");
				}
			} catch (NullPointerException e) {
				logger.info("Today there is no Session..");
			}

		} else {
			logger.info("MorningSession is null");
		}
	}

	public void insertTimeIntoDriver(String rfid_num,ApplicationContext context) {
		logger.info("RFID [ " + rfid_num+ "] exists. with RFID Type [ Driver ]");
		mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String todayDate = dateFormat.format(date);
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// logger.info("Today's date --> "+todayDate);
		// BUS_101:09-01-2014
		Criteria c1 = Criteria.where("_id").is("BUS_101:"+todayDate);
		
		BasicQuery query = new BasicQuery(c1.getCriteriaObject());
		logger.info(query);
		// MorningSession
		// morningSession=context.getBean("morningSession",MorningSession.class);
		OneDayBusInfo oneDayBusInfo = mongoTemplate.findOne(query,OneDayBusInfo.class, "oneday_bus_information");
		driver_info driverInfo=oneDayBusInfo.getDriver_info();
		try{
			logger.info(driverInfo);
			if(driverInfo!=null){
				
				String mor_in=driverInfo.getMorning().getIn_bound();
				String mor_out=driverInfo.getMorning().getOut_bound();
				String eve_in=driverInfo.getEvening().getIn_bound();
				String eve_out=driverInfo.getEvening().getOut_bound();
				if(!mor_in.equals("no")){
					
					if(!mor_out.equals("no")){
						
						if(!eve_in.equals("no")){
							
							if(!eve_out.equals("no")){
								
								logger.info("Today's session closed cannot insert any data");
							}
							else{
								
								//insert into evening outbound
								Query query3 = new Query(c1);
								Update update = new Update().set("driver_info.evening.out_bound", sdf.format(cal.getTime()));

								mongoTemplate.updateMulti(query3, update, "oneday_bus_information");
								logger.info("Created Evening out bound for driver with RFID [ "+ rfid_num+" ]");
								logger.info("Today's session closed....");
							}
						}
						else{
							
							//insert into evening in bound
							Query query3 = new Query(c1);
							Update update = new Update().set("driver_info.evening.in_bound", sdf.format(cal.getTime()));

							mongoTemplate.updateMulti(query3, update, "oneday_bus_information");
							logger.info("Created Evening in bound for driver with RFID [ "+ rfid_num+" ]");
						}
					}
					else{
						//insert into morning out bound
						
						Query query3 = new Query(c1);
						Update update = new Update().set("driver_info.morning.out_bound", sdf.format(cal.getTime()));

						mongoTemplate.updateMulti(query3, update, "oneday_bus_information");
						logger.info("Created Morning out bound for driver with RFID [ "+ rfid_num+" ]");
					}
				}
			}
			else{
				//Insert into student morning in bound
				
				Query query2=new Query(c1);
				driver_info rf=context.getBean("driver_info",driver_info.class);
				
				morning mor=context.getBean("morning",morning.class);
				evening eve=context.getBean("evening",evening.class);
				rf.setEvening(eve);
				rf.setMorning(mor);
				mor.setIn_bound(sdf.format(cal.getTime()));
				mor.setOut_bound("no");
				eve.setIn_bound("no");
				eve.setOut_bound("no");
				Update update=new Update().set("driver_info", rf);
				mongoTemplate.updateFirst(query2, update, "oneday_bus_information");
				
				logger.info("Created Morning in bound for driver with RFID [ "+ rfid_num+" ]");
			}
		}
		catch(NullPointerException e){
			logger.info("Eception at insertTimeIntoDriver"+e);
		}
		
	}
}
