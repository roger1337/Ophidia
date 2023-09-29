package roger.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;

import java.util.List;

public class LookUtil {

    public static Tuple<Double, Double> getAngles(Entity origin, Entity target) {
        return getAngles(origin.getPositionVector().addVector(0, origin.getEyeHeight(), 0), target.getPositionVector().addVector(0, target.getEyeHeight(), 0), origin.rotationYaw);
    }

    public static Tuple<Double, Double> getAngles(BlockPos pos) {
        Entity player = Minecraft.getMinecraft().thePlayer;
        return getAngles(player.getPositionVector().addVector(0, player.getEyeHeight(), 0), new Vec3(pos).addVector(0.5f, 0.5f, 0.5f), player.rotationYaw);
    }

    public static Tuple<Double, Double> getAngles(Entity origin, Vec3 target) {
        return getAngles(origin.getPositionVector().addVector(0, origin.getEyeHeight(), 0), target, origin.rotationYaw);
    }

    public static Tuple<Double, Double> getAngles(Vec3 origin, Vec3 point, double currentYaw) {
        double dx = origin.xCoord - point.xCoord;
        double dy = origin.yCoord - point.yCoord;
        double dz = origin.zCoord - point.zCoord;
        double dist = Math.sqrt(dx*dx + dz*dz);

        if(dist == 0)
            return new Tuple<>(0.0, 0.0);

        double pitch = 90 - Math.toDegrees(Math.atan(dist/Math.abs(dy)));
        if (dy < 0)
            pitch = -pitch;

        double angle =  Math.toDegrees(Math.atan(Math.abs(dx/dz)));
        double yaw;
        if(dx > 0 && dz < 0)
            yaw = angle;
        else if(dx > 0 && dz > 0)
            yaw = 180 - angle;
        else if (dx < 0 && dz > 0)
            yaw = 180 + angle;
        else
            yaw = 360 - angle;

        double diff = yaw - currentYaw;

        while (diff > 180) {
            yaw -= 360;
            diff = yaw - currentYaw;
        }

        while (diff < -180) {
            yaw += 360;
            diff = yaw - currentYaw;
        }

        return new Tuple<>(yaw, pitch);
    }


    public static EntityLivingBase getClosestEntityToCrosshair(List<EntityLivingBase> entities) {
        float minDist = Float.MAX_VALUE;
        EntityLivingBase closest = null;

        for(EntityLivingBase entity : entities){
            // Get distance between the two entities (rotations)
            Tuple<Double, Double> yawPitch = getAngles(
                    Minecraft.getMinecraft().thePlayer, entity
            );

            // Compute the distance from the player's crosshair
            float distYaw = MathHelper.abs(MathHelper.wrapAngleTo180_float(yawPitch.getFirst().floatValue() - Minecraft.getMinecraft().thePlayer.rotationYaw));
            float distPitch = MathHelper.abs(MathHelper.wrapAngleTo180_float(yawPitch.getSecond().floatValue() - Minecraft.getMinecraft().thePlayer.rotationPitch));
            float dist = MathHelper.sqrt_float(distYaw*distYaw + distPitch*distPitch);

            // Get the closest entity
            if(dist < minDist) {
                closest = entity;
                minDist = dist;
            }
        }

        return closest;
    }
}
