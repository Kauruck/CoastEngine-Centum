import com.kauruck.coastEngine.centum.Centum;
import com.kauruck.coastEngine.centum.entity.GlobalEntity;
import com.kauruck.coastEngine.centum.world.World;

public class Test {

    public static void main(String[] args){
        Centum.registerSystem(new TestSystem());
        Centum.startSystems();
        World world = new World();
        Centum.registerWorld(world);
        GlobalEntity testEntity = new GlobalEntity();
        testEntity.addComponent(new TestComponent());
        world.addGlobalEntity(testEntity);
    }
}
