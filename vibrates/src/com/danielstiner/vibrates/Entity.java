/**
 * 
 */
package com.danielstiner.vibrates;


public class Entity {

	public enum Kind {

		Contact("contacts_contract_contact"), Group("contacts_contract_group"), App(
				"contact_vibrates_apptype");

		private String mName;

		private Kind(String name) {
			mName = name;
		}

		@Override
		public String toString() {
			return mName;
		}
		
		public static Kind fromString(String name) {
			
			for(Kind k : Entity.Kind.values())
			{
				if(k.mName.equals(name))
					return k;
			}
			
			throw new IllegalArgumentException(name + " not a valid kind name");
			
		}
		
		
	}

	static final String CLASSNAME = com.danielstiner.vibrates.Vibrates.NS + "."
			+ "Entity";

	public static final String EXTRA_KEY_ID = CLASSNAME + "." + "id";

	public static final Kind KIND_CONTACT = Kind.Contact;

	public static final Kind KIND_GROUP = Kind.Group;

	public static final Kind KIND_APP = Kind.App;

	/**  */
	public static final int ID_NOBODY = -3;

	public static final int ID_DEFAULT = -4;

	private Long _id;

	private Kind mKind;

	private int mNotify;

	private String mName;

	private Pattern mPattern;

	/**
	 * Create default entity representing nobody in particular
	 * 
	 * @param identifier
	 */
	public Entity() {
		entityid(new Long(ID_NOBODY));
	}

	public Long entityid() {
		return _id;
	}

	public Entity entityid(Long entityid) {
		_id = entityid;
		return this;
	}

	public void setKind(Kind kind) {
		this.mKind = kind;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void setNotifyCount(int count) {
		this.mNotify = count;
	}

	public void setPattern(Pattern pattern) {
		this.mPattern = pattern;
	}

	public String getName() {
		return mName;
	}

	public Pattern getPattern() {
		return mPattern;
	}
}