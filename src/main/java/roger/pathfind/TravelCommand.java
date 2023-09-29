package roger.pathfind;

import net.minecraft.util.ChatComponentText;
import roger.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import roger.pathfind.main.walk.Walker;

public class TravelCommand extends CommandBase {
    public String getCommandName() {
        return "travel";
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length > 2) {
            BlockPos currentPos = Util.getPlayerBlockPos();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(currentPos.toString()));

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            BlockPos targetPos = new BlockPos(x,y,z);

            Util.msg("Computing...");
            Walker.getInstance().walk(currentPos, targetPos, 10000);
        }
    }
}