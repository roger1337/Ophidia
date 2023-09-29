package roger.pathfind.main.path.impl;

import net.minecraft.util.Vec3;
import roger.util.Util;
import roger.pathfind.main.path.Node;
import roger.pathfind.main.path.PathElm;

public class FallNode extends Node implements PathElm {
    public FallNode(int x, int y, int z) {
        super(x, y, z);
    }

    @Override
    public boolean playerOn(Vec3 playerPos) {
        return Util.toBlockPos(playerPos).equals(getBlockPos());
    }
}
