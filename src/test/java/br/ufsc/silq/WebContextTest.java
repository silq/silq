package br.ufsc.silq;

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.ufsc.silq.config.Constants;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles({ Constants.SPRING_PROFILE_FAST, Constants.SPRING_PROFILE_TEST })
@Transactional
public abstract class WebContextTest {

}
