package com.gps.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ProcessGPS {

	private static final Logger logger = Logger.getLogger(ProcessGPS.class);
	public void process(String recvData1,ApplicationContext context,MongoTemplate mongoTemplate){
		
		logger.info(recvData1);
		String recvData[]=recvData1.split(",");
		if(recvData.length == 5 ){
			
			try{
				
				String firstSign = null,secondSign = null;
				for(int i=0;i<recvData.length;i++){
					
					recvData[i]=recvData[i].replaceAll("\\s","");
				}
				if(recvData[2].equals("N") && recvData[4].equals("E") ){
					
					firstSign="+";
					secondSign="+";
				}
				if(recvData[2].equals("S") && recvData[4].equals("W") ){
					firstSign="-";
					secondSign="-";
				}
				if(recvData[2].equals("N") && recvData[4].equals("W") ){
					
					firstSign="+";
					secondSign="-";
				}
				if(recvData[2].equals("S") && recvData[4].equals("E") ){
					firstSign="-";
					secondSign="+";
				}
				insertData(recvData[1], recvData[3],firstSign,secondSign,context,mongoTemplate);
			}
			catch(Exception e){
				logger.info("Invalid GPRS Data [ "+recvData1 +" ]");
			}
			
		}
		else{
			
			logger.info("Invalid GPRS Data [ "+recvData1 +" ]");
		}
	}
	public void insertData(String Lat,String Long,String firstSign,String secondSign,ApplicationContext context,MongoTemplate mongoTemplate){
		
		double LAT=0,LONG=0;
		try{
			LAT=convert(Lat);
			LONG=convert(Long);
			if(LAT==9999999 || LONG==9999999){
				 System.out.println("Invalid GPRS data.....");
			}
			else{
				Calendar cal = Calendar.getInstance();
		    	cal.getTime();
		    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date = new Date();
				String todayDate = dateFormat.format(date);
				Map map=context.getBean("map",Map.class);
				map.setLat(firstSign+LAT);
				map.setLong(secondSign+LONG);
				map.setArrived_time(sdf.format(cal.getTime()));
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
		catch(Exception e){
			logger.info("Invalid GPRS Data [ "+Lat+" , "+Long +" ]");
		}
	}
public float convert(String pos){
		
		Float f=null;
		String s4 = null,s5 = null;
		try{
			 f=Float.parseFloat(pos);
			//System.out.println(f +" -->  value" );
				char[] ch=f.toString().toCharArray();
				for(int i=0;i<ch.length;i++){
					
					//System.out.println(ch[i]);
				}
				//System.out.println(ch.length+" --> len");
				char[] s1=Arrays.copyOfRange(ch, 0, 2);	
				char[] s2=Arrays.copyOfRange(ch, 2, 4);	
				char[] s3=Arrays.copyOfRange(ch, 4, 9);	
				//System.out.println(new String(s1)+"  s1..."+new String(s2)+"  s2..."+new String(s3)+"  s3...");
				
				 s4=new String(s2)+new String(s3);
				 s5=new String(s1);
				System.out.println(Float.parseFloat(s5)+Float.parseFloat(s4)/60);
				return (Float.parseFloat(s5))+(Float.parseFloat(s4)/60);
				
		}
		catch(NumberFormatException e){
			
			
			return 9999999;
		}
		
		
		
		
	}
}
