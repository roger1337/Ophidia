package roger.util;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

public class Util {
    public static void msg(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }

    public static BlockPos getPlayerBlockPos() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        BlockPos pos = new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));

        if(isBlockSolid(pos)) {
            if (isBlockSolid(pos = pos.add(0,1,0)))
                player.addChatMessage(new ChatComponentText("player block pos was solid! (cannot continue)"));
        }

        return pos;
    }

    public static BlockPos toBlockPos(Vec3 vec) {
        return new BlockPos(Math.floor(vec.xCoord), Math.floor(vec.yCoord), Math.floor(vec.zCoord));
    }

    public static Vec3 vecMultiply(Vec3 vec, double scale) {
        return new Vec3(vec.xCoord * scale, vec.yCoord * scale, vec.zCoord * scale);
    }
    public static Vec3 toVec(BlockPos pos)  {
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }
    public static boolean isBlockSolid(BlockPos block) {
        return Minecraft.getMinecraft().theWorld.getBlockState(block)
                .getBlock().isBlockSolid(Minecraft.getMinecraft().theWorld, block, null) ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSlab ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStainedGlass ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPane ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockFence ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonExtension ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockEnderChest ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockTrapDoor ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockPistonBase ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockChest ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockStairs ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockCactus ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockWall ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockGlass ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSkull ||
                Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock() instanceof BlockSand;
    }


    public static double calculateAngleVec2D(Vec3 one, Vec3 two) {
        one = new Vec3(one.xCoord, 0, one.zCoord);
        two = new Vec3(two.xCoord, 0, two.zCoord);

        double oneMagnitude = one.distanceTo(new Vec3(0, 0, 0));
        double twoMagnitude = two.distanceTo(new Vec3(0, 0, 0));

        double deg = Math.toDegrees(Math.acos(one.dotProduct(two)/(oneMagnitude*twoMagnitude)));
        if(Double.isNaN(deg)) {

            //vector should be going in the opposite direction
            return 180;
        }
        return deg;
    }


}
