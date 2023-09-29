package roger.pathfind.main.walk.target.impl;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.input.Keyboard;
import roger.util.Util;
import roger.pathfind.main.path.PathElm;
import roger.pathfind.main.path.impl.JumpNode;
import roger.pathfind.main.walk.target.WalkTarget;

public class JumpTarget extends WalkTarget {

    JumpNode node;
    WalkTarget next;

    boolean originalYSet;
    int originalY;

    int wait = 0;

    public JumpTarget(JumpNode node, WalkTarget next) {
        this.node = node;
        this.next = next;
    }
    @Override
    public boolean tick(Vec3 predictedMotionOnStop, Vec3 playerPos) {
        if(!originalYSet) {
            originalYSet = true;
            originalY = (int)playerPos.yCoord;
        }
        // last one
        if(next == null)
            return true;

        setCurrentTarget(next.getNodeBlockPos());

        wait++;

        // change value and stuff
        if(wait < 2)
            return false;


        KeyBinding.setKeyBindState(Keyboard.KEY_SPACE, true);

        if((int)playerPos.yCoord - originalY == 1 && Util.isBlockSolid(new BlockPos(playerPos).subtract(new Vec3i(0, 1, 0)))) {
            KeyBinding.setKeyBindState(Keyboard.KEY_SPACE, false);
            return true;
        }

        return false;
    }

    public BlockPos getNodeBlockPos() {
        return node.getBlockPos();
    }

    public PathElm getElm() {
        return node;
    }
}
