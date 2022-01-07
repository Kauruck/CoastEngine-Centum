import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.system.AbstractSystem;
import com.kauruck.coastEngine.core.resources.ResourceLocation;

public class TestSystem extends AbstractSystem<TestComponent> {
    public TestSystem() {
        super(10, TestComponent.class);
    }

    @Override
    public void process(AbstractComponent component, float deltaTime) {
        if(component instanceof TestComponent){
            TestComponent comp = (TestComponent) component;
            System.out.println(comp.test);
            comp.test++;
        }
    }

    @Override
    public void pre() {

    }

    @Override
    public void post() {

    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation("test","testSystem");
    }
}
