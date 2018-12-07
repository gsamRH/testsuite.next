package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_DIVERT;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DIVERT_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DIVERT_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DIVERT_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.DIVERT_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.FORWARDING_ADDRESS;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.divertAddress;

@RunWith(Arquillian.class)
public class DivertTest extends AbstractServerDestinationsTest {

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + ID_DELIMITER + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.create(divertAddress(SRV_UPDATE, DIVERT_CREATE), table, f -> {
            f.text(NAME, DIVERT_CREATE);
            f.text(DIVERT_ADDRESS, Random.name());
            f.text(FORWARDING_ADDRESS, Random.name());
        });
    }

    @Test
    public void tryCreate() {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + ID_DELIMITER + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.createWithErrorAndCancelDialog(table, DIVERT_CREATE, DIVERT_ADDRESS);
    }

    @Test
    public void editAddress() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + ID_DELIMITER + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        table.select(DIVERT_UPDATE);
        crudOperations.update(divertAddress(SRV_UPDATE, DIVERT_UPDATE), form, DIVERT_ADDRESS);
    }

    @Test
    public void tryEditAddress() {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + ID_DELIMITER + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        table.select(DIVERT_UPDATE);
        crudOperations.updateWithError(form, f -> f.clear(DIVERT_ADDRESS), DIVERT_ADDRESS);
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_DIVERT + ID_DELIMITER + ITEM);
        TableFragment table = page.getDivertTable();
        FormFragment form = page.getDivertForm();
        table.bind(form);

        crudOperations.delete(divertAddress(SRV_UPDATE, DIVERT_DELETE), table, DIVERT_DELETE);
    }

}
