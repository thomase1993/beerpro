import android.content.Context;

import com.google.common.util.concurrent.AbstractScheduledService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Fridge;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FridgeTest {

    @Mock
    Context mMockContext;

    @Test
    public void FridgeRepositoryAddTest() {
        FridgeRepository rep = new FridgeRepository();
        Beer b = new Beer("Test", "Brauhaus", "Edeltropfen", "Weizen", "Id", 10 ,1);
        rep.addOrIncrementBeer(b, "user1");
        LiveData<List<Fridge>> f = rep.getContent("user1");
        assertEquals("1", f.getValue().get(0).getAmount());
        rep.addOrIncrementBeer(b, "user1");
        assertEquals("2", f.getValue().get(0).getAmount());
        rep.removeBeer(b, "user1");
        assertTrue(rep.getContent("user1").getValue().isEmpty());
    }
}
