package com.danielstiner.vibrates.database;

import java.util.List;

import roboguice.util.Ln;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class Manager implements IManager {

	private Provider<Entity> entity_provider;
	private Activity activity;
	private Provider<IEntityManager> entitymanager_provider;
	private Provider<IIdentifierManager> identifiermanager_provider;
	private Provider<IPatternManager> patternmanager_provider;
	
	@Inject private IEntityManager entity_manager;
	@Inject private IIdentifierManager identifier_manager;
	@Inject private IPatternManager pattern_manager;
	
	@Inject
    public Manager(
    		Provider<Entity> entity_provider,
    		Activity activity
    		//Provider<IEntityManager> entitymanager_provider,
    		//Provider<IIdentifierManager> identifiermanager_provider,
    		//Provider<IPatternManager> patternmanager_provider) {
    		) {
    	this.entity_provider = entity_provider;
    	this.activity = activity;
    	//this.entitymanager_provider = entitymanager_provider;
    	//this.identifiermanager_provider = identifiermanager_provider;
    	//this.patternmanager_provider = patternmanager_provider;
    }
	
	
	private Entity create(String identifier, String name, long[] pattern, String type) {
    	// First first see if such an entity already exists
    	Entity entity = getEntity(identifier);
    	if(entity != null)
    		return entity;
    	
    	// Do some validation
    	if(!getPatternManager().isValid(pattern)) {
    		Ln.e("Invalid pattern given to create a new contact.");
    	}
    	
    	// Try and add such an entity
    	entity = getEntityManager().create(name, pattern, type);
    	
    	// Now map some identifiers
    	getIdentifierManager().add(entity, identifier);
    	getIdentifierManager().add(entity, name);    
        
        return entity;
	}
	
	@Override
	public Entity getEntity(String identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public Entity createFromContactUri(Uri contact_uri) {
    	// Example uri: content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
		Ln.v("Got a result: %s", contact_uri.toString());
		// get the contact id from the Uri
		Long origId = Long.parseLong(contact_uri.getLastPathSegment());
		
		// get the permanent lookup key in case the id changes
		List<String> resultSegments = contact_uri.getPathSegments();
		// hint, its the second to last segment
		String origLookupKey = resultSegments.get(resultSegments.size()-2);
		
		// TODO fix identifier assumptions
		// Grab some info from the system contact service
		Cursor c = activity.getContentResolver().query(
				ContactsContract.Contacts.getLookupUri(origId, origLookupKey),
				new String[] {
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.LOOKUP_KEY
					},
				null,
				null,
				null);
		try {
		    c.moveToFirst();
		    // Grab some info
		    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		    String lookup = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
		    
		    // Generate a default contact pattern
		    long[] pattern = PatternManager.generate(name);
		    
		    return create(lookup, name, pattern, Entity.TYPE_CONTACTSCONTRACTCONTACT);
		    
		} finally {
		    c.close();
		}
		
    }
	
	private IEntityManager getEntityManager()
	{
		if(entity_manager == null)
			entity_manager = entitymanager_provider.get();
		return entity_manager;
	}
	private IIdentifierManager getIdentifierManager()
	{
		if(identifier_manager == null)
			identifier_manager = identifiermanager_provider.get();
		return identifier_manager;
	}
	private IPatternManager getPatternManager()
	{
		if(pattern_manager == null)
			pattern_manager = patternmanager_provider.get();
		return pattern_manager;
	}


	@Override
	public Cursor getEntities() {
		return getEntityManager().getAll();
	}
}
