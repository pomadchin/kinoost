package com.cyber.kinoost.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "film")
public class Film {
	public final static String ID_FIELD_NAME = "id";
	
	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField(index = true)
	String name;
	@DatabaseField(index = true)
	int year;
	@DatabaseField
	String img;
	@DatabaseField(index = true)
	double rating;
	
	public Film() {
	}
	
	public Film(int id, String name, int year, String img, double rating) {
		super();
		this.id = id;
		this.name = name;
		this.year = year;
		this.img = img;
		this.rating = rating;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Film [id=" + id + ", name=" + name + ", year=" + year
				+ ", img=" + img + ", rating=" + rating + "]";
	}	
}
