package org.panero.common.model;

import java.util.Set;
import java.util.function.Consumer;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.hibernate.validator.constraints.NotEmpty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Command {
    @NotEmpty
    private String name;

    @Valid
    private Set<NameValuePair<String>> options = Sets.newHashSet();

    public static Builder create(final String name) {
        return new Builder(name);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<NameValuePair<String>> getOptions() {
        return options;
    }

    public void setOptions(final Set<NameValuePair<String>> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("name", name)
                .add("options", options)
                .toString();
    }

    public static class Builder {
        private final Command object = new Command();
        private final Set<NameValuePair<String>> options = Sets.newHashSet();

        private Builder(final String name) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Parameter 'name' must not be empty");
            object.setName(name);
        }

        public <T> Builder option(final String name, final T value) {
            Preconditions.checkArgument(name != null, "Parameter 'name' must not be null");
            Preconditions.checkArgument(value != null, "Parameter 'value' must not be null");
            options.add(new NameValuePair<>(name, value.toString()));
            return this;
        }

        public Actions and() {
            return this.new Actions();
        }

        public class Actions {
            public Command buildIt() {
                object.setOptions(options);
                return object;
            }

            public void consumeIt(Consumer<Command> consumer) {
                consumer.accept(buildIt());
            }
        }
    }
}
