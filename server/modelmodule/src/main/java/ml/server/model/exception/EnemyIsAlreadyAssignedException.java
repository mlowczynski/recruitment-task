package ml.server.model.exception;

import java.text.MessageFormat;

public class EnemyIsAlreadyAssignedException extends RuntimeException {
    public EnemyIsAlreadyAssignedException (final Long enemyId, final Long heroId) {
        super(MessageFormat.format("Enemy: {0} is already assigned to hero: {1}", enemyId, heroId));
    }
}
