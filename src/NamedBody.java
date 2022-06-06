import codedraw.CodeDraw;

import java.util.Objects;

public class NamedBody implements Massive {

    private final Body body;
    private final String name;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBody(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;

        body = new Body(mass, massCenter, currentMovement);
    }

    public void setState(Vector3 position, Vector3 velocity) {
        body.setState(position, velocity);
    }

    // Returns the name of the body.
    public String getName() {
        return name;
    }

    // Compares `this` with the specified object. Returns `true` if the specified `o` is not
    // `null` and is of type `NamedBody` and both `this` and `o` have equal names.
    // Otherwise `false` is returned.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedBody)) return false;
        NamedBody namedBody = (NamedBody) o;
        return Objects.equals(name, namedBody.name);
    }

    // Returns the hashCode of `this`.
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    // Returns a readable representation including the name of this body.
    @Override
    public String toString() {
        return "NamedBody{" +
                "body=" + body +
                ", name=" + name + "}";
    }

    @Override
    public void draw(CodeDraw cd) {
        body.draw(cd);
    }

    @Override
    public void move(Vector3 force) {
        body.move(force);
    }

    @Override
    public double mass() {
        return body.mass();
    }

    @Override
    public Vector3 massCenter() {
        return body.massCenter();
    }

    @Override
    public int compareTo(Massive o) {
        return (int) (mass() - o.mass());
    }
}
