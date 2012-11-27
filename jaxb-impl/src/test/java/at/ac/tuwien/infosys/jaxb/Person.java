package at.ac.tuwien.infosys.jaxb;

import javax.xml.bind.annotation.Facets;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person")
public class Person {
    @XmlElement(required = true, name = "firstName")
    @Facets(pattern="[A-Z]+")
    private String firstName;

    @XmlElement(required = true, name = "lastName")
    private String lastName;

    @XmlElement(required = true, name = "gender")
    private Gender gender;
    
    public Person() {
    }
    
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}