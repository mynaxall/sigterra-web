package itomy.sigterra.web.rest;

import itomy.sigterra.SigterraWebApp;
import itomy.sigterra.domain.CardletFooter;
import itomy.sigterra.repository.CardletFooterRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SigterraWebApp.class)
public class CardletViewResourceTest {

    @Inject
    CardletFooterRepository cardletFooterRepository;

    @Before
    public void setUp() throws Exception {

    }
}
