import codedraw.CodeDraw;

// A body with a name and an associated force. The leaf node of
// a hierarchical cosmic system. This class implements 'CosmicSystem'.
//
public class NamedBodyForcePair implements CosmicSystem {

    private String name;
    private Body body;
    private Vector3 appliedForce;

    // Initializes this with name, mass, current position and movement. The associated force
    // is initialized with a zero vector.
    public NamedBodyForcePair(String name, double mass, Vector3 massCenter, Vector3 currentMovement) {
        this.name = name;
        this.body = new Body(mass, massCenter, currentMovement);
        this.appliedForce = new Vector3();
    }

    // Returns the name of the body.
    public String getName() {
        return name;

    }

    @Override
    public Vector3 getMassCenter() {
        return body.massCenter();
    }

    @Override
    public double getMass() {
        return body.mass();
    }

    @Override
    public int numberOfBodies() {
        return 1;
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return body.massCenter().distanceTo(cs.getMassCenter());
    }

    @Override
    public void addForceFrom(Body b) {
        if (b != body)
            appliedForce = appliedForce.plus(body.gravitationalForce(b));
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        cs.addForceFrom(body);
    }

    @Override
    public BodyLinkedList getBodies() {
        var list = new BodyLinkedList();
        list.addFirst(body);

        return list;
    }

    @Override
    public void update() {
        body.move(appliedForce);
        appliedForce = new Vector3();
    }

    @Override
    public void draw(CodeDraw cd) {
        body.draw(cd);
    }

    @Override
    public String toString() {
        return getName();
    }
}
