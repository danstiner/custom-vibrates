package com.danielstiner.vibrates;

public class Identifier {

	public enum Kind {

		Default("unknown"), SystemContactId("generated.contacts_contract_id"), SystemContactLookup(
				"generated.contacts_contract_lookup"), SystemContactName(
				"generated.contacts_contract_name"), GroupId(
				"generated.contacts_group_id"), GroupTitle(
				"generated.contacts_group_title");

		private String mName;

		private Kind(String name) {
			mName = name;
		}

		@Override
		public String toString() {
			return mName;
		}

		public static Kind fromString(String name) {

			for (Kind k : Identifier.Kind.values()) {
				if (k.mName.equals(name))
					return k;
			}

			throw new IllegalArgumentException(name + " not a valid kind name");

		}

	}

	private String mIdentifier;
	private Kind mKind;

	public Identifier() {
		this(null, null);
	}

	public Identifier(String identifier, Kind kind) {
		mIdentifier = identifier;
		mKind = kind;
	}

	public String getIdentifier() {
		return mIdentifier;
	}

	public Identifier setIdentifier(String identifier) {
		mIdentifier = identifier;
		return this;
	}

	public Kind getKind() {
		return mKind;
	}

	public Identifier setKind(Kind kind) {
		mKind = kind;
		return this;
	}

}
