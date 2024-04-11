package org.apache.commons.mail;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

public class EmailConcrete extends Email {
	@Override
	public Email setMsg(String msg) throws EmailException {
		return null;
	}
	public Map<String, String> getHeaders() {
	    return this.headers;
	}
	public List<InternetAddress> getReplyTo() {
		return this.replyList;
	}
	

} 
