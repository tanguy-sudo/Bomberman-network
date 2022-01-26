package models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AgentIterator.class, name = "AgentIterator")
})
/**
 * Interface d'un itérator
 * @author tanguy
 *
 */
public interface MyIterator {
    boolean hasNext();
    Object next();
}
