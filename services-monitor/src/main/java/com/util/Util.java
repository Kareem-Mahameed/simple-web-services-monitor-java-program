/**
 * 
 */
package com.util;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import org.apache.log4j.Logger;

import com.servicemonitoring.ServicePollWorker;

//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author Kareem
 *
 */
public class Util {
	
	protected static Logger logger=Logger.getLogger(ServicePollWorker.class.getSimpleName());
	private String emailSubject;
	private String emailText;
	
	public static boolean pollService(final String host, final int port) throws UnknownHostException, IOException {
		Socket socket = null;
		boolean status = false;
		try {
			socket = SocketFactory.getDefault().createSocket(host, port);
			status = socket.isConnected();
			logger.debug(host + ":" + port + " IS_CONNECTED: " + status);
		} catch (UnknownHostException e) {
			logger.warn("ERROR unknown host: " + e);
		} catch (IOException e) {
			logger.error("ERROR IO: " + e);
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
		return status;
	}
	public static void sendNotificationEmail(final String toEmailAddress, final String host, int port) {
		//TODO later 

		boolean isEmailSent = false;
		
		try {
			//SimpleMailMessage message = new SimpleMailMessage(); 
	        //message.setTo(toEmailAddress); 
	        //message.setSubject(emailSubject); 
	        //message.setText(String.format(emailText, host, port));
	        //emailSender.send(message);
	        isEmailSent = true;
			
		} catch(Exception mailException) {
			isEmailSent = false;
		}


	}

}
