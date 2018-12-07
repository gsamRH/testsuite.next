package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.dmr.ModelDescriptionConstants.QUEUE_ADDRESS;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_CORE_QUEUE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.COREQUEUE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.COREQUEUE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.coreQueueAddress;

@RunWith(Arquillian.class)
public class CoreQueueTest extends AbstractServerDestinationsTest {

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.create(coreQueueAddress(SRV_UPDATE, COREQUEUE_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, COREQUEUE_CREATE);
                formFragment.text(QUEUE_ADDRESS, Random.name());
            }
        );
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_CORE_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getCoreQueueTable();
        FormFragment form = page.getCoreQueueForm();
        table.bind(form);

        crudOperations.delete(coreQueueAddress(SRV_UPDATE, COREQUEUE_DELETE), table, COREQUEUE_DELETE);
    }

}
