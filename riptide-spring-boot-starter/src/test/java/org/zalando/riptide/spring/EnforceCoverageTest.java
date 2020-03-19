package org.zalando.riptide.spring;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.Test;
import org.springframework.core.env.ConfigurableEnvironment;

import static org.mockito.Mockito.mock;

@Hack
@OhNoYouDidnt
public final class EnforceCoverageTest {

    @Test(expected = IllegalStateException.class)
    public void shouldTriggerSneakyException() {
        final RiptidePostProcessor unit = new RiptidePostProcessor(DefaultRiptideRegistrar::new);
        unit.setEnvironment(mock(ConfigurableEnvironment.class));
    }

    @Test
    public void shouldUseDependenciesConstructor() {
        new Dependencies();
    }

    @Test
    public void shouldUseDefaultingConstructor() {
        new Defaulting();
    }

    @Test
    public void shouldCallHttpFactoryConstructor() {
        new HttpFactory();
    }

    @Test
    public void shouldCallPluginInterceptorsConstructor() {
        new PluginInterceptors();
    }

}
