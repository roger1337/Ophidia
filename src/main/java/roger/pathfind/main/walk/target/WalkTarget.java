package roger.pathfind.main.walk.target;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import roger.util.Util;
import roger.pathfind.main.path.PathElm;

public abstract class WalkTarget {

    protected final int PREDICTED_MOTION_ANGLE = 20;
    private BlockPos currentTarget;
    public abstract boolean tick(Vec3 predictedMotionOnStop, Vec3 playerPos);

    public BlockPos getCurrentTarget() {
        return currentTarget;
    }
    protected void setCurrentTarget(BlockPos target) {
        this.currentTarget = target;
    }

    protected double calculateAnglePredictionDest(Vec3 predictedVec, Vec3 destVec) {
        return Util.calculateAngleVec2D(predictedVec, destVec);
    }

    public abstract BlockPos getNodeBlockPos();

    public abstract PathElm getElm();
}


