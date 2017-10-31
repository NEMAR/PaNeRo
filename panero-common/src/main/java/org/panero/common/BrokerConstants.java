package org.panero.common;

import org.apache.activemq.RedeliveryPolicy;

public abstract class BrokerConstants {
    public static final String PANERO_COMMANDS_DESTINATION = "Queue.PANERO.1.COMMANDS";

    public static final String PANERO_PLAIN_INPUT_DESTINATION = "Queue.PANERO.1.INPUT_PLAIN";

    public static final String PANERO_NORMALIZED_INPUT_DESTINATION = "Queue.PANERO.1.INPUT_NORMALIZED";

    public static final String PANERO_PERSISTENCE_INFLUXDB_DESTINATION = "Queue.PANERO.1.PERSISTENCE.INFLUXDB";

    public static final String PANERO_PERSISTENCE_HANADB_DESTINATION = "Queue.PANERO.1.PERSISTENCE.HANADB";

    public static final String PANERO_PERSISTENCE_MESAP_DESTINATION = "Queue.PANERO.1.PERSISTENCE.MESAP";

    public static final String PANERO_PERSISTENCE_CSV_DESTINATION = "Queue.PANERO.1.PERSISTENCE.CSV";

    public static final RedeliveryPolicy DEFAULT_REDELIVERY_POLICY = DefaultRedeliveryPolicy.INSTANCE;

    public static final String COMMAND_HEADER_FIELD = "command_name";

    public static final String COMMAND_PREPARE_OPTIMIZATION = "COMMAND_PREPARE_OPTIMIZATION";

    private static final class DefaultRedeliveryPolicy extends RedeliveryPolicy {
        static final DefaultRedeliveryPolicy INSTANCE = new DefaultRedeliveryPolicy();
        private static final long serialVersionUID = 42L;

        private DefaultRedeliveryPolicy() {
            super();
        }
    }
}
