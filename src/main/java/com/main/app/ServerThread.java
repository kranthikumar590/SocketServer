package com.main.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.client.data.Data;
import com.client.data.InsertRfidData;
import com.client.data.Rfid;



class ServerThread extends Thread {
	private static final Logger logger = Logger.getLogger(ServerThread.class);
	String line = null;
	BufferedReader is = null;
	PrintWriter os = null;
	Socket s = null;
	private ApplicationContext context;

	public  ServerThread(Socket s, ApplicationContext context) {
		this.s = s;
		this.context = context;
	}

	public void run() {
		try {
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());

		} catch (IOException e) {
			logger.info("IO error in server thread");
		}

		try {
			line = is.readLine();

			while (line.compareTo("QUIT") != 0) {

				os.println(line);
				os.flush();
				Data data = context.getBean("data", Data.class);
				data.setDataLine(line);
				logger.info("Data Received is [ " + data.getDataLine() + " ]");
				try{
					String arr[]=data.getDataLine().split(",");
					
					if(arr[0].equals("R")){
						
						InsertRfidData insertRfid=context.getBean("insertRfid",InsertRfidData.class);
						insertRfid.setRfid(arr[1]);
						Rfid rfid=insertRfid.rfidExists(context);
						if(rfid!=null){
							
							
							if(rfid.getType().equals("student"))	
								insertRfid.insertTimeIntoStudent(rfid.getRfid_number(),context);
							if(rfid.getType().equals("driver"))
								insertRfid.insertTimeIntoDriver(rfid.getRfid_number());
						}
						else{
							logger.info("RFID [ " +insertRfid.getRfid()+"] not valid / not registered....");
						}
						
					}
					else if(arr[0].equals("G")){
						//GPRSData gprsData=context.getBean("gprs",GPRSData.class);
						
					}
				}
				catch(Exception e){
					logger.info("Exception with data ... Exception type [" +e.getMessage()+" ]");
				}
				
				line = is.readLine();
			}
		} catch (IOException e) {

			line = this.getName();
			logger.info("IO Error/ Client [" +s.getRemoteSocketAddress()+" ] "+ line + " terminated abruptly");
		} catch (NullPointerException e) {
			line = this.getName();
			logger.info("Client " + line + " Closed");
		}

		finally {
			try {

				if (is != null) {
					is.close();

				}

				if (os != null) {
					os.close();

				}
				if (s != null) {
					s.close();

				}
				logger.info("Connection Closing..");
			} catch (IOException ie) {
				logger.info("Socket Close Error");
			}
		}
	}
}