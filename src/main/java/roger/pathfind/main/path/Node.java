package roger.pathfind.main.path;

import net.minecraft.util.BlockPos;

public abstract class Node {

    private final int x;
    private final int y;
    private final int z;

    public Node(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

    public BlockPos getBlockPosUnder() {
        return new BlockPos(x, y-1, z);
    }


}
