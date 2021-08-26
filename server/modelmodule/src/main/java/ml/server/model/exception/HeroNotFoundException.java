package ml.server.model.exception;

import java.text.MessageFormat;

public class HeroNotFoundException extends RuntimeException {

    public HeroNotFoundException (final Long id) {
        super(MessageFormat.format("Could not find hero with id: {0}", id));
    }
}
