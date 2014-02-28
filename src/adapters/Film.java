package adapters;

public class Film {
	  
	  public String name;
	  int price;
	  int image;
	  public boolean box;
	  

	  public Film(String _describe, int _price, int _image, boolean _box) {
	    name = _describe;
	    price = _price;
	    image = _image;
	    box = _box;
	  }
	}