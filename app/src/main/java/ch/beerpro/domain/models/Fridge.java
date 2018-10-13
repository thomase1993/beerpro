package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

import javax.annotation.Nonnull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Fridge implements Entity {
    public static final String COLLECTION = "fridge";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_CREATION_DATE = "creationDate";
    public static final String FIELD_amount = "amount";

    @Exclude
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private int amount;
    @Nonnull
    private String beerId;

    private Date creationDate = new Date();

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }
}
