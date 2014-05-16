package com.cyber.kinoost.db.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "performer")
public class Performer implements Serializable {
	public final static String ID_FIELD_NAME = "id";
	public final static String NAME_FIELD_NAME = "name";
	
	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField(index = true, columnName = NAME_FIELD_NAME)
	String name;
	
	public Performer() {
	}

	public Performer(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Performer [id=" + id + ", name=" + name + "]";
	}	
}
