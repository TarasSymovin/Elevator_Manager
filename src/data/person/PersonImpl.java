package data.person;

public class PersonImpl implements Person {

    private final int id;

    private final String name;

    private final float weight;

    public PersonImpl(int id, String name, float weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public int getPersonId() {
        return id;
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
