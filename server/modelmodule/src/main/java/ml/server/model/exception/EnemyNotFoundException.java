package ml.server.model.exception;

import java.text.MessageFormat;

public class EnemyNotFoundException extends RuntimeException {

    public EnemyNotFoundException (final Long id) {
        super(MessageFormat.format("Could not find enemy with id: {0}", id));
    }
}
