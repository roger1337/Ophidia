package roger.pathfind.main;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class LookManager {

    private static LookManager instance;

    private boolean active;
    double yawTarget;
    double pitchTarget;

    private long lastUpdate;


    public LookManager() {
        instance = this;
    }

    public void setTarget(double yaw, double pitch) {
        this.yawTarget = yaw;
        this.pitchTarget = pitch;
        this.active = true;
        this.lastUpdate = System.currentTimeMillis();
    }

    public void cancel() {
        this.active = false;
    }
    @SubscribeEvent
    public void onRenderTickEnd(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !active)
            return;


        double yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        double pitch = Minecraft.getMinecraft().thePlayer.rotationPitch;

        if(Math.abs(yawTarget - yaw) < 1) {
            Minecraft.getMinecraft().thePlayer.rotationYaw = (float)yawTarget;
            Minecraft.getMinecraft().thePlayer.rotationPitch = (float)pitchTarget;

            active = false;
        }


        long msElapsed = System.currentTimeMillis() - lastUpdate;

        double diff = (double)msElapsed/200;


        Minecraft.getMinecraft().thePlayer.rotationYaw += (yawTarget - yaw)*diff;
        Minecraft.getMinecraft().thePlayer.rotationPitch += (pitchTarget - pitch)*diff;




        this.lastUpdate = System.currentTimeMillis();

    }

    public static LookManager getInstance() {
        return instance;
    }
}
