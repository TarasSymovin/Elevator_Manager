package data.person;

public class PersonImpl implements Person {

    private final String name;

    private final float weight;

    public PersonImpl(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    @Override
    public String getPersonName() {
        return name;
    }

    @Override
    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Person [" + name + "]";
    }
}
