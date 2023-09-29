package roger.pathfind.main.processor;

import roger.pathfind.main.path.PathElm;

import java.util.List;

public abstract class Processor {
    public abstract void process(List<PathElm> elms);
}
