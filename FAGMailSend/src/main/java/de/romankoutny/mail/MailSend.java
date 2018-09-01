package de.romankoutny.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSend 
{
	private static Session session;
	private static Properties props = new Properties();

	
	public static void main(String[] args) {
		if(args.length < 1)
		{
			System.err.println("to Angabe fehlt");
			System.exit(1);
		}

		doSend(args[0]);
	}


    private static void initialize()
    {
        setProp("mail.smtp.host", "MAIL_HOST");
        setProp("mail.smtp.port", "MAIL_PORT");
        props.put("mail.transport.protocol", "smtp");
        setProp("mail.smtp.auth", "MAIL_SMTP_AUTH");
        setProp("mail.smtp.starttls.enable", "MAIL_SMTP_STARTTLS_ENABLE");
        setProp("mail.smtp.tls", "MAIL_SMTP_STARTTLS_ENABLE");
        setProp("mail.smtp.user", "MAIL_USERNAME");
        setProp("mail.password", "MAIL_PASSWORD");
        props.put("mail.debug", "true");
        
        String password = props.getProperty("mail.password");
        String user =  props.getProperty("mail.smtp.user");
        
        session = Session.getDefaultInstance(props, new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, password);
            }
        });

        // Get a Session object
        session.setDebug(true);
    }	
	
    private static void setProp(String p, String sysp)
    {
    	String s = System.getProperty(sysp);
    	if(s != null)
    	{
    		props.put(p, s);
    	}
    }
    
    private static void doSend(String to)
    {
        initialize();

    	String from = System.getProperty("MAIL_SENDERNAME");
    	String cc = null;
    	String bcc = null;
    	String subject = "kiu Testmail";
    	String messageText = "Das ist eine Testmail von kiu!";
    	
        try
        {
            // construct the message
            Message msg = new MimeMessage(session);
            if (from != null)
            {
                msg.setFrom(new InternetAddress(from));
            }
            else
            {
                msg.setFrom();
            }

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            if (cc != null)
            {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
            }
            if (bcc != null)
            {
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
            }

            msg.setSubject(subject);

            msg.setText(messageText);

            msg.saveChanges();
            
//            msg.setHeader("X-Mailer", "roman" /* mailer */);
//            msg.setSentDate(new Date());

            // send the thing off
            Transport.send(msg);

            System.out.println("\nMail was sent successfully.");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
	
}
