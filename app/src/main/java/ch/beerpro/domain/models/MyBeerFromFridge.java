package ch.beerpro.domain.models;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class MyBeerFromFridge implements MyBeer {
    public Beer b;
    public Fridge f;

    public MyBeerFromFridge(Beer b, Fridge f) {
        this.b = b;
        this.f = f;
    }

    @Override
    public String getBeerId() {
        return b.getId();
    }

    @Override
    public Beer getBeer() {
        return b;
    }

    @Override
    public Date getDate() {
        return f.getCreationDate();
    }
}
