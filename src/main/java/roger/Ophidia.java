package roger;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import roger.pathfind.TravelCommand;
import roger.pathfind.main.LookManager;
import roger.pathfind.main.PathRenderer;
import roger.pathfind.main.walk.Walker;
import java.util.Arrays;

@Mod(modid = Ophidia.MODID, version = Ophidia.VERSION)
public class Ophidia
{
    public static final String MODID = "Ophidia";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        registerCommands(new TravelCommand());
        registerListeners(this, new PathRenderer(), new Walker(), new LookManager());
    }

    private void registerCommands(ICommand... commands) {
        Arrays.stream(commands).forEachOrdered(ClientCommandHandler.instance::registerCommand);
    }

    private void registerListeners(Object... listeners) {
        Arrays.stream(listeners).forEachOrdered(MinecraftForge.EVENT_BUS::register);
    }
}
