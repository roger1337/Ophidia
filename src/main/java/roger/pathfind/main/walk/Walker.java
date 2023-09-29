package roger.pathfind.main.walk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import roger.util.LookUtil;
import roger.util.Util;
import roger.pathfind.main.LookManager;
import roger.pathfind.main.PathRenderer;
import roger.pathfind.main.processor.ProcessorManager;
import roger.pathfind.main.astar.AStarNode;
import roger.pathfind.main.astar.AStarPathFinder;
import roger.pathfind.main.path.PathElm;
import roger.pathfind.main.path.impl.FallNode;
import roger.pathfind.main.path.impl.JumpNode;
import roger.pathfind.main.path.impl.TravelNode;
import roger.pathfind.main.path.impl.TravelVector;
import roger.pathfind.main.walk.target.WalkTarget;
import roger.pathfind.main.walk.target.impl.FallTarget;
import roger.pathfind.main.walk.target.impl.JumpTarget;
import roger.pathfind.main.walk.target.impl.TravelTarget;
import roger.pathfind.main.walk.target.impl.TravelVectorTarget;

import java.util.List;

public class Walker {
    private static Walker instance;
    private boolean isActive;

    List<PathElm> path;
    WalkTarget currentTarget;

    public Walker() {
        instance = this;
    }

    public void walk(BlockPos start, BlockPos end, int nodeCount) {
        isActive = true;

        List<AStarNode> nodes = AStarPathFinder.compute(start, end, nodeCount);
        path = ProcessorManager.process(nodes);

        if(path.size() == 0) {
            isActive = false;
            currentTarget = null;
            return;
        }


        PathRenderer.getInstance().render(path);
        currentTarget = null;
    }


    // Key press in here
    @SubscribeEvent
    public void onClientTickPre(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END || Minecraft.getMinecraft().thePlayer == null)
            return;


        /*
        double motionX = Minecraft.getMinecraft().thePlayer.motionX;
        double motionZ = Minecraft.getMinecraft().thePlayer.motionZ;
        if(motionX > 0 || motionZ > 0)
             System.out.println("Motion: " + Minecraft.getMinecraft().thePlayer.motionX + " " + Minecraft.getMinecraft().thePlayer.motionZ);
        */

        if(!isActive)
            return;

        if(currentTarget == null)
            currentTarget = getCurrentTarget(path.get(0));

        WalkTarget playerOnTarget;
        if(!((playerOnTarget = onTarget()) == null))
            currentTarget = playerOnTarget;

        // while, so we don't skip ticks
        while (tick(currentTarget)) {
            // removes it
            path.remove(0);

            if(path.isEmpty()) {
                isActive = false;
                currentTarget = null;
                KeyBinding.setKeyBindState(Keyboard.KEY_W, false);
                KeyBinding.setKeyBindState(Keyboard.KEY_A, false);
                KeyBinding.setKeyBindState(Keyboard.KEY_D, false);
                KeyBinding.setKeyBindState(Keyboard.KEY_S, false);
                LookManager.getInstance().cancel();

                return;
            }

            currentTarget = getCurrentTarget(path.get(0));
        }

        KeyBinding.setKeyBindState(Keyboard.KEY_LCONTROL, true);
        Tuple<Double, Double> angles = LookUtil.getAngles(currentTarget.getCurrentTarget());
        LookManager.getInstance().setTarget(angles.getFirst().floatValue(), currentTarget instanceof JumpTarget ? -10 : 10);

        pressKeys(angles.getFirst().floatValue());
    }


    private void pressKeys(double targetYaw) {
        double difference = targetYaw - Minecraft.getMinecraft().thePlayer.rotationYaw;
        KeyBinding.setKeyBindState(Keyboard.KEY_W, false);
        KeyBinding.setKeyBindState(Keyboard.KEY_A, false);
        KeyBinding.setKeyBindState(Keyboard.KEY_S, false);
        KeyBinding.setKeyBindState(Keyboard.KEY_D, false);

        if(22.5 > difference && difference > -22.5) {   // Forwards

            KeyBinding.setKeyBindState(Keyboard.KEY_W, true);
        } else if(-22.5 > difference && difference > -67.5) {   // Forwards+Right

            KeyBinding.setKeyBindState(Keyboard.KEY_W, true);
            KeyBinding.setKeyBindState(Keyboard.KEY_A, true);
        } else if(-67.5 > difference && difference > -112.5) { // Right

            KeyBinding.setKeyBindState(Keyboard.KEY_A, true);
        } else if(-112.5 > difference && difference > -157.5) { // Backwards + Right

            KeyBinding.setKeyBindState(Keyboard.KEY_A, true);
            KeyBinding.setKeyBindState(Keyboard.KEY_S, true);
        } else if((-157.5 > difference && difference > -180) || (180 > difference && difference > 157.5)) { // Backwards

            KeyBinding.setKeyBindState(Keyboard.KEY_S, true);
        } else if(67.5 > difference && difference > 22.5) { // Forwards + Left

            KeyBinding.setKeyBindState(Keyboard.KEY_W, true);
            KeyBinding.setKeyBindState(Keyboard.KEY_D, true);

        } else if(112.5 > difference && difference > 67.5) { // Left

            KeyBinding.setKeyBindState(Keyboard.KEY_D, true);
        } else if(157.5 > difference && difference > 112.5) {  // Backwards+Left

            KeyBinding.setKeyBindState(Keyboard.KEY_S, true);
            KeyBinding.setKeyBindState(Keyboard.KEY_D, true);
        }
    }

    // This checks if the player is on any nodes further in the queue, which means the player, due to probably high speed, has skipped some. Then
    // this removes the nodes behind it and sets it as the current target.
    private WalkTarget onTarget() {
        for(int i = 0 ; i < path.size() ; i++) {
            PathElm elm = path.get(i);

            if(elm.playerOn(Minecraft.getMinecraft().thePlayer.getPositionVector())) {
                System.out.println("Returned true: " + elm);

                if(elm == currentTarget.getElm())
                    return null;



                // Get the next one if the player is on it
                // if its travel vector, we don't get the next one, cos we need to go to the dest.
                // if its jump, we don't get the next one, cos we need to jump.
                if(path.size() > i + 1 && !(elm instanceof TravelVector) && !(elm instanceof JumpNode)) {
                    System.out.println("E");
                    path.subList(0, i + 1).clear();
                } else {
                    path.subList(0, i).clear();
                }

                // cutting off might end jump target so stop jumping
                KeyBinding.setKeyBindState(Keyboard.KEY_SPACE, false);


                return getCurrentTarget(path.get(0));
            }
        }

        return null;
    }

    // The return value of this is if the node has been satisfied, and the next one should be polled.
    private boolean tick(WalkTarget current) {

        // We should improve the predicted motion calculation. Right now it's based on the estimate that the motion will last for 12 ticks, but this is different across speeds.
        Vec3 offset = new Vec3(Minecraft.getMinecraft().thePlayer.motionX, 0, Minecraft.getMinecraft().thePlayer.motionZ);
        Vec3 temp = offset;
        offset.add(temp);

        for(int i = 0 ; i < 12 ; i++) {

            // 0.54600006f is how much the motion stops after every tick after not moving.
            offset = offset.add((temp = Util.vecMultiply(temp, 0.54600006f)));
        }

        return current.tick(offset, Minecraft.getMinecraft().thePlayer.getPositionVector());
    }

    private WalkTarget getCurrentTarget(PathElm elm) {
        if(elm instanceof FallNode)
            return new FallTarget((FallNode) elm);
        if(elm instanceof TravelNode)
            return new TravelTarget((TravelNode) elm);
        if(elm instanceof TravelVector)
            return new TravelVectorTarget((TravelVector) elm);
        if(elm instanceof JumpNode) {
            if(path.size() > 1)
                return new JumpTarget((JumpNode) elm, getCurrentTarget(path.get(1)));
            return new JumpTarget((JumpNode) elm, null);
        }
        System.out.println("Wrong walk target");
        return null;
    }
    public boolean isActive() {
        return isActive;
    }

    public static Walker getInstance() {
        return instance;
    }
}
