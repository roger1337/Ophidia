package roger.pathfind.main.processor.impl;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import roger.util.Util;
import roger.pathfind.main.path.PathElm;
import roger.pathfind.main.path.impl.TravelNode;
import roger.pathfind.main.path.impl.TravelVector;
import roger.pathfind.main.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class TravelProcessor extends Processor {




    // Here we detect paths on the same y level with ray trace to shorten the route (travel nodes)


    @Override
    public void process(List<PathElm> elms) {
        List<PathElm> newPath = new ArrayList<>();

        PathIter:
        for(int a = 0 ; a < elms.size() ; a++) {
            PathElm elm = elms.get(a);

            if(!(elm instanceof TravelNode)) {
                newPath.add(elm);
                continue;
            }

            TravelNode start = (TravelNode)elms.get(a);

            for(int b = elms.size() - 1 ; b > a ; b--) {
                if(!(elms.get(b) instanceof TravelNode)) {
                    continue;
                }

                TravelNode end = (TravelNode)elms.get(b);

                if(shouldOptimise(start, end)) {
                    a = b;
                    newPath.add(new TravelVector(start, end));
                    continue PathIter;
                }
            }

            newPath.add(elm);
        }

        elms.clear();
        elms.addAll(newPath);
    }




    // instead of getting surrounding blocks we could just shoot 2 vectors at an offset to each other
    public boolean shouldOptimise(TravelNode start, TravelNode end) {
        if(start.getY() != end.getY())
            return false;

        Vec3 startVec = new Vec3(start.getBlockPos());
        Vec3 endVec = new Vec3(end.getBlockPos());

        Vec3 differenceVector = endVec.subtract(startVec);
        Vec3 normalDelta = differenceVector.normalize();

        List<BlockPos> blocksWithinVector = new ArrayList<>();

        // populate the blocks in the path in the vector
        for(int scale = 0 ; scale < endVec.distanceTo(startVec) ; scale++) {
            Vec3 blockVec = startVec.add(Util.vecMultiply(normalDelta, scale));
            BlockPos blockPos = Util.toBlockPos(blockVec);

            if(!blocksWithinVector.contains(blockPos))
                blocksWithinVector.add(blockPos);
        }
        if(!blocksWithinVector.contains(Util.toBlockPos(endVec)))
            blocksWithinVector.add(Util.toBlockPos(endVec));

        blocksWithinVector.remove(Util.toBlockPos(startVec));

        for(BlockPos block : blocksWithinVector) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();

            // can optimise this by caching state
            BlockPos[] surroundings = new BlockPos[] {
                    new BlockPos(x+1, y, z+1),
                    new BlockPos(x, y, z+1),
                    new BlockPos(x-1, y, z+1),

                    new BlockPos(x+1, y, z),
                    new BlockPos(x, y, z),
                    new BlockPos(x-1, y, z),

                    new BlockPos(x+1, y, z-1),
                    new BlockPos(x, y, z-1),
                    new BlockPos(x-1, y, z-1),

                    new BlockPos(x+1, y+1, z+1),
                    new BlockPos(x, y+1, z+1),
                    new BlockPos(x-1, y+1, z+1),

                    new BlockPos(x+1, y+1, z),
                    new BlockPos(x, y+1, z),
                    new BlockPos(x-1, y+1, z),

                    new BlockPos(x+1, y+1, z-1),
                    new BlockPos(x, y+1, z-1),
                    new BlockPos(x-1, y+1, z-1),
            };

            for(BlockPos surroundingBlock : surroundings) {
                if(Util.isBlockSolid(surroundingBlock)) {
                    return false;
                }
            }

            if(!Util.isBlockSolid(block.subtract(new Vec3i(0, 1, 0))))
                return false;

        }

        return true;
    }


}
