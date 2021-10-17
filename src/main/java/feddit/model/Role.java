package feddit.model;

import feddit.model.hierarchy.DatabaseObject;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

/**
 * represents a concretization of a DatabaseObject
 *
 * @author Groupe A
 * @see feddit.model.hierarchy.DatabaseObject
 *
 * */
@Entity
@Table(name = "roles")
public class Role extends DatabaseObject {

    @NotEmpty
    @Column(name = "description")
    private String description;

    /**
     * Constructor method Role
     */

    public Role() {}

    /**
     * method will return "Role"
     * @return "Role" in string format
     */

    @Override
    public String getClazz() {
        return "Role";
    }

    /**
     * method will get the discription
     * @return The description in String format
     */

    public String getDescription() {
        return description;
    }

    /**
     * method will set the discription with the param description
     * @param description
     */

    public void setDescription(String description) {
        this.description = description;
    }

}