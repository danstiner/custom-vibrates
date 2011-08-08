package com.danielstiner.vibrates.database;

import java.io.InputStream;
import java.util.List;

import roboguice.util.Ln;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class Manager implements IManager {

	//private Provider<Entity> entity_provider;
	private Context activity;
	private Provider<IEntityManager> entitymanager_provider;
	private Provider<IIdentifierManager> identifiermanager_provider;
	private Provider<IPatternManager> patternmanager_provider;
	
	@Inject private IEntityManager entity_manager;
	@Inject private IIdentifierManager identifier_manager;
	@Inject private IPatternManager pattern_manager;
	
	@Inject
    public Manager(
    		//Provider<Entity> entity_provider,
    		Context activity
    		//Provider<IEntityManager> entitymanager_provider,
    		//Provider<IIdentifierManager> identifiermanager_provider,
    		//Provider<IPatternManager> patternmanager_provider) {
    		) {
    	//this.entity_provider = entity_provider;
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
    	//getIdentifierManager().add(entity, identifier);
    	//getIdentifierManager().add(entity, name);    
        
        return entity;
	}
	
	@Override
	public Entity getEntity(String identifier) {
		return getIdentifierManager().get(identifier);
	}

	@Override
    public Entity createFromContactUri(Uri contact_uri) {
		if(contact_uri == null)
			return null;
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
					ContactsContract.Contacts.LOOKUP_KEY,
					ContactsContract.Contacts._ID
					},
				null,
				null,
				null);
		String name;
		String lookup;
		Long id;
		try {
		    c.moveToFirst();
		    // Grab some info
		    name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		    lookup = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
		    id = c.getLong(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
		} finally {
		    c.close();
		}
		if(name == null || lookup == null)
			return null;
		
	    // Generate a default contact pattern
	    long[] pattern = PatternManager.generate(name);
	    
	    Entity e = create(lookup, name, pattern, Entity.TYPE_CONTACTSCONTRACTCONTACT);
	    
	    // Add some lookup identifiers
	    getIdentifierManager().add(e, name, IdentifierManager.KIND_CONTACTS_CONTRACT_NAME);
	    getIdentifierManager().add(e, lookup, IdentifierManager.KIND_CONTACTS_CONTRACT_LOOKUP);
	    getIdentifierManager().add(e, id.toString(), IdentifierManager.KIND_CONTACTS_CONTRACT_ID);
	    
	    return e;
		    
		
		
    }
	
	@Override
	public Cursor getEntities() {
		return getEntityManager().getAll();
	}

	@Override
	public String getDisplayName(Entity entity) {
		return getEntityManager().getDisplayName(entity);
	}
	
	@Override
	public String getKind(Entity entity) {
		return getEntityManager().getKind(entity);
	}
	
	@Override
	public InputStream getPhotoStream(Entity entity) {
		// TODO Switch based on entity kind maybe?
		Cursor c = getIdentifierManager().get(entity, IdentifierManager.KIND_CONTACTS_CONTRACT_ID);
		
		if(!c.moveToFirst())
			return null;
		
		String contacts_contract_idstring = getIdentifierManager().identifierFromCursor(c);
		
		try {
			Long contacts_contract_id = Long.parseLong(contacts_contract_idstring);
			
			Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contacts_contract_id);
			InputStream input = Contacts.openContactPhotoInputStream(activity.getContentResolver(), uri);
			return input;
		} catch(NumberFormatException e) {
			return null;
		}
	}
	
	@Override
	public long[] getPattern(Entity entity, String type) {
		// TODO type based pattern appending
		return getEntityManager().getPattern(entity);
	}
	
	@Override
	public void setPattern(Entity entity, long[] pattern) {
		getEntityManager().setPattern(entity, pattern);
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
	public void update(Entity entity) {
		// TODO Auto-generated method stub
		
	}
}
