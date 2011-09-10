package com.danielstiner.vibrates.utility;

public class NotificationTypes {
	
	public static final String DEFAULT  = "";
	public static final String CHAT     = particularizeType(DEFAULT, "Chat");
	public static final String HARDWARE = particularizeType(DEFAULT, "Hardware");
	public static final String MESSAGE  = particularizeType(DEFAULT, "Messaging");
	public static final String UPDATE   = particularizeType(DEFAULT, "Updates");
	public static final String VOICE    = particularizeType(DEFAULT, "Voice");
	
	// Listing of all types used in the application
	public static final String DEFAULT_PHONENUMBER  = "";
	public static final String MESSAGE_EMAIL        = particularizeType(MESSAGE, "email");
	public static final String CHAT_SMS             = particularizeType(CHAT, "SMS");
	public static final String UPDATE_STATUS_UPDATE = particularizeType(UPDATE, "Status Updates");
	
	/** List of root types for generating a tree of notification types */
	public static final String[] COMMON_TYPES = {
		CHAT,
		HARDWARE,
		MESSAGE,
		UPDATE,
		VOICE
		};
	
	/**
	 * 
	 * @param type
	 * @param specifier
	 * @return
	 */
	public static String particularizeType(String type, String specifier) {
		return type + "/" + specifier;
	}
}
