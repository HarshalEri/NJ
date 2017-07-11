package com.abfl.automation.utilities;
import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.search.SubjectTerm;

import org.apache.commons.lang3.StringUtils;

import com.sun.mail.imap.IMAPFolder;

/**
 * Class contains method to check mail
 * @author Harshal.e
 *
 */
public class EmailController {
	private static Session mailSession = null;
	private static Transport transport = null;
	private static Store store = null;
	private static IMAPFolder folder = null;
	private static IMAPFolder trashFolder = null;
	private static Message [] messages ;
		
	public static void init() {
		try {
			if (EmailController.mailSession == null) {
				Properties props = new Properties();
				EmailController.mailSession = Session.getInstance(props, null);
			}
			transportInit();
			StoreInit();
		} catch (Exception e) {
			System.out.println("Exception occured in Init method:" + e.getMessage());
		}
	}

	public EmailController() {
	}

	private static void transportInit() {
		try {
			if (EmailController.transport == null || !EmailController.transport.isConnected()) {
				EmailController.transport = mailSession.getTransport("smtps");
				EmailController.transport.connect("smtp." + "gmail.com", "automation.samaritan@gmail.com", "@f0urt3ch");
			}
		} catch (Exception e) {
			System.out.println("Exception occured in transportInit method:" + e.getMessage());
		}
	}

	private static void StoreInit() {
		try {
			if (EmailController.store == null || !EmailController.store.isConnected()) {
				EmailController.store = EmailController.mailSession.getStore("imaps");
				EmailController.store.connect("imap." + "gmail.com", "automation.samaritan@gmail.com", "@f0urt3ch");
				EmailController.folder = (IMAPFolder) EmailController.store.getFolder("INBOX");
				EmailController.trashFolder = (IMAPFolder) EmailController.store.getFolder("[Gmail]/Trash");
			}
		} catch (Exception e) {
			System.out.println("Exception occured in StoreInit method:" + e.getMessage());
		}
	}

	/**
	 * Method to check email Received by volunteer.
	 * @param subject : Email Subject 
	 * @param isAttachmentPresent 
	 * @param attachmentName
	 * @return
	 * @throws Exception
	 */
	public static boolean isEmailReceived(String subject, boolean isAttachmentPresent, String attachmentName) throws Exception {
		String message = null;
		boolean attachmentFlag = false;
		try {
			EmailController.folder = (IMAPFolder) EmailController.store.getFolder("INBOX");
			EmailController.folder.open(Folder.READ_WRITE);
			EmailController.messages = EmailController.folder.search(new SubjectTerm(subject.toString()));
			if (EmailController.messages.length > 0) {
				System.out.println("Mail Subject : " + EmailController.messages[0].getSubject());
				System.out.println(EmailController.getText(EmailController.messages[0]));
				message = EmailController.messages[0].getSubject();
				if (isAttachmentPresent && messages != null)
					attachmentFlag = EmailController.isAttachmentReceived(attachmentName);
				else
					attachmentFlag = true;
				EmailController.messages[0].setFlag(Flags.Flag.DELETED, true);
			}
			EmailController.folder.close(true);
			if (message.equalsIgnoreCase(subject) && attachmentFlag)
				return true;

		} catch (Exception exception) {
			throw new Exception("Exception occured in isEmailReceived method:" + exception.getMessage());
		} finally {
			if (EmailController.folder.isOpen()) {
				EmailController.folder.close(true);
			}
		}
		return false;
	}
	
	 private static boolean textIsHtml = false; 
	/**
     * Return the primary text content of the message.
     */
    public static String getText(Part p) throws
                MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        return getText(bp);
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
	public static void close() {
		try {
			if (EmailController.folder.isOpen()) {
				EmailController.folder.close(true);
			}

			if (!EmailController.trashFolder.isOpen()) {
				EmailController.trashFolder.open(Folder.READ_WRITE);
			}

			EmailController.trashFolder.setFlags(EmailController.trashFolder.getMessages(),
					new Flags(Flags.Flag.DELETED), true);

			if (EmailController.trashFolder.isOpen()) {
				EmailController.trashFolder.close(true);
			}

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}
		/**
		 * Method to verify attachment in received mail by volunteer.
		 * @param attachmentName : Name of attachment to be verified
		 * 
		 * @return
		 * @throws Exception 
		 */
	public static boolean isAttachmentReceived(String attachmentName) throws Exception {
		try {
			Multipart multipart = (Multipart) EmailController.messages[0].getContent();
			BodyPart bodyPart = null;
			for (int i = 0; i < multipart.getCount(); i++) {
				bodyPart = multipart.getBodyPart(i);
				if (StringUtils.isNotBlank(bodyPart.getFileName())) {
					System.out.println("Attachment name : "+bodyPart.getFileName());
					if (bodyPart.getFileName().contains(attachmentName))
						return true;
				}
			}
		} catch (Exception e) {
			throw new Exception("Exception occured in [isAttachmentReceived()] method:" + e.getMessage());
		}
		return false;
	}
}