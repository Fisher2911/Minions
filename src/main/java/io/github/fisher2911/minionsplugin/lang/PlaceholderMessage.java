package io.github.fisher2911.minionsplugin.lang;

import io.github.fisher2911.fishcore.message.Message;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public record PlaceholderMessage(Message message, Map<String, String> placeholders) {

    public static PlaceholderMessage withoutPlaceholders(final Message message) {
        return new PlaceholderMessage(message, Collections.emptyMap());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlaceholderMessage that = (PlaceholderMessage) o;
        return Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.message);
    }
}
