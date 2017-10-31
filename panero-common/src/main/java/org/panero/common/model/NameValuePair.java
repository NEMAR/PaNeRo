package org.panero.common.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement(name = "element")
public class NameValuePair<T> {
    @NotEmpty
    private String name;

    @NotNull
    private T value;

    public NameValuePair() {

    }

    public NameValuePair(final String name, final T value) {
        this.name = name;
        this.value = value;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @XmlElement
    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NameValuePair<?> that = (NameValuePair<?>) o;
        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }
}
