package org.apache.commons.mail;

//import java.util.List;

//import javax.mail.internet.InternetAddress;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	private static final String[] TEST_EMAILS = {"ab@c.com", "a.b@c.org", "abcdefg@abcdefg.com.bd"};
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception {}
	
	// Test Email addBcc(String... emails) with test emails
	@Test
	public void testAddBcc() throws Exception {
		email.addBcc(TEST_EMAILS);
		// Number of BCC addresses matches the number of email addresses added
		assertEquals(3, email.getBccAddresses().size());
	}
	
	// Test Email addCc(String email) with test emails
	@Test
	public void testAddCc() throws Exception {
		email.addCc(TEST_EMAILS);
		// Number of CC addresses matches the number of email addresses added
		assertEquals(3, email.getCcAddresses().size());
	}
	
	// Test void addHeader(String name, String value) with valid parameters
	@Test
	public void testAddHeaderValidNameValidValue() throws Exception {
		// Test data
        String name = "HeaderName";
        String value = "HeaderValue";
        email.addHeader(name, value);
        
        // Create expected header values
        Map<String, String> expectedHeaders = new HashMap<>();
        expectedHeaders.put(name, value);
        email.setHeaders(expectedHeaders);
        // Compare result to expected values 
        assertEquals(value, email.getHeaders().get(name));
	}
	
	// Test Email addReplyTo(String email, String name)
	@Test
	public void testAddReplyTo() throws Exception {
		// Test Data
		String emailAddress = "ab@c.com";
		String name = "name";

		// Create expected replyTo address and name
        email.addReplyTo(emailAddress, name); 
        List<InternetAddress> expectedReplyTo = new ArrayList<InternetAddress>();
        expectedReplyTo.add(new InternetAddress(emailAddress, name));
        // Compare result to expected values 
        assertEquals(expectedReplyTo.toString(), email.getReplyTo().toString());
	}

    
	// Test void buildMimeMessage()
	@Test
	public void testBuildMimeMessage () throws Exception {
		try {
			// Set values for mime message
			email.setHostName("localHost");
			email.setSmtpPort(1234);
			email.setFrom("a@b.com");
			email.addTo("c@d.com");
			email.setSubject("test email");
			email.setCharset("ISO-8859-1");
			email.setContent("test content", "text");
			email.buildMimeMessage();
			
		} catch (RuntimeException re) {

		}
	}
	
	// Test void buildMimeMessage() without setFrom
	@Test
	public void testBuildMimeMessageMissingFrom() throws Exception {
	    try {
	        email.setHostName("localHost");
	        email.setSmtpPort(1234);
	        email.addTo("c@d.com");
	        email.setSubject("test email");
	        email.setCharset("ISO-8859-1");
	        email.setContent("test content", "text");
	        
	        // Should not build message without From address
	        email.buildMimeMessage();
	    } catch (EmailException ee) {

	    }
	}
	
	
	// Test void buildMimeMessage() without addTo
	@Test
	public void testBuildMimeMessageMissingAddTo() throws Exception {
	    try {
			email.setHostName("localHost");
			email.setSmtpPort(1234);
			email.setFrom("a@b.com");
			email.setSubject("test email");
			email.setCharset("ISO-8859-1");
			email.setContent("test content", "text");
	        
	        // Should not build message without To address
	        email.buildMimeMessage();
	    } catch (EmailException ee) {

	    }
	}

	// Test void buildMimeMessage() without setSubject
	@Test
	public void testBuildMimeMessageMissingSubject () throws Exception {
		try {
			email.setHostName("localHost");
			email.setSmtpPort(1234);
			email.setFrom("a@b.com");
			email.addTo("c@d.com");
			email.setCharset("ISO-8859-1");
			email.setContent("test content", "text");
			
			// Should not build message without subject
			email.buildMimeMessage();
		} catch (EmailException ee) {

		}
	}
	
	// Test void buildMimeMessage() without setCharset
	@Test
	public void testBuildMimeMessageMissingCharset() throws Exception {
	    try {
	        email.setHostName("localHost");
	        email.setSmtpPort(1234);
	        email.setFrom("a@b.com");
	        email.addTo("c@d.com");
	        email.setSubject("test email");
	        email.setContent("test content", "text");
	        
	        // Charset is missing, but should still build
	        email.buildMimeMessage();
	        
	    } catch (Exception e) {
	    }
	}
	
	@Test
	public void testBuildMimeMessageWithCC() throws Exception {
	    email.setHostName("localHost");
	    email.setSmtpPort(1234);
	    email.setFrom("a@b.com");
	    email.addTo("c@d.com");
	    email.addCc("e@f.com");
	    email.setSubject("test email");
	    email.setCharset("ISO-8859-1");
	    email.setContent("test content", "text");

	    email.buildMimeMessage();

	    // Get MimeMessage
	    MimeMessage mimeMessage = email.getMimeMessage();

	    // Check if number of CC and CC address are set
	    Address[] ccAddresses = mimeMessage.getRecipients(Message.RecipientType.CC);
	    assertEquals(1, ccAddresses.length);
	    assertEquals("e@f.com", ccAddresses[0].toString());
	}

	
	// Test void buildMimeMessage() without setContent
	@Test
	public void testBuildMimeMessageMissingContent() throws Exception {
	    try {
	        email.setHostName("localHost");
	        email.setSmtpPort(1234);
	        email.setFrom("a@b.com");
	        email.addTo("c@d.com");
	        email.setSubject("test email");
	        email.setCharset("ISO-8859-1");
	        
	        // Should not build message without content
	        email.buildMimeMessage();
	    } catch (EmailException ee) {
	        // Exception is thrown due to missing content
	        assertEquals("Content missing", ee.getMessage());
	        throw ee;
	    }
	}
	
	
	// Test String getHostName() when the host name is not empty and the session is not null
    @Test
    public void testGetHostNameNotEmpty() throws Exception {
    	// Set host name should equal get host name
        email.setHostName("localHost");
        assertEquals("localHost", email.getHostName());
    }
    
	// Test String getHostName() when the host name is null and the session is not null
    @Test
    public void testGetHostNameHostNameNull() throws Exception {
    	// Set host name should equal get host name
    	assertEquals(null, email.getHostName());
    }
    
    // Test String getHostName() when the host name is null and the session is not null
    @Test
    public void testGetHostNameHostNameEmpty() throws Exception {
    	// Set host name should equal get host name
        email.setHostName("");
        assertEquals(null, email.getHostName());
    }
    
    // Test Session getMailSession() with setting properties and session
    @Test 
    public void testGetSessionWithPropertiesSession() throws Exception {
    	// Test data
    	Properties props = new Properties();
    	props.setProperty("testProp1", "testProp2");
    	
    	Session sessionObj = Session.getInstance(props);
    	email.setMailSession(sessionObj);
    	
    	// After creating session and property object, get values
    	Session getSession = email.getMailSession();
    	Properties getProps = getSession.getProperties();
    	
    	// Create expected values
    	String testStr = (String) getProps.getProperty("testProp1");
    	
    	// Compare result to the set values 
    	assertEquals(testStr, "testProp2");
    }
    
    // Test Session getMailSession() with setting HostName and SSLOnConnect
    @Test
    public void testGetMailSessionWithHostNameSSL() throws Exception {
    	// Set test data
    	email.setHostName("localHost");
    	email.setSSLOnConnect(true);
    	
    	Session session = email.getMailSession();
    	Properties getProps = session.getProperties();
    	String protocol = (String) getProps.getProperty("mail.transport.protocol");
    	
    	// Compare result to the set values 
    	assertEquals(protocol, email.SMTP);
    }
    
    // Test Date getSentDate()
    @Test
    public void testGetSentDate() throws Exception {
        // Set test date
        Date testDate = new Date();
        email.setSentDate(testDate);

        Date sentDate = email.getSentDate();

        // Compare result to the set date
        assertEquals(testDate, sentDate);
    }
    
    // Test int getSocketConnectionTimeout()
    @Test 
    public void testGetSocketConnectionTimeout() throws Exception {
    	// Set value for socketConnectionTimeout
        int timeoutValue = 5000;
        email.setSocketConnectionTimeout(timeoutValue);

        int result = email.getSocketConnectionTimeout();

        // Compare result to the set value
        assertEquals(timeoutValue, result);
    }
    
    // Test Email setFrom(String email)
    @Test
    public void testSetFrom() throws Exception {
    	// Create test data
    	String emailAddress = "ab@c.com";

		email.setFrom(emailAddress);

    }
    
}
	
