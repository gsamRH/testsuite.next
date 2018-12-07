package org.jboss.hal.testsuite.test.configuration.messaging.server.destinations;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.hal.testsuite.Random;
import org.jboss.hal.testsuite.fragment.FormFragment;
import org.jboss.hal.testsuite.fragment.TableFragment;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.jboss.hal.dmr.ModelDescriptionConstants.ENTRIES;
import static org.jboss.hal.dmr.ModelDescriptionConstants.NAME;
import static org.jboss.hal.resources.Ids.ITEM;
import static org.jboss.hal.resources.Ids.MESSAGING_JMS_QUEUE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSQUEUE_CREATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSQUEUE_DELETE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.JMSQUEUE_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.SRV_UPDATE;
import static org.jboss.hal.testsuite.test.configuration.messaging.MessagingFixtures.jmsQueueAddress;

@RunWith(Arquillian.class)
public class JMSQueueTest extends AbstractServerDestinationsTest {

    @Test
    public void create() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);

        crudOperations.create(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_CREATE), table,
            formFragment -> {
                formFragment.text(NAME, JMSQUEUE_CREATE);
                formFragment.properties(ENTRIES).add(Random.name());
            }
        );
    }

    @Test
    public void editEntries() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);
        String val = Random.name();

        table.select(JMSQUEUE_UPDATE);
        crudOperations.update(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_UPDATE), form,
            formFragment -> formFragment.list(ENTRIES).add(val),
            resourceVerifier -> resourceVerifier.verifyListAttributeContainsValue(ENTRIES, val));
    }

    @Test
    public void remove() throws Exception {
        console.verticalNavigation().selectPrimary(MESSAGING_JMS_QUEUE + ID_DELIMITER + ITEM);
        TableFragment table = page.getJmsQueueTable();
        FormFragment form = page.getJmsQueueForm();
        table.bind(form);

        crudOperations.delete(jmsQueueAddress(SRV_UPDATE, JMSQUEUE_DELETE), table, JMSQUEUE_DELETE);
    }

}
