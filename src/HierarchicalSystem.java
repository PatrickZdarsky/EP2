import codedraw.CodeDraw;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// A cosmic system that is composed of a central named body (of type 'NamedBodyForcePair')
// and an arbitrary number of subsystems (of type 'HierarchicalSystem') in its orbit.
// This class implements 'CosmicSystem'.
//
public class HierarchicalSystem implements CosmicSystem, MassiveIterable {

    private NamedBodyForcePair centerBody;

    private List<CosmicSystem> subsystems;

    // Initializes this system with a name and a central body.
    public HierarchicalSystem(NamedBodyForcePair central, CosmicSystem... inOrbit) {
        centerBody = central;
        subsystems = Arrays.asList(inOrbit);
    }

    @Override
    public Vector3 getMassCenter() {
        var weightedCenters = centerBody.getMassCenter().times(centerBody.getMass());

        for (var subsystem : subsystems) {
            weightedCenters = weightedCenters.plus(subsystem.getMassCenter().times(subsystem.getMass()));
        }

        weightedCenters = weightedCenters.times(1 / getMass());

        return weightedCenters;
    }

    @Override
    public double getMass() {
        return centerBody.getMass()
                + subsystems.stream().map(CosmicSystem::getMass).mapToDouble(Double::doubleValue).sum();
    }

    @Override
    public int numberOfBodies() {
        return 1 + subsystems.stream().map(CosmicSystem::numberOfBodies).mapToInt(Integer::intValue).sum();
    }

    @Override
    public double distanceTo(CosmicSystem cs) {
        return centerBody.distanceTo(cs);
    }

    @Override
    public void addForceFrom(Body b) {
        centerBody.addForceFrom(b);

        subsystems.forEach(system -> system.addForceFrom(b));
    }

    @Override
    public void addForceTo(CosmicSystem cs) {
        var sources = getBodies();

        int size = sources.size();
        for (int i = 0; i < size; i++) {
            cs.addForceFrom(sources.get(i));
        }
    }

    @Override
    public BodyLinkedList getBodies() {
        var list = new BodyLinkedList();
        list.addAll(centerBody.getBodies());

        subsystems.forEach(system -> list.addAll(system.getBodies()));

        return list;
    }

    @Override
    public void update() {
        centerBody.update();

        subsystems.forEach(CosmicSystem::update);
    }

    @Override
    public void markCentralBodies() {
        centerBody.setName("Central "+centerBody.getName());

        subsystems.forEach(CosmicSystem::markCentralBodies);
    }

    @Override
    public void draw(CodeDraw cd) {
        centerBody.draw(cd);

        subsystems.forEach(system -> system.draw(cd));
    }

    @Override
    public String toString() {
        return centerBody.getName() + " {" + subsystems.stream().map(CosmicSystem::toString)
                .collect(Collectors.joining(", ")) + "}";
    }

    @Override
    public MassiveIterator iterator() {
        return new HierarchicalSystemIterator(this);
    }

    public class HierarchicalSystemIterator implements MassiveIterator {

        private final HierarchicalSystem system;

        MassiveIterator currentIterator;

        private byte index = 0;
        private Massive next;

        public HierarchicalSystemIterator(HierarchicalSystem system) {
            this.system = system;

            next = system.centerBody;
        }

        private Massive retrieveNext() {
            if (currentIterator != null) {
                if (currentIterator.hasNext()) {
                    return currentIterator.next();
                }
                currentIterator = null;
            }

            if (index >= system.subsystems.size())
                return null;

            var nextSystem = system.subsystems.get(index++);
            
            if (nextSystem instanceof Massive)
                return (Massive) nextSystem;

            currentIterator = ((HierarchicalSystem) nextSystem).iterator();

            return currentIterator != null ? currentIterator.next() : null;
        }

        @Override
        public Massive next() {
            var currentNext = next;
            if (currentNext == null)
                throw new NoSuchElementException();


            next = retrieveNext();

            return currentNext;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }
    }
}
