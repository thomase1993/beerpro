import android.content.Context;

import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.MyBeerFromFridge;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MyBeerFromFridgeTest {

    @Mock
    Context mMockContext;

    @Test
    public void FridgeRepositoryAddTest() {
        FirebaseApp.initializeApp(mMockContext);
    }
}
