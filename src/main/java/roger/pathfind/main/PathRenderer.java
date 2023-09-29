package roger.pathfind.main;

import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import roger.util.RenderUtil;
import roger.pathfind.main.path.Node;
import roger.pathfind.main.path.PathElm;
import roger.pathfind.main.path.impl.JumpNode;
import roger.pathfind.main.path.impl.TravelVector;
import roger.pathfind.main.walk.Walker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PathRenderer {


    private static PathRenderer renderer;
    private List<PathElm> path = new ArrayList<>();

    public PathRenderer() {
        renderer = this;
    }

    public void render(List<PathElm> elms) {
        path = elms;
    }


    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(!Walker.getInstance().isActive())
            return;

        if (!path.isEmpty()) {
            Node lastNode = null;
            for (PathElm elm : path) {

                if(elm instanceof Node) {
                    if (elm instanceof JumpNode) {
                        RenderUtil.drawFilledEsp(((Node) elm).getBlockPosUnder().subtract(new Vec3i(0, 1, 0)), Color.GREEN);
                    }

                    if (lastNode != null) {
                        List<Vec3> lines = new ArrayList<>();
                        lines.add(new Vec3(lastNode.getBlockPos()).subtract(0, 0.5, 0));
                        lines.add(new Vec3(((Node) elm).getBlockPos()).subtract(0, 0.5, 0));
                        RenderUtil.drawLines(lines, 2f, event.partialTicks, new Color(138, 206, 255).getRGB());
                    }

                    lastNode = (Node) elm;
                }

                if(elm instanceof TravelVector) {
                    Node from = ((TravelVector) elm).getFrom();
                    Node to = ((TravelVector) elm).getTo();

                    List<Vec3> lines = new ArrayList<>();
                    if(lastNode != null)
                        lines.add(new Vec3(lastNode.getBlockPos()).subtract(0, 0.5, 0));

                    lines.add(new Vec3(from.getBlockPos()).subtract(0, 0.5, 0));
                    lines.add(new Vec3(to.getBlockPos()).subtract(0, 0.5, 0));

                    RenderUtil.drawLines(lines, 2f, event.partialTicks, new Color(138, 206, 255).getRGB());

                    RenderUtil.drawFilledEsp(from.getBlockPosUnder(), new Color(138, 206, 255));
                    RenderUtil.drawFilledEsp(to.getBlockPosUnder(), new Color(138, 206, 255));

                    lastNode = to;
                }
            }
        }
    }

    public static PathRenderer getInstance() {
        return renderer;
    }
}
