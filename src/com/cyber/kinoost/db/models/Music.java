package com.cyber.kinoost.db.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "music")
public class Music {
	
	public final static String ID_FIELD_NAME = "id";
	public final static String PERFORMER_ID_FIELD_NAME = "performer_id";
	
	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField(index = true)
	String name;
	@DatabaseField(index = true)
	double rating;
	@DatabaseField(foreign = true, columnName = PERFORMER_ID_FIELD_NAME)
	private Performer performer;
	
	public static String getPerformerIdFieldName() {
		return PERFORMER_ID_FIELD_NAME;
	}

	public Music() {
	}

	public Music(int id, String name, double rating, Performer performer) {
		super();
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.performer = performer;
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public Performer getPerformer() {
		return performer;
	}
	
	public void setPerformer(Performer performer){
		this.performer = performer;
	}
	
	public void setPerformerJackson(JsonNode jsonNode) throws JsonProcessingException {
		performer = new ObjectMapper().treeToValue(jsonNode, Performer.class);
	}

	@Override
	public String toString() {
		return "Music [id=" + id + ", name=" + name + ", rating=" + rating
				+ ", performer=" + performer + "]";
	}
}
