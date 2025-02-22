package org.jboss.hal.testsuite.test.configuration.microprofile.metrics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.dmr.ModelNode;
import org.jboss.hal.testsuite.CrudOperations;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.category.Microprofile;
import org.jboss.hal.testsuite.creaper.ManagementClientProvider;
import org.jboss.hal.testsuite.dmr.ModelNodeGenerator;
import org.jboss.hal.testsuite.page.configuration.MicroprofileMetricsPage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;
import org.wildfly.extras.creaper.core.online.operations.Operations;

import static org.jboss.hal.dmr.ModelDescriptionConstants.PREFIX;
import static org.jboss.hal.testsuite.fixtures.microprofile.MicroprofileMetricsFixtures.EXPOSED_SUBSYSTEMS;
import static org.jboss.hal.testsuite.fixtures.microprofile.MicroprofileMetricsFixtures.EXPOSE_ALL_SUBSYSTEMS;
import static org.jboss.hal.testsuite.fixtures.microprofile.MicroprofileMetricsFixtures.MICROPROFILE_METRICS_ADDRESS;
import static org.jboss.hal.testsuite.fixtures.microprofile.MicroprofileMetricsFixtures.SECURITY_ENABLED;

@Category(Microprofile.class)
@RunWith(Arquillian.class)
public class MicroprofileMetricsTest {

    private static final OnlineManagementClient client = ManagementClientProvider.createOnlineManagementClient();
    private static final Operations operations = new Operations(client);
    private static final String STAR = "*";

    private static String defaultPrefixValue = "";
    private static ModelNode defaultExposedSystem;

    @BeforeClass
    public static void getInitialPrefixValue() throws IOException {
        defaultPrefixValue = operations.readAttribute(MICROPROFILE_METRICS_ADDRESS, PREFIX).stringValue("result");
        defaultExposedSystem = operations.readAttribute(MICROPROFILE_METRICS_ADDRESS, EXPOSED_SUBSYSTEMS).value();
        operations.writeListAttribute(MICROPROFILE_METRICS_ADDRESS, EXPOSED_SUBSYSTEMS, STAR).assertSuccess();
    }

    @AfterClass
    public static void cleanUpAndCloseClient() throws IOException {
        operations.writeAttribute(MICROPROFILE_METRICS_ADDRESS, PREFIX, defaultPrefixValue).assertSuccess();
        operations.writeAttribute(MICROPROFILE_METRICS_ADDRESS, EXPOSED_SUBSYSTEMS, defaultExposedSystem).assertSuccess();
        client.close();
    }

    @After
    public void exposeAllSubsystems() throws Exception {
        operations.writeListAttribute(MICROPROFILE_METRICS_ADDRESS, EXPOSED_SUBSYSTEMS, STAR).assertSuccess();
    }

    @Drone
    private WebDriver browser;

    @Page
    private MicroprofileMetricsPage page;

    @Inject
    private CrudOperations crudOperations;

    @Test
    public void toggleExposeAllSubsystems() throws Exception {
        boolean exposeAllSubsystems =
            operations.readAttribute(MICROPROFILE_METRICS_ADDRESS,
                EXPOSED_SUBSYSTEMS)
                .value()
                .equals(new ModelNodeGenerator.ModelNodeListBuilder().addAll(STAR).build());
        final AtomicReference<Optional<List<String>>> exposedSubsystems = new AtomicReference<>(Optional.empty());
        page.navigate();
        crudOperations.update(MICROPROFILE_METRICS_ADDRESS, page.getMicroprofileMetricsForm(),
            formFragment -> {
                if (exposeAllSubsystems) {
                    formFragment.flip(EXPOSE_ALL_SUBSYSTEMS, false);
                    exposedSubsystems.set(Optional.of(Arrays.asList(Random.name(), Random.name())));
                    formFragment.list(EXPOSED_SUBSYSTEMS).add(exposedSubsystems.get().get());
                } else {
                    formFragment.flip(EXPOSE_ALL_SUBSYSTEMS, true);
                }
            }, resourceVerifier -> {
                ModelNode value = new ModelNodeGenerator.ModelNodeListBuilder().addAll(STAR).build();
                if (exposeAllSubsystems) {
                    List<String> exposedSubsystemsList = exposedSubsystems.get().get();
                    value =
                        new ModelNodeGenerator.ModelNodeListBuilder().addAll(
                            exposedSubsystemsList.toArray(new String[exposedSubsystemsList.size()])).build();
                }
                resourceVerifier.verifyAttribute(EXPOSED_SUBSYSTEMS, value);
            });
    }

    @Test
    public void editExposedSubsystems() throws Exception {
        boolean exposeAllSubsystems =
            operations.readAttribute(MICROPROFILE_METRICS_ADDRESS,
                EXPOSED_SUBSYSTEMS)
                .value().equals(new ModelNodeGenerator.ModelNodeListBuilder().addAll(STAR).build());
        String[] exposedSubsystems = new String[] {Random.name(), Random.name()};
        page.navigate();
        crudOperations.update(MICROPROFILE_METRICS_ADDRESS, page.getMicroprofileMetricsForm(),
            formFragment -> {
                if (exposeAllSubsystems) {
                    formFragment.flip(EXPOSE_ALL_SUBSYSTEMS, false);
                }
                formFragment.list(EXPOSED_SUBSYSTEMS).add(Arrays.asList(exposedSubsystems));
            }, resourceVerifier -> resourceVerifier.verifyAttribute(EXPOSED_SUBSYSTEMS,
                new ModelNodeGenerator.ModelNodeListBuilder().addAll(exposedSubsystems).build()));
    }

    @Test
    public void toggleSecurityEnabled() throws Exception {
        boolean securityEnabled =
            operations.readAttribute(MICROPROFILE_METRICS_ADDRESS,
                SECURITY_ENABLED)
                .booleanValue(false);
        page.navigate();
        crudOperations.update(MICROPROFILE_METRICS_ADDRESS,
            page.getMicroprofileMetricsForm(), SECURITY_ENABLED, !securityEnabled);
    }
}
