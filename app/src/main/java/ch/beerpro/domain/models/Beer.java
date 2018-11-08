package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beer implements Entity, Serializable {

    public static final String COLLECTION = "beers";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_AVGPRICE = "avgPrice";
    public static final String FIELD_NUMPRICES = "numPrices";
    public static final String FIELD_MAXPRICE = "maxPrice";
    public static final String FIELD_MINPRICE = "minPrice";

    @Exclude
    private String id;
    private String manufacturer;
    private String name;
    private String category;
    private String photo;
    private float avgRating;
    private int numRatings;
    private float avgPrice;
    private int numPrices;
    private float minPrice;
    private float maxPrice;

}
